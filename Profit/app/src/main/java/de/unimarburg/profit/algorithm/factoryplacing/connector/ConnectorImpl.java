package de.unimarburg.profit.algorithm.factoryplacing.connector;

import de.unimarburg.profit.model.Conveyor;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Tile;
import de.unimarburg.profit.model.enums.ConveyorSubType;
import de.unimarburg.profit.model.enums.TileType;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import de.unimarburg.profit.model.exceptions.CouldNotRemoveObjectException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

/**
 * This class supplies functionality for finding reachable {@link Mine}s for a {@link Factory} and
 * for connecting {@link Mine}s to a {@link Factory}.
 *
 * @author Fabian Moos
 */
public class ConnectorImpl implements Connector {

  /**
   * The field the {@code connectionMatrix3D} is created for.
   */
  private final Field field;
  /**
   * The {@link Tile}s that make up the current {@link Field}.
   */
  private final Tile[][] fieldTiles;
  /**
   * Matrix that saves connection information about all tiles. Every tile in the matrix knows which
   * other tiles in the matrix it can be connected to.
   *
   * <p>The first dimension is the layer (z-coordinate), the second dimension is the row
   * (y-coordinate) and the third dimension is the column (x-coordinate). So
   * {@code connectionMatrix[3][7][13]} means the 13th tile in the 7th row of the 3rd layer of the
   * matrix.<br/> Every layer represents one run to find all connectable tiles from a specific input
   * tile of the {@link Factory}.</p>
   */
  private final TileConnectionInfo[][][] connectionMatrix3D;
  /**
   * The current layer for the current loop of {@code getReachableMines(Factory factory)}.
   */
  private TileConnectionInfo[][] currentLayer = null;
  /**
   * The current factory the {@link Connector} is working on. Is always set when calling
   * {@code getReachableMines(Factory factory)}.
   */
  private Factory currentFactory = null;
  /**
   * The current distance from the factory in {@link Conveyor} jumps.
   */
  private int jumpCount;
  /**
   * The queue of next {@link Point}s in the current BFS.
   */
  private Deque<Point> queue;
  private Stack<Conveyor> placedConveyorsStack;
  /**
   * The maximum input count of a {@link Factory}. If the size of the factory grows, this value must
   * be adjusted. Otherwise, calling this classes methods will lead to
   * ArrayIndexOutOfBoundsExceptions.
   */
  private static final int FACTORY_OUTPUT_COUNT = 16;
  /**
   * The number of times a connection matrix will be build for every input of the factory.
   */
  private static final int RUNS = 3;
  private static final int LAYER_COUNT = FACTORY_OUTPUT_COUNT * RUNS;

  /**
   * Constructor of class {@link ConnectorImpl}. Sets the {@link Field} this {@link Connector} will
   * work on.
   *
   * @param field The field that is used by this {@link Connector}.
   */
  public ConnectorImpl(Field field) {
    this.field = field;
    this.fieldTiles = field.getTiles();
    this.connectionMatrix3D =
        new TileConnectionInfo[LAYER_COUNT][this.field.getHeight()][this.field.getWidth()];
    for (TileConnectionInfo[][] rows : this.connectionMatrix3D) {
      for (TileConnectionInfo[] columns : rows) {
        Arrays.fill(columns, null);
      }
    }
  }

  /**
   * Connects all {@link Mine}s to the {@code currentFactory} if possible.
   *
   * <p>This method does not check if the given mines can actually be connected to the
   * given {@link Factory}. The method {@code getReachableMines(Factory factory)} must be used to
   * find all {@link Mine}s that are not separated from the {@link Factory} by obstacles. Without
   * calling this function prior to connecting the mines,
   * {@code connectMines(Collection<Mine> minesToConnect)} will not work.</p>
   *
   * <p>This method tries to connect all mines in {@code minesToConnect} with best effort. If
   * at least one {@link Mine} cannot be connected, the result is still false, but the method still
   * tries to connect the rest of the mines.</p>
   *
   * @param minesToConnect The {@link Mine}s that are supposed to be connected to the given
   *                       {@link Factory}.
   * @return {@code true} if all mines in the {@link Collection} could be connected, false
   *         otherwise.
   */
  @Override
  public synchronized boolean connectMines(Collection<Mine> minesToConnect) {
    this.placedConveyorsStack = (new Stack<>());
    var mineOutputs = gatherMineOutputs(minesToConnect);
    var successfullyConnected = true;

    for (var output : mineOutputs) {
      successfullyConnected = successfullyConnected && connectMineToFactory(output);
    }

    return successfullyConnected;
  }

  /**
   * Connects a {@link Mine} to the {@code currentFactory}.
   *
   * @param output An output of a {@link Mine} or {@link Conveyor}.
   * @return true if the {@link Mine} is connected, when the method returns, false otherwise.
   */
  private boolean connectMineToFactory(Point output) {
    if (isAdjacentToFactory(output)) {
      return true;
    }
    var conveyorQueue = gatherInputsInQueue(output);
    var placed = false;

    while (!conveyorQueue.isEmpty()) {
      var currentTriple = conveyorQueue.poll();
      placed = placeConveyor(currentTriple);
      if (placed) {
        placed = this.connectMineToFactory(currentTriple.output);
        if (placed) {
          break;
        } else {
          removeTopOfConveyorStack();
        }
      }
    }
    return placed;
  }

  /**
   * Removes the top of the stack of recently placed {@link Conveyor}s.
   */
  private void removeTopOfConveyorStack() {
    if (!this.placedConveyorsStack.isEmpty()) {
      Conveyor topOfStack = this.placedConveyorsStack.pop();
      try {
        this.field.removeBaseObject(topOfStack);
      } catch (CouldNotRemoveObjectException e) {
        System.err.println(e.getMessage());
        System.err.println(Arrays.toString(e.getStackTrace()));
      }
    }
  }

  /**
   * Places a {@link Conveyor} on the {@link Field}.
   *
   * @param conveyorTriple A description of the conveyor that is supposed to be placed.
   * @return true if the {@link Conveyor} has been placed successfully, false otherwise.
   */
  private boolean placeConveyor(ConveyorTriple conveyorTriple) {
    var conveyor = createNewConveyorFrom(conveyorTriple);
    var hasBeenPlaced = true;
    if (conveyor != null) {
      try {
        this.field.addBaseObject(conveyor);
      } catch (CouldNotPlaceObjectException e) {
        hasBeenPlaced = false;
      }
    } else {
      hasBeenPlaced = false;
    }
    if (hasBeenPlaced) {
      this.placedConveyorsStack.push(conveyor);
    }
    return hasBeenPlaced;
  }

  /**
   * Creates a new {@link Conveyor}-object from a given {@link ConveyorTriple}.
   *
   * @param conveyorTriple The {@link ConveyorTriple} the {@link Conveyor} is created from.
   * @return The created {@link Conveyor} if it could be created, {@code null} otherwise.
   */
  private Conveyor createNewConveyorFrom(ConveyorTriple conveyorTriple) {
    return switch (conveyorTriple.input.coordinateA
        - conveyorTriple.output.coordinateA) {
      case 0 -> switch (conveyorTriple.input.coordinateB
          - conveyorTriple.output.coordinateB) {
        case -2 -> // SUBTYPE 1
            Conveyor.createConveyor(conveyorTriple.input.coordinateA,
                (conveyorTriple.input.coordinateB
                    + conveyorTriple.output.coordinateB) / 2,
                ConveyorSubType.SHORT_OUTPUT_SOUTH);
        case 2 -> // SUBTYPE 3
            Conveyor.createConveyor(conveyorTriple.input.coordinateA,
                (conveyorTriple.input.coordinateB
                    + conveyorTriple.output.coordinateB) / 2,
                ConveyorSubType.SHORT_OUTPUT_NORTH);
        case -3 -> // SUBTYPE 5
            Conveyor.createConveyor(conveyorTriple.input.coordinateA,
                (conveyorTriple.input.coordinateB
                    + conveyorTriple.output.coordinateB) / 2,
                ConveyorSubType.LONG_OUTPUT_SOUTH);
        case 3 -> // SUBTYPE 7
            Conveyor.createConveyor(conveyorTriple.input.coordinateA,
                (conveyorTriple.input.coordinateB
                    + conveyorTriple.output.coordinateB) / 2,
                ConveyorSubType.LONG_OUTPUT_NORTH);
        default -> null;
      };
      case -2 -> // SUBTYPE 0
          Conveyor.createConveyor((conveyorTriple.input.coordinateA
                  + conveyorTriple.output.coordinateA) / 2,
              conveyorTriple.input.coordinateB, ConveyorSubType.SHORT_OUTPUT_EAST);
      case 2 -> // SUBTYPE 2
          Conveyor.createConveyor((conveyorTriple.input.coordinateA
                  + conveyorTriple.output.coordinateA) / 2,
              conveyorTriple.input.coordinateB, ConveyorSubType.SHORT_OUTPUT_WEST);
      case -3 -> // SUBTYPE 4
          Conveyor.createConveyor((conveyorTriple.input.coordinateA
                  + conveyorTriple.output.coordinateA) / 2,
              conveyorTriple.input.coordinateB, ConveyorSubType.LONG_OUTPUT_EAST);
      case 3 -> // SUBTYPE 6
          Conveyor.createConveyor((conveyorTriple.input.coordinateA
                  + conveyorTriple.output.coordinateA) / 2,
              conveyorTriple.input.coordinateB, ConveyorSubType.LONG_OUTPUT_WEST);
      default -> null;
    };
  }

  /**
   * Collects all inputs around a given output and saves the input together with the given output in
   * a priority queue. The queue is in ascending order of the jump counts of the
   * {@link ConveyorTriple}s.
   *
   * @param output The output the inputs are related to.
   * @return A queue of {@link ConveyorTriple}s for every {@link Conveyor} that can be placed with
   *         the given output.
   */
  private Queue<ConveyorTriple> gatherInputsInQueue(Point output) {
    var conveyorQueue = new PriorityQueue<ConveyorTriple>(
        Comparator.comparingInt(a -> a.jumpCount));
    for (TileConnectionInfo[][] layer : this.connectionMatrix3D) {
      // NORTH TILE
      if (output.coordinateB - 1 >= 0) {
        addConnectionTriples(conveyorQueue, layer, output.coordinateA, output.coordinateB
            - 1);
      }
      // EAST TILE
      if (output.coordinateA + 1 < this.field.getWidth()) {
        addConnectionTriples(conveyorQueue, layer, output.coordinateA + 1, output.coordinateB);
      }
      // SOUTH TILE
      if (output.coordinateB + 1 < this.field.getHeight()) {
        addConnectionTriples(conveyorQueue, layer, output.coordinateA, output.coordinateB
            + 1);
      }
      // WEST TILE
      if (output.coordinateA - 1 >= 0) {
        addConnectionTriples(conveyorQueue, layer, output.coordinateA - 1, output.coordinateB);
      }
    }
    return conveyorQueue;
  }

  /**
   * Adds all {@link ConveyorTriple}s that are possible for the input with the given x- and
   * y-coordinates to the given queue.
   *
   * @param queue A {@link Queue} of {@link ConveyorTriple}s.
   * @param layer The layer the {@link ConveyorTriple}s are searched in.
   * @param x     x-coordinate of a tile on the given layer.
   * @param y     y-coordinate of a tile on the given layer.
   */
  private void addConnectionTriples(Queue<ConveyorTriple> queue, TileConnectionInfo[][] layer,
      int x, int y) {
    var tile = layer[y][x];
    if (tile != null && tile.type == NodeType.INPUT) {
      for (Point output : tile.connectedNodes) {
        queue.add(new ConveyorTriple(new Point(x, y, NodeType.INPUT), output,
            layer[output.coordinateB][output.coordinateA].jumpCount));
      }
    }
  }

  /**
   * Checks if a {@link Point} is adjacent to a factory or to the input of a {@link Conveyor} that
   * is transitive adjacent to the {@code currentFactory}.
   *
   * @param output The coordinates that are checked for being adjacent to a factory- or
   *               conveyor-input
   * @return {@code true} if the given coordinates are adjacent to a factory- or conveyor-input,
   *         false otherwise.
   */
  private boolean isAdjacentToFactory(Point output) {
    var factoryInputFound = false;
    // NORTH SIDE
    if (output.coordinateB - 1 >= 0) {
      factoryInputFound =
          isFactoryInput(output.coordinateA, output.coordinateB
              - 1) || isFactoryConnectedConveyorInput(output.coordinateA,
              output.coordinateB - 1);
    }
    // EAST SIDE
    if (output.coordinateA + 1 < this.field.getWidth()) {
      factoryInputFound = factoryInputFound || isFactoryInput(output.coordinateA
          + 1, output.coordinateB)
          || isFactoryConnectedConveyorInput(output.coordinateA + 1, output.coordinateB);
    }
    // SOUTH SIDE
    if (output.coordinateB + 1 < this.field.getHeight()) {
      factoryInputFound = factoryInputFound || isFactoryInput(output.coordinateA, output.coordinateB
          + 1)
          || isFactoryConnectedConveyorInput(output.coordinateA, output.coordinateB
          + 1);
    }
    // WEST SIDE
    if (output.coordinateA - 1 >= 0) {
      factoryInputFound = factoryInputFound || isFactoryInput(output.coordinateA
          - 1, output.coordinateB)
          || isFactoryConnectedConveyorInput(output.coordinateA - 1, output.coordinateB);
    }
    return factoryInputFound;
  }

  /**
   * Checks if the input of a {@link Conveyor} is transitively connected to the input of a
   * {@link Factory}.
   *
   * @param x The x-coordinate of the input of a {@link Conveyor}.
   * @param y The y-coordinate of the input of a {@link Conveyor}.
   * @return {@code true} if the input defined by the given x- and y-coordinates is transitive
   *         connected to a {@link Factory}, {@code false} otherwise.
   */
  private boolean isFactoryConnectedConveyorInput(int x, int y) {
    var object = this.fieldTiles[x][y].getObject().orElse(null);
    var isConnectedConveyor = object != null && object.getClass().equals(Conveyor.class)
        && this.placedConveyorsStack.contains((Conveyor) object);
    return this.fieldTiles[x][y].getType() == TileType.INPUT && isConnectedConveyor;
  }

  /**
   * Checks if the given coordinates belong to the input of the {@code currentFactory}.
   *
   * @param x The x-coordinate of a {@link Tile} from the current {@code fieldTiles}.
   * @param y The y-coordinate of a {@link Tile} from the current {@code fieldTiles}.
   * @return {@code true} if the given {@link Tile} is the input of a {@link Factory}, {@code false}
   *         otherwise.
   */
  private boolean isFactoryInput(int x, int y) {
    var object = this.fieldTiles[x][y].getObject().orElse(null);
    var isCorrectFactory = object != null && object.equals(this.currentFactory);
    return this.fieldTiles[x][y].getType() == TileType.INPUT && isCorrectFactory;
  }

  /**
   * Collect the coordinates of all outputs of all mines in the given {@link Collection} in an
   * array.
   *
   * @param mines A {@link Collection} of mines.
   * @return an array with coordinates of all outputs of all {@link Mine}s in the given
   *         {@link Collection}.
   */
  private Point[] gatherMineOutputs(Collection<Mine> mines) {
    var outputs = new Point[mines.size()];
    var index = 0;
    for (Mine mine : mines) {
      for (Tile tile : mine.getTiles()) {
        if (tile.getType() == TileType.OUTPUT) {
          var x = mine.getX();
          var y = mine.getY();
          outputs[index] = new Point(x + tile.getRelHorPos(), y + tile.getRelVerPos(),
              NodeType.OUTPUT);
          ++index;
          break;
        }
      }
    }
    return outputs;
  }

  /**
   * Finds all {@link Mine}s that are not separated from the given {@link Factory} by obstacles.
   *
   * @param factory The {@link Factory} the reachable mines are searched for. This value is saved in
   *                the {@link Connector} object.
   * @return a list of mines that can be connected to the factory.
   */
  @Override
  public Collection<Mine> getReachableMines(Factory factory) {
    this.currentFactory = factory;
    var factoryInputs = gatherFactoryInputCoordinates();
    var reachableMines = new HashSet<Mine>();

    buildConnectionMatrixWithFourTileConveyors(factoryInputs, reachableMines);
    buildConnectionMatrixWithThreeTileConveyors(factoryInputs, reachableMines);
    buildConnectionMatrixWithRandomConveyors(factoryInputs, reachableMines);

    return reachableMines;
  }

  /**
   * Tries to find outputs for the given inputs. This function prioritizes four tile
   * {@link Conveyor}s over three tile {@link Conveyor}s.
   *
   * @param factoryInputs  An array of input coordinates that are already connected to the factory.
   * @param reachableMines A {@link HashSet} of all mines that have been found until now.
   */
  private void buildConnectionMatrixWithFourTileConveyors(Point[] factoryInputs,
      HashSet<Mine> reachableMines) {
    for (int i = 0; i < FACTORY_OUTPUT_COUNT; ++i) {
      breadthFirstSearchMines(reachableMines, factoryInputs, i, Priority.FOUR_TILE_CONVEYOR);
    }
  }

  /**
   * Tries to find outputs for the given inputs. This function prioritizes three tile
   * {@link Conveyor}s over four tile {@link Conveyor}s.
   *
   * @param factoryInputs  An array of input coordinates that are already connected to the factory.
   * @param reachableMines A {@link HashSet} of all mines that have been found until now.
   */
  private void buildConnectionMatrixWithThreeTileConveyors(Point[] factoryInputs,
      HashSet<Mine> reachableMines) {
    for (int i = FACTORY_OUTPUT_COUNT; i < 2 * FACTORY_OUTPUT_COUNT; ++i) {
      breadthFirstSearchMines(reachableMines, factoryInputs, i, Priority.THREE_TILE_CONVEYOR);
    }
  }

  /**
   * Tries to find outputs for the given inputs. This function chooses the {@link Conveyor} length
   * randomly.
   *
   * @param factoryInputs  An array of input coordinates that are already connected to the factory.
   * @param reachableMines A {@link HashSet} of all mines that have been found until now.
   */
  private void buildConnectionMatrixWithRandomConveyors(Point[] factoryInputs,
      HashSet<Mine> reachableMines) {
    for (int i = 2 * FACTORY_OUTPUT_COUNT; i < 3 * FACTORY_OUTPUT_COUNT; ++i) {
      breadthFirstSearchMines(reachableMines, factoryInputs, i, Priority.RANDOM);
    }
  }

  /**
   * Modified BFS that tries to find each reachable mine for one input {@link Tile} of a
   * {@link Factory}.
   *
   * @param reachableMines A set of all mines that have been identified as reachable so far.
   * @param factoryInputs The inputs of the {@link Factory} that is currently processed.
   * @param i The current index of {@code factoryInputs}.
   * @param prio The prioritized {@link Conveyor} length that will be placed during the search.
   */
  private void breadthFirstSearchMines(HashSet<Mine> reachableMines, Point[] factoryInputs, int i,
      Priority prio) {
    // Initialization
    var coord = factoryInputs[i];
    this.queue = new ArrayDeque<>();
    this.queue.add(coord);
    this.currentLayer = this.connectionMatrix3D[i];
    this.currentLayer[coord.coordinateB][coord.coordinateA] = new TileConnectionInfo(NodeType.INPUT,
        0, new LinkedList<>());

    // BFS
    while (!this.queue.isEmpty()) {
      var currentInputCoord = this.queue.poll();
      this.jumpCount =
          this.currentLayer[currentInputCoord.coordinateB][currentInputCoord.coordinateA].jumpCount
              + 1;
      var possibleOutputs = gatherPossibleOutputs(reachableMines, currentInputCoord);
      var outputInputMap = determinePotentialConveyorInputsForOutputs(possibleOutputs, prio);
      updateConnectionMatrix(outputInputMap);
    }
  }

  /**
   * Takes a {@link Collection} of output {@link Point}s and determines coordinates for all possible
   * {@code NodeType.INPUT}s for every output.
   *
   * @param possibleOutputs All possible outputs around an input that have been found before calling
   *                        this function.
   * @return A map of (output, input list)-Key-Value-pairs. Is never {@code null}, but can be empty.
   */
  private Map<Point, Deque<Point>> determinePotentialConveyorInputsForOutputs(
      Collection<Point> possibleOutputs, Priority prio) {
    var outputInputMapping = new HashMap<Point, Deque<Point>>();

    for (Point output : possibleOutputs) {
      Deque<Point> possibleInputs;

      switch (prio) {
        case THREE_TILE_CONVEYOR -> possibleInputs = gatherPossibleInputsAlt1(output);
        case FOUR_TILE_CONVEYOR -> possibleInputs = gatherPossibleInputs(output);
        case RANDOM -> {
          if (new Random(System.currentTimeMillis()).nextInt(2) == 0) {
            possibleInputs = gatherPossibleInputs(output);
          } else {
            possibleInputs = gatherPossibleInputsAlt1(output);
          }
        }
        default -> possibleInputs = new ArrayDeque<>();
      }

      outputInputMapping.put(output, possibleInputs);
    }

    return outputInputMapping;
  }

  /**
   * Initializes an array with the coordinates of all output tiles of the {@code currentFactory}.
   *
   * @return coordinates for all input tiles for the {@code currentFactory}.
   */
  private Point[] gatherFactoryInputCoordinates() {
    var factoryInputs = new Point[LAYER_COUNT];
    var factoryX = this.currentFactory.getX();
    var factoryY = this.currentFactory.getY();
    var count = 0;

    for (int i = 0; i < RUNS; ++i) {
      for (int y = factoryY; y < factoryY + 5; ++y) {
        for (int x = factoryX; x < factoryX + 5; ++x) {
          if ((x == factoryX || y == factoryY || x == factoryX + 4 || y == factoryY + 4)
              && count < factoryInputs.length) {
            factoryInputs[count] = new Point(x, y, NodeType.INPUT);
            ++count;
          }
        }
      }
    }

    return factoryInputs;
  }

  /**
   * Gather all inputs for the given output coordinates.
   *
   * @param output The output coordinates the possible inputs are gathered for. This {@link Point}
   *               must be a valid output that has been set in the {@code current2DConnectionMatrix}
   *               before calling this method.
   * @return A queue of all found input coordinates.
   */
  private Deque<Point> gatherPossibleInputs(Point output) {
    var inputDeque = new ArrayDeque<Point>();
    // NORTH POINTING CONVEYOR
    boolean conveyorCanBePlaced = output.coordinateB - 3 >= 0;
    if (conveyorCanBePlaced) {
      if (tileIsVacant(output.coordinateA, output.coordinateB
          - 1, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA, output.coordinateB
          - 2, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA, output.coordinateB
          - 3, NodeType.INPUT)) {
        inputDeque.add(new Point(output.coordinateA, output.coordinateB
            - 3, NodeType.INPUT));
      } else {
        conveyorCanBePlaced = false;
      }
    }
    if (!conveyorCanBePlaced && output.coordinateB - 2 >= 0) {
      if (tileIsVacant(output.coordinateA, output.coordinateB
          - 1, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA, output.coordinateB
          - 2, NodeType.INPUT)) {
        inputDeque.add(new Point(output.coordinateA, output.coordinateB
            - 2, NodeType.INPUT));
      }
    }

    // EAST POINTING CONVEYOR
    conveyorCanBePlaced = output.coordinateA + 3 < this.field.getWidth();
    if (conveyorCanBePlaced) {
      if (tileIsVacant(output.coordinateA + 1, output.coordinateB, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA + 2, output.coordinateB, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA + 3, output.coordinateB, NodeType.INPUT)) {
        inputDeque.add(new Point(output.coordinateA + 3, output.coordinateB, NodeType.INPUT));
      } else {
        conveyorCanBePlaced = false;
      }
    }
    if (!conveyorCanBePlaced && output.coordinateA + 2 < this.field.getWidth()) {
      if (tileIsVacant(output.coordinateA + 1, output.coordinateB, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA + 2, output.coordinateB, NodeType.INPUT)) {
        inputDeque.add(new Point(output.coordinateA + 2, output.coordinateB, NodeType.INPUT));
      }
    }

    // SOUTH POINTING CONVEYOR
    conveyorCanBePlaced = output.coordinateB + 3 < this.field.getHeight();
    if (conveyorCanBePlaced) {
      if (tileIsVacant(output.coordinateA, output.coordinateB
          + 1, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA, output.coordinateB
          + 2, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA, output.coordinateB
          + 3, NodeType.INPUT)) {
        inputDeque.add(new Point(output.coordinateA, output.coordinateB
            + 3, NodeType.INPUT));
      } else {
        conveyorCanBePlaced = false;
      }
    }
    if (!conveyorCanBePlaced && output.coordinateB + 2 < this.field.getHeight()) {
      if (tileIsVacant(output.coordinateA, output.coordinateB
          + 1, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA, output.coordinateB
          + 2, NodeType.INPUT)) {
        inputDeque.add(new Point(output.coordinateA, output.coordinateB
            + 2, NodeType.IN_BETWEEN));
      }
    }

    // WEST POINTING CONVEYOR
    conveyorCanBePlaced = output.coordinateA - 3 >= 0;
    if (conveyorCanBePlaced) {
      if (tileIsVacant(output.coordinateA - 1, output.coordinateB, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA - 2, output.coordinateB, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA - 3, output.coordinateB, NodeType.INPUT)) {
        inputDeque.add(new Point(output.coordinateA - 3, output.coordinateB, NodeType.INPUT));
      } else {
        conveyorCanBePlaced = false;
      }
    }
    if (!conveyorCanBePlaced && output.coordinateA - 2 >= 0) {
      if (tileIsVacant(output.coordinateA - 1, output.coordinateB, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA - 2, output.coordinateB, NodeType.INPUT)) {
        inputDeque.add(new Point(output.coordinateA - 2, output.coordinateB, NodeType.INPUT));
      }
    }
    return inputDeque;
  }

  private Deque<Point> gatherPossibleInputsAlt1(Point output) {
    var inputDeque = new ArrayDeque<Point>();
    // NORTH POINTING CONVEYOR
    boolean conveyorCanBePlaced = output.coordinateB - 2 >= 0;
    if (conveyorCanBePlaced) {
      if (tileIsVacant(output.coordinateA, output.coordinateB
          - 1, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA, output.coordinateB
          - 2, NodeType.INPUT)) {
        inputDeque.add(new Point(output.coordinateA, output.coordinateB
            - 2, NodeType.INPUT));
      } else {
        conveyorCanBePlaced = false;
      }
    }
    if (!conveyorCanBePlaced && output.coordinateB - 3 >= 0) {
      if (tileIsVacant(output.coordinateA, output.coordinateB
          - 1, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA, output.coordinateB
          - 2, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA, output.coordinateB
          - 3, NodeType.INPUT)) {
        inputDeque.add(new Point(output.coordinateA, output.coordinateB
            - 3, NodeType.INPUT));
      }
    }

    // EAST POINTING CONVEYOR
    conveyorCanBePlaced = output.coordinateA + 2 < this.field.getWidth();
    if (conveyorCanBePlaced) {
      if (tileIsVacant(output.coordinateA + 1, output.coordinateB, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA + 2, output.coordinateB, NodeType.INPUT)) {
        inputDeque.add(new Point(output.coordinateA + 2, output.coordinateB, NodeType.INPUT));
      } else {
        conveyorCanBePlaced = false;
      }
    }
    if (!conveyorCanBePlaced && output.coordinateA + 3 < this.field.getWidth()) {
      if (tileIsVacant(output.coordinateA + 1, output.coordinateB, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA + 2, output.coordinateB, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA + 3, output.coordinateB,
          NodeType.INPUT)) {
        inputDeque.add(new Point(output.coordinateA + 3, output.coordinateB, NodeType.INPUT));
      }
    }

    // SOUTH POINTING CONVEYOR
    conveyorCanBePlaced = output.coordinateB + 2 < this.field.getHeight();
    if (conveyorCanBePlaced) {
      if (tileIsVacant(output.coordinateA, output.coordinateB
          + 1, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA, output.coordinateB
          + 2, NodeType.IN_BETWEEN)) {
        inputDeque.add(new Point(output.coordinateA, output.coordinateB
            + 2, NodeType.INPUT));
      } else {
        conveyorCanBePlaced = false;
      }
    }
    if (!conveyorCanBePlaced && output.coordinateB + 3 < this.field.getHeight()) {
      if (tileIsVacant(output.coordinateA, output.coordinateB
          + 1, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA, output.coordinateB
          + 2, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA, output.coordinateB
          + 3, NodeType.INPUT)) {
        inputDeque.add(new Point(output.coordinateA, output.coordinateB
            + 3, NodeType.IN_BETWEEN));
      }
    }

    // WEST POINTING CONVEYOR
    conveyorCanBePlaced = output.coordinateA - 2 >= 0;
    if (conveyorCanBePlaced) {
      if (tileIsVacant(output.coordinateA - 1, output.coordinateB, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA - 2, output.coordinateB, NodeType.INPUT)) {
        inputDeque.add(new Point(output.coordinateA - 2, output.coordinateB, NodeType.INPUT));
      } else {
        conveyorCanBePlaced = false;
      }
    }
    if (!conveyorCanBePlaced && output.coordinateA - 3 >= 0) {
      if (tileIsVacant(output.coordinateA - 1, output.coordinateB, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA - 2, output.coordinateB, NodeType.IN_BETWEEN)
          && tileIsVacant(output.coordinateA - 3, output.coordinateB, NodeType.INPUT)) {
        inputDeque.add(new Point(output.coordinateA - 3, output.coordinateB, NodeType.INPUT));
      }
    }
    return inputDeque;
  }

  /**
   * Gathers all possible outputs for a given central point. If the output of a mine is found, it is
   * added to the list of reachable mines.<br/> A tile in the connection matrix can only be used as
   * a possible output if there is currently nothing set on it, that means if it is still equal to
   * {@code null}.
   *
   * @param reachableMines A list of all reachable mines.
   * @param center         The {@link Point} the outputs will be searched for. Must be the
   *                       coordinates of an input.
   * @return a {@link Collection} of all {@link Point}s that can be used as output.
   */
  private Collection<Point> gatherPossibleOutputs(Collection<Mine> reachableMines, Point center) {
    var possibleOutputCoords = new ArrayList<Point>();
    if (center.coordinateB - 1 >= 0) {
      var currTile = this.field.getTiles()[center.coordinateA][center.coordinateB
          - 1];
      var baseObject = currTile.getObject().orElse(null);
      if (baseObject != null && baseObject.getClass() == Mine.class
          && currTile.getType() == TileType.OUTPUT) {
        if (!reachableMines.contains((Mine) baseObject)) {
          reachableMines.add((Mine) baseObject);
        }
      } else {
        if (currTile.getType() == TileType.EMPTY
            && this.currentLayer[center.coordinateB - 1][center.coordinateA] == null) {
          possibleOutputCoords.add(new Point(center.coordinateA, center.coordinateB
              - 1, NodeType.OUTPUT));
        }
      }
    }

    if (center.coordinateA + 1 < this.field.getWidth()) {
      var currTile = this.field.getTiles()[center.coordinateA + 1][center.coordinateB];
      var baseObject = currTile.getObject().orElse(null);
      if (baseObject != null && baseObject.getClass() == Mine.class
          && currTile.getType() == TileType.OUTPUT) {
        if (!reachableMines.contains((Mine) baseObject)) {
          reachableMines.add((Mine) baseObject);
        }
      } else {
        if (currTile.getType() == TileType.EMPTY
            && this.currentLayer[center.coordinateB][center.coordinateA + 1] == null) {
          possibleOutputCoords.add(
              new Point(center.coordinateA + 1, center.coordinateB, NodeType.OUTPUT));
        }
      }
    }

    if (center.coordinateB + 1 < this.field.getHeight()) {
      var currTile = this.field.getTiles()[center.coordinateA][center.coordinateB
          + 1];
      var baseObject = currTile.getObject().orElse(null);
      if (baseObject != null && baseObject.getClass() == Mine.class
          && currTile.getType() == TileType.OUTPUT) {
        if (!reachableMines.contains((Mine) baseObject)) {
          reachableMines.add((Mine) baseObject);
        }
      } else {
        if (currTile.getType() == TileType.EMPTY
            && this.currentLayer[center.coordinateB + 1][center.coordinateA] == null) {
          possibleOutputCoords.add(new Point(center.coordinateA, center.coordinateB
              + 1, NodeType.OUTPUT));
        }
      }
    }

    if (center.coordinateA - 1 >= 0) {
      var currTile = this.field.getTiles()[center.coordinateA - 1][center.coordinateB];
      var baseObject = currTile.getObject().orElse(null);
      if (baseObject != null && baseObject.getClass() == Mine.class
          && currTile.getType() == TileType.OUTPUT) {
        if (!reachableMines.contains((Mine) baseObject)) {
          reachableMines.add((Mine) baseObject);
        }
      } else {
        if (currTile.getType() == TileType.EMPTY
            && this.currentLayer[center.coordinateB][center.coordinateA - 1] == null) {
          possibleOutputCoords.add(
              new Point(center.coordinateA - 1, center.coordinateB, NodeType.OUTPUT));
        }
      }
    }

    return possibleOutputCoords;
  }

  /**
   * Checks if the tile at the given position is free enough for the requested {@link NodeType} to
   * be placed. The check is different for {@code NodeType.INPUT} tiles and for
   * {@code NodeType.IN_BETWEEN} tiles.
   *
   * @param horizontalPosition The horizontal position of the tile that will be checked.
   * @param verticalPosition   The vertical position of the tile that will be checked.
   * @param requiredType The {@link NodeType} the tile will be checked for.
   * @return true if a tile of the given type can be placed at the given coordinates.
   */
  private boolean tileIsVacant(int horizontalPosition, int verticalPosition,
      NodeType requiredType) {
    if (requiredType == NodeType.INPUT) {
      return this.fieldTiles[horizontalPosition][verticalPosition].getType() == TileType.EMPTY
          && (this.currentLayer[verticalPosition][horizontalPosition] == null
          || this.currentLayer[verticalPosition][horizontalPosition].type == NodeType.INPUT);
    } else {
      return (this.fieldTiles[horizontalPosition][verticalPosition].getType() == TileType.EMPTY
          || this.fieldTiles[horizontalPosition][verticalPosition].getType() == TileType.CROSSABLE)
          && this.currentLayer[verticalPosition][horizontalPosition] == null;
    }
  }

  /**
   * Updates the connection matrix with the given mapping.
   *
   * @param outputInputMapping Maps every output that has previously been determined for a specific
   *                           input to its own inputs, that can build a conveyor together with this
   *                           respective output.
   */
  private void updateConnectionMatrix(Map<Point, Deque<Point>> outputInputMapping) {
    for (Map.Entry<Point, Deque<Point>> mapping : outputInputMapping.entrySet()) {
      var output = mapping.getKey();
      var inputs = mapping.getValue();
      while (!inputs.isEmpty()) {
        var nextInput = inputs.poll();
        writeConveyorPositionToConnectionMatrix(output, nextInput);
      }
    }
  }

  /**
   * Determines the {@link Orientation} of the {@link Conveyor} defined by {@code output} and
   * {@code input} and writes it to the {@code current2DConnectionMatrix}. No check is performed for
   * validity of the placed {@link Conveyor}, because only valid conveyors are expected by this
   * method.
   *
   * @param output The coordinates of the output of the placed {@link Conveyor}.
   * @param input  The coordinates of the input of the placed {@link Conveyor}.
   */
  private synchronized void writeConveyorPositionToConnectionMatrix(Point output, Point input) {
    // Input node handling.
    // In this function the input tile can only be of type input or equal to null.
    if (this.currentLayer[input.coordinateB][input.coordinateA] == null) {
      this.currentLayer[input.coordinateB][input.coordinateA] = new TileConnectionInfo(
          NodeType.INPUT,
          this.jumpCount, new LinkedList<>());
      this.currentLayer[input.coordinateB][input.coordinateA].connectedNodes.addLast(output);
      this.queue.add(input);
    } else if (this.currentLayer[input.coordinateB][input.coordinateA].type == NodeType.INPUT) {
      this.currentLayer[input.coordinateB][input.coordinateA].connectedNodes.addLast(output);
      if (this.currentLayer[input.coordinateB][input.coordinateA].jumpCount > this.jumpCount) {
        this.currentLayer[input.coordinateB][input.coordinateA].jumpCount = this.jumpCount;
      }
    }
    // Output node.
    if (this.currentLayer[output.coordinateB][output.coordinateA] == null) {
      this.currentLayer[output.coordinateB][output.coordinateA] = new TileConnectionInfo(
          NodeType.OUTPUT,
          this.jumpCount, new LinkedList<>());
    }
    this.currentLayer[output.coordinateB][output.coordinateA].connectedNodes.addLast(input);
    var orientation = output.coordinateA
        - input.coordinateA < 0 ? Orientation.EAST
        : output.coordinateA
            - input.coordinateA > 0 ? Orientation.WEST
            : output.coordinateB
                - input.coordinateB < 0 ? Orientation.SOUTH : Orientation.NORTH;
    // HANDLE NODES IN BETWEEN.
    switch (orientation) {
      case NORTH -> {
        this.currentLayer[output.coordinateB - 1][output.coordinateA] = new TileConnectionInfo(
            NodeType.IN_BETWEEN, this.jumpCount, null);
        if (output.coordinateB
            - input.coordinateB > 2) {
          this.currentLayer[output.coordinateB - 2][output.coordinateA] = new TileConnectionInfo(
              NodeType.IN_BETWEEN, this.jumpCount, null);
        }
      }
      case EAST -> {
        this.currentLayer[output.coordinateB][output.coordinateA + 1] = new TileConnectionInfo(
            NodeType.IN_BETWEEN, this.jumpCount, null);
        if (input.coordinateA
            - output.coordinateA > 2) {
          this.currentLayer[output.coordinateB][output.coordinateA + 2] = new TileConnectionInfo(
              NodeType.IN_BETWEEN, this.jumpCount, null);
        }
      }
      case SOUTH -> {
        this.currentLayer[output.coordinateB + 1][output.coordinateA] = new TileConnectionInfo(
            NodeType.IN_BETWEEN, this.jumpCount, null);
        if (input.coordinateB
            - output.coordinateB > 2) {
          this.currentLayer[output.coordinateB + 2][output.coordinateA] = new TileConnectionInfo(
              NodeType.IN_BETWEEN, this.jumpCount, null);
        }
      }
      case WEST -> {
        this.currentLayer[output.coordinateB][output.coordinateA - 1] = new TileConnectionInfo(
            NodeType.IN_BETWEEN, this.jumpCount, null);
        if (output.coordinateA
            - input.coordinateA > 2) {
          this.currentLayer[output.coordinateB][output.coordinateA - 2] = new TileConnectionInfo(
              NodeType.IN_BETWEEN, this.jumpCount, null);
        }
      }
      default -> {
      }
    }
  }

  /**
   * Combines all relevant information about the connection possibilities of a {@link Tile}.
   */
  private static class TileConnectionInfo {

    /**
     * A list of tiles that can be connected to this tile. If the {@code type} of this tile is
     * {@code NodeType.INPUT} this is a list of output tiles. If the {@code type} of this tile is
     * {@code NodeType.OUTPUT} this is a list of input tiles.
     */
    LinkedList<Point> connectedNodes;
    /**
     * The amount of {@link Conveyor} jumps that must be performed from this tile to reach the
     * {@code currentFactory}.
     */
    int jumpCount;
    NodeType type;

    TileConnectionInfo(NodeType type, int jumpCount, LinkedList<Point> connectedNodes) {
      this.jumpCount = jumpCount;
      this.type = type;
      this.connectedNodes = connectedNodes;
    }
  }

  /**
   * The type of node in the {@code current2DConnectionMatrix}. Every node in the matrix can only be
   * of one type to make sure, that no output tile will ever be placed on a passable tile, which
   * would lead to errors.
   */
  private enum NodeType {
    /**
     * The input of a {@link Conveyor} can be placed here.
     */
    INPUT,
    /**
     * The output of a {@link Conveyor} can be placed here.
     */
    OUTPUT,
    /**
     * The passable part of a {@link Conveyor} can be placed here.
     */
    IN_BETWEEN,
  }

  /**
   * The orientation of a conveyor defined by input- and output-coordinates.
   */
  private enum Orientation {
    NORTH, EAST, SOUTH, WEST,
  }

  /**
   * A point defined by two coordinates x and y and the type of the node that could be placed at the
   * given coordinates.
   */
  private static class Point {

    int coordinateA;
    int coordinateB;
    NodeType type;


    Point(int coordinateA, int coordinateB, NodeType type) {
      this.coordinateA = coordinateA;
      this.coordinateB = coordinateB;
      this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj != null && obj.getClass() == Point.class) {
        var lhs = (Point) obj;
        return this.coordinateA
            == lhs.coordinateA && this.coordinateB
            == lhs.coordinateB && this.type == lhs.type;
      }
      return false;
    }
  }

  /**
   * A {@link Conveyor} described by an input an output and the number of jumps that are required
   * from the respective conveyor to reach the {@code currentFactory}.
   */
  private static class ConveyorTriple {

    final Point input;
    final Point output;
    final int jumpCount;

    ConveyorTriple(Point input, Point output, int jumpCount) {
      this.input = input;
      this.output = output;
      this.jumpCount = jumpCount;
    }

  }

  private static enum Priority {
    THREE_TILE_CONVEYOR,
    FOUR_TILE_CONVEYOR,
    RANDOM,
  }

  /**
   * Overrides the {@code toString()}-method of the {@link Object} class.
   *
   * <p>This method prints all layers of the {@code connectionMatrix3D}.</p>
   *
   * @return a {@link String} representation of the {@code connectionMatrix3D}.
   */
  @Override
  public String toString() {
    var stringBuilder = new StringBuilder();
    for (TileConnectionInfo[][] matrix : this.connectionMatrix3D) {
      stringBuilder.append(" ");
      int rowCount = 0;
      var greaterThanOrEqualTo10 = matrix[0].length >= 10;
      var greaterThanOrEqualTo100 = matrix[0].length >= 100;
      if (greaterThanOrEqualTo10) {
        stringBuilder.append(" ");
      }
      if (greaterThanOrEqualTo100) {
        stringBuilder.append(" |");
      }
      int count = 0;
      for (int i = 1; i <= matrix[0].length; ++i) {
        if (i % 10 == 0) {
          ++count;
          stringBuilder.append(":");
        } else {
          stringBuilder.append(i - count * 10);
        }
      }
      stringBuilder.append("\n-");
      if (greaterThanOrEqualTo10) {
        stringBuilder.append("-");
      }
      if (greaterThanOrEqualTo100) {
        stringBuilder.append("-|");
      }
      stringBuilder.append("-".repeat(matrix[0].length));
      stringBuilder.append("\n");
      for (TileConnectionInfo[] rows : matrix) {
        ++rowCount;
        if (rowCount < 100) {
          stringBuilder.append(" ");
        }
        if (rowCount < 10) {
          stringBuilder.append(" ");
        }
        stringBuilder.append(rowCount);
        stringBuilder.append("|");
        for (TileConnectionInfo value : rows) {
          if (value != null) {
            switch (value.type) {
              case INPUT -> stringBuilder.append("+");
              case OUTPUT -> stringBuilder.append("-");
              case IN_BETWEEN -> stringBuilder.append("o");
              default -> {
                // There is no other value than INPUT, OUTPUT and IN_BETWEEN in this enum,
                // Mr. Google Style, why do I need a default case here...? O_o
                // ALL HAIL THE Rust match STATEMENT!!!
              }
            }
          } else {
            stringBuilder.append(".");
          }
        }
        stringBuilder.append("|\n");
      }
      stringBuilder.append("\n\n\n");
    }
    return stringBuilder.toString();
  }
}
