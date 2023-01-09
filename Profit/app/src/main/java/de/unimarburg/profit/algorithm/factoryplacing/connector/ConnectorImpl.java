package de.unimarburg.profit.algorithm.factoryplacing.connector;

import de.unimarburg.profit.model.Conveyer;
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
  private TileConnectionInfo[][] current2DConnectionMatrix = null;
  /**
   * The current factory the {@link Connector} is working on. Is always set when calling
   * {@code getReachableMines(Factory factory)}.
   */
  private Factory currentFactory = null;
  /**
   * The current distance from the factory in {@link Conveyer} jumps.
   */
  private int jumpCount;
  /**
   * The queue of next {@link Point}s in the current BFS.
   */
  private Deque<Point> queue;
  private Stack<Conveyer> placedConveyorsStack;
  /**
   * The maximum output count of a {@link Factory}. If the size of the factory grows, this value
   * must be adjusted. Otherwise, calling this classes methods will lead to
   * ArrayIndexOutOfBoundsExceptions.
   */
  private static final int FACTORY_OUTPUT_COUNT = 16;

  /**
   * Constructor of class {@link ConnectorImpl}. Sets the {@link Field} this {@link Connector} will
   * work on.
   *
   * @param field The field that is used by this {@link Connector}.
   */
  public ConnectorImpl(Field field) {
    this.field = field;
    this.fieldTiles = field.getTiles();
    this.connectionMatrix3D = new TileConnectionInfo[FACTORY_OUTPUT_COUNT][this.field.getHeight()][this.field.getWidth()];
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
   * otherwise.
   */
  @Override
  public synchronized boolean connectMines(Collection<Mine> minesToConnect) {
    this.placedConveyorsStack = (new Stack<>());
    var mineOutputs = gatherMineOutputs(minesToConnect);
    var successfullyConnected = true;

    for (var output : mineOutputs) {
      successfullyConnected = successfullyConnected && connectMineToFactory(output);
    }

    for (Conveyer conveyer : this.placedConveyorsStack) {
      conveyer.setConnectedFactory(this.currentFactory);
    }

    return successfullyConnected;
  }

  /**
   * Connects a {@link Mine} to the {@code currentFactory}.
   *
   * @param output An output of a {@link Mine} or {@link Conveyer}.
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
   * Removes the top of the stack of recently placed {@link Conveyer}s.
   */
  private void removeTopOfConveyorStack() {
    if (!this.placedConveyorsStack.isEmpty()) {
      Conveyer topOfStack = this.placedConveyorsStack.pop();
      try {
        this.field.removeBaseObject(topOfStack);
      } catch (CouldNotRemoveObjectException e) {
        System.err.println(e.getMessage());
        System.err.println(Arrays.toString(e.getStackTrace()));
      }
    }
  }

  /**
   * Places a {@link Conveyer} on the {@link Field}.
   *
   * @param conveyorTriple A description of the conveyor that is supposed to be placed.
   * @return true if the {@link Conveyer} has been placed successfully, false otherwise.
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
   * Creates a new {@link Conveyer}-object from a given {@link ConveyorTriple}.
   *
   * @param conveyorTriple The {@link ConveyorTriple} the {@link Conveyer} is created from.
   * @return The created {@link Conveyer} if it could be created, {@code null} otherwise.
   */
  private Conveyer createNewConveyorFrom(ConveyorTriple conveyorTriple) {
    return switch (conveyorTriple.input.x - conveyorTriple.output.x) {
      case 0 -> switch (conveyorTriple.input.y - conveyorTriple.output.y) {
        case -2 -> // SUBTYPE 1
            Conveyer.createConveyor(conveyorTriple.input.x,
                (conveyorTriple.input.y + conveyorTriple.output.y) / 2,
                ConveyorSubType.SHORT_OUTPUT_SOUTH);
        case 2 -> // SUBTYPE 3
            Conveyer.createConveyor(conveyorTriple.input.x,
                (conveyorTriple.input.y + conveyorTriple.output.y) / 2,
                ConveyorSubType.SHORT_OUTPUT_NORTH);
        case -3 -> // SUBTYPE 5
            Conveyer.createConveyor(conveyorTriple.input.x,
                (conveyorTriple.input.y + conveyorTriple.output.y) / 2,
                ConveyorSubType.LONG_OUTPUT_SOUTH);
        case 3 -> // SUBTYPE 7
            Conveyer.createConveyor(conveyorTriple.input.x,
                (conveyorTriple.input.y + conveyorTriple.output.y) / 2,
                ConveyorSubType.LONG_OUTPUT_NORTH);
        default -> null;
      };
      case -2 -> // SUBTYPE 0
          Conveyer.createConveyor((conveyorTriple.input.x + conveyorTriple.output.x) / 2,
              conveyorTriple.input.y, ConveyorSubType.SHORT_OUTPUT_EAST);
      case 2 -> // SUBTYPE 2
          Conveyer.createConveyor((conveyorTriple.input.x + conveyorTriple.output.x) / 2,
              conveyorTriple.input.y, ConveyorSubType.SHORT_OUTPUT_WEST);
      case -3 -> // SUBTYPE 4
          Conveyer.createConveyor((conveyorTriple.input.x + conveyorTriple.output.x) / 2,
              conveyorTriple.input.y, ConveyorSubType.LONG_OUTPUT_EAST);
      case 3 -> // SUBTYPE 6
          Conveyer.createConveyor((conveyorTriple.input.x + conveyorTriple.output.x) / 2,
              conveyorTriple.input.y, ConveyorSubType.LONG_OUTPUT_WEST);
      default -> null;
    };
  }

  /**
   * Collects all inputs around a given output and saves the input together with the given output in
   * a priority queue. The queue is in ascending order of the jump counts of the
   * {@link ConveyorTriple}s.
   *
   * @param output The output the inputs are related to.
   * @return A queue of {@link ConveyorTriple}s for every {@link Conveyer} that can be placed with
   * the given output.
   */
  private Queue<ConveyorTriple> gatherInputsInQueue(Point output) {
    var conveyorQueue = new PriorityQueue<ConveyorTriple>(
        Comparator.comparingInt(a -> a.jumpCount));
    for (TileConnectionInfo[][] layer : this.connectionMatrix3D) {
      // NORTH TILE
      if (output.y - 1 >= 0) {
        addConnectionTriples(conveyorQueue, layer, output.x, output.y - 1);
      }
      // EAST TILE
      if (output.x + 1 < this.field.getWidth()) {
        addConnectionTriples(conveyorQueue, layer, output.x + 1, output.y);
      }
      // SOUTH TILE
      if (output.y + 1 < this.field.getHeight()) {
        addConnectionTriples(conveyorQueue, layer, output.x, output.y + 1);
      }
      // WEST TILE
      if (output.x - 1 >= 0) {
        addConnectionTriples(conveyorQueue, layer, output.x - 1, output.y);
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
            layer[output.y][output.x].jumpCount));
      }
    }
  }

  /**
   * Checks if a {@link Point} is adjacent to a factory or to the input of a {@link Conveyer} that
   * is transitive adjacent to the {@code currentFactory}.
   *
   * @param output The coordinates that are checked for being adjacent to a factory- or
   *               conveyor-input
   * @return {@code true} if the given coordinates are adjacent to a factory- or conveyor-input,
   * false otherwise.
   */
  private boolean isAdjacentToFactory(Point output) {
    var factoryInputFound = false;
    // NORTH SIDE
    if (output.y - 1 >= 0) {
      factoryInputFound =
          isFactoryInput(output.x, output.y - 1) || isFactoryConnectedConveyorInput(output.x,
              output.y - 1);
    }
    // EAST SIDE
    if (output.x + 1 < this.field.getWidth()) {
      factoryInputFound = factoryInputFound || isFactoryInput(output.x + 1, output.y)
          || isFactoryConnectedConveyorInput(output.x + 1, output.y);
    }
    // SOUTH SIDE
    if (output.y + 1 < this.field.getHeight()) {
      factoryInputFound = factoryInputFound || isFactoryInput(output.x, output.y + 1)
          || isFactoryConnectedConveyorInput(output.x, output.y + 1);
    }
    // WEST SIDE
    if (output.x - 1 >= 0) {
      factoryInputFound = factoryInputFound || isFactoryInput(output.x - 1, output.y)
          || isFactoryConnectedConveyorInput(output.x - 1, output.y);
    }
    return factoryInputFound;
  }

  /**
   * Checks if the input of a {@link Conveyer} is transitively connected to the input of a
   * {@link Factory}.
   *
   * @param x The x-coordinate of the input of a {@link Conveyer}.
   * @param y The y-coordinate of the input of a {@link Conveyer}.
   * @return {@code true] if the input defined by the given x- and y-coordinates is transitive
   * connected to a {@link Factory}, {@code false} otherwise.
   */
  private boolean isFactoryConnectedConveyorInput(int x, int y) {
    var object = this.fieldTiles[x][y].getObject().orElse(null);
    var isConnectedConveyor = object != null && object.getClass().equals(Conveyer.class)
        && this.placedConveyorsStack.contains((Conveyer) object);
    return this.fieldTiles[x][y].getType() == TileType.INPUT && isConnectedConveyor;
  }

  /**
   * Checks if the given coordinates belong to the input of the {@code currentFactory}.
   *
   * @param x The x-coordinate of a {@link Tile} from the current {@code fieldTiles}.
   * @param y The y-coordinate of a {@link Tile} from the current {@code fieldTiles}.
   * @return {@code true} if the given {@link Tile} is the input of a {@link Factory}, {@code false}
   * otherwise.
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
   * {@link Collection}.
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
    var factoryOutputs = gatherFactoryOutputCoordinates();
    var reachableMines = new HashSet<Mine>();

    for (int i = 0; i < factoryOutputs.length; ++i) {
      var coords = factoryOutputs[i];
      this.queue = new ArrayDeque<>();
      this.queue.add(coords);
      this.current2DConnectionMatrix = this.connectionMatrix3D[i];
      this.jumpCount = 0;

      while (!this.queue.isEmpty()) {
        ++this.jumpCount;
        var currentInputCoords = this.queue.poll();
        var possibleOutputs = gatherPossibleOutputs(reachableMines, currentInputCoords);
        var outputInputMap = determinePotentialConveyorInputsForOutputs(possibleOutputs);
        updateConnectionMatrix(outputInputMap);
      }
    }
    return reachableMines;
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
      Collection<Point> possibleOutputs) {
    var outputInputMapping = new HashMap<Point, Deque<Point>>();

    for (Point output : possibleOutputs) {
      var possibleInputs = gatherPossibleInputs(output);
      outputInputMapping.put(output, possibleInputs);
    }

    return outputInputMapping;
  }

  /**
   * Initializes an array with the coordinates of all output tiles of the {@code currentFactory}.
   *
   * @return coordinates for all input tiles for the {@code currentFactory}.
   */
  private Point[] gatherFactoryOutputCoordinates() {
    var factoryOutputs = new Point[FACTORY_OUTPUT_COUNT];
    var factoryX = this.currentFactory.getX();
    var factoryY = this.currentFactory.getY();
    var count = 0;

    for (int y = factoryY; y < factoryY + 5; ++y) {
      for (int x = factoryX; x < factoryX + 5; ++x) {
        if ((x == factoryX || y == factoryY || x == factoryX + 4 || y == factoryY + 4)
            && count < factoryOutputs.length) {
          factoryOutputs[count] = new Point(x, y, NodeType.INPUT);
          ++count;
        }
      }
    }

    return factoryOutputs;
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
    boolean conveyorCanBePlaced = output.y - 3 >= 0;
    if (conveyorCanBePlaced) {
      if (tileIsVacant(output.x, output.y - 1, NodeType.IN_BETWEEN) && tileIsVacant(output.x,
          output.y - 2, NodeType.IN_BETWEEN) && tileIsVacant(output.x, output.y - 3,
          NodeType.INPUT)) {
        inputDeque.add(new Point(output.x, output.y - 3, NodeType.INPUT));
      } else {
        conveyorCanBePlaced = false;
      }
    }
    if (!conveyorCanBePlaced && output.y - 2 >= 0) {
      if (tileIsVacant(output.x, output.y - 1, NodeType.IN_BETWEEN) && tileIsVacant(output.x,
          output.y - 2, NodeType.INPUT)) {
        inputDeque.add(new Point(output.x, output.y - 2, NodeType.INPUT));
      }
    }

    // EAST POINTING CONVEYOR
    conveyorCanBePlaced = output.x + 3 < this.field.getWidth();
    if (conveyorCanBePlaced) {
      if (tileIsVacant(output.x + 1, output.y, NodeType.IN_BETWEEN) && tileIsVacant(output.x + 2,
          output.y, NodeType.IN_BETWEEN) && tileIsVacant(output.x + 3, output.y, NodeType.INPUT)) {
        inputDeque.add(new Point(output.x + 3, output.y, NodeType.INPUT));
      } else {
        conveyorCanBePlaced = false;
      }
    }
    if (!conveyorCanBePlaced && output.x + 2 < this.field.getWidth()) {
      if (tileIsVacant(output.x + 1, output.y, NodeType.IN_BETWEEN) && tileIsVacant(output.x + 2,
          output.y, NodeType.INPUT)) {
        inputDeque.add(new Point(output.x + 2, output.y, NodeType.INPUT));
      }
    }

    // SOUTH POINTING CONVEYOR
    conveyorCanBePlaced = output.y + 3 < this.field.getHeight();
    if (conveyorCanBePlaced) {
      if (tileIsVacant(output.x, output.y + 1, NodeType.IN_BETWEEN) && tileIsVacant(output.x,
          output.y + 2, NodeType.IN_BETWEEN) && tileIsVacant(output.x, output.y + 3,
          NodeType.INPUT)) {
        inputDeque.add(new Point(output.x, output.y + 3, NodeType.INPUT));
      } else {
        conveyorCanBePlaced = false;
      }
    }
    if (!conveyorCanBePlaced && output.y + 2 < this.field.getHeight()) {
      if (tileIsVacant(output.x, output.y + 1, NodeType.IN_BETWEEN) && tileIsVacant(output.x,
          output.y + 2, NodeType.INPUT)) {
        inputDeque.add(new Point(output.x, output.y + 2, NodeType.IN_BETWEEN));
      }
    }

    // WEST POINTING CONVEYOR
    conveyorCanBePlaced = output.x - 3 >= 0;
    if (conveyorCanBePlaced) {
      if (tileIsVacant(output.x - 1, output.y, NodeType.IN_BETWEEN) && tileIsVacant(output.x - 2,
          output.y, NodeType.IN_BETWEEN) && tileIsVacant(output.x - 3, output.y, NodeType.INPUT)) {
        inputDeque.add(new Point(output.x - 3, output.y, NodeType.INPUT));
      } else {
        conveyorCanBePlaced = false;
      }
    }
    if (!conveyorCanBePlaced && output.x - 2 >= 0) {
      if (tileIsVacant(output.x - 1, output.y, NodeType.IN_BETWEEN) && tileIsVacant(output.x - 2,
          output.y, NodeType.IN_BETWEEN)) {
        inputDeque.add(new Point(output.x - 2, output.y, NodeType.INPUT));
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
    if (center.y - 1 >= 0) {
      var currTile = this.field.getTiles()[center.x][center.y - 1];
      var baseObject = currTile.getObject().orElse(null);
      if (baseObject != null && baseObject.getClass() == Mine.class
          && currTile.getType() == TileType.OUTPUT) {
        if (!reachableMines.contains((Mine) baseObject)) {
          reachableMines.add((Mine) baseObject);
        }
      } else {
        if (currTile.getType() == TileType.EMPTY
            && this.current2DConnectionMatrix[center.y - 1][center.x] == null) {
          possibleOutputCoords.add(new Point(center.x, center.y - 1, NodeType.OUTPUT));
        }
      }
    }

    if (center.x + 1 < this.field.getWidth()) {
      var currTile = this.field.getTiles()[center.x + 1][center.y];
      var baseObject = currTile.getObject().orElse(null);
      if (baseObject != null && baseObject.getClass() == Mine.class
          && currTile.getType() == TileType.OUTPUT) {
        if (!reachableMines.contains((Mine) baseObject)) {
          reachableMines.add((Mine) baseObject);
        }
      } else {
        if (currTile.getType() == TileType.EMPTY
            && this.current2DConnectionMatrix[center.y][center.x + 1] == null) {
          possibleOutputCoords.add(new Point(center.x + 1, center.y, NodeType.OUTPUT));
        }
      }
    }

    if (center.y + 1 < this.field.getHeight()) {
      var currTile = this.field.getTiles()[center.x][center.y + 1];
      var baseObject = currTile.getObject().orElse(null);
      if (baseObject != null && baseObject.getClass() == Mine.class
          && currTile.getType() == TileType.OUTPUT) {
        if (!reachableMines.contains((Mine) baseObject)) {
          reachableMines.add((Mine) baseObject);
        }
      } else {
        if (currTile.getType() == TileType.EMPTY
            && this.current2DConnectionMatrix[center.y + 1][center.x] == null) {
          possibleOutputCoords.add(new Point(center.x, center.y + 1, NodeType.OUTPUT));
        }
      }
    }

    if (center.x - 1 >= 0) {
      var currTile = this.field.getTiles()[center.x - 1][center.y];
      var baseObject = currTile.getObject().orElse(null);
      if (baseObject != null && baseObject.getClass() == Mine.class
          && currTile.getType() == TileType.OUTPUT) {
        if (!reachableMines.contains((Mine) baseObject)) {
          reachableMines.add((Mine) baseObject);
        }
      } else {
        if (currTile.getType() == TileType.EMPTY
            && this.current2DConnectionMatrix[center.y][center.x - 1] == null) {
          possibleOutputCoords.add(new Point(center.x - 1, center.y, NodeType.OUTPUT));
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
   * @param xPos         The horizontal position of the tile that will be checked.
   * @param yPos         The vertical position of the tile that will be checked.
   * @param requiredType The {@link NodeType} the tile will be checked for.
   * @return true if a tile of the given type can be placed at the given coordinates.
   */
  private boolean tileIsVacant(int xPos, int yPos, NodeType requiredType) {
    if (requiredType == NodeType.INPUT) {
      return this.fieldTiles[xPos][yPos].getType() == TileType.EMPTY && (
          this.current2DConnectionMatrix[yPos][xPos] == null
              || this.current2DConnectionMatrix[yPos][xPos].type == NodeType.INPUT);
    } else {
      return (this.fieldTiles[xPos][yPos].getType() == TileType.EMPTY
          || this.fieldTiles[xPos][yPos].getType() == TileType.CROSSABLE)
          && this.current2DConnectionMatrix[yPos][xPos] == null;
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
   * Determines the {@link Orientation} of the {@link Conveyer} defined by {@code output} and
   * {@code input} and writes it to the {@code current2DConnectionMatrix}. No check is performed for
   * validity of the placed {@link Conveyer}, because only valid conveyors are expected by this
   * method.
   *
   * @param output The coordinates of the output of the placed {@link Conveyer}.
   * @param input  The coordinates of the input of the placed {@link Conveyer}.
   */
<<<<<<< HEAD
  private void writeConveyorPositionToConnectionMatrix(Point output, Point input) {
    var orientation = output.x - input.x < 0 ? Orientation.EAST
        : output.x - input.x > 0 ? Orientation.WEST
            : output.y - input.y < 0 ? Orientation.SOUTH : Orientation.NORTH;
=======
  private synchronized void writeConveyorPositionToConnectionMatrix(Point output, Point input) {
    var orientation = output.x - input.x < 0 ? Orientation.EAST :
        output.x - input.x > 0 ? Orientation.WEST :
            output.y - input.y < 0 ? Orientation.SOUTH : Orientation.NORTH;
>>>>>>> e2844156fd0c73e7b066813d2c2fbd496de01d19
    // Input node handling.
    // In this function the input tile can only be of type input or equal to null.
    if (this.current2DConnectionMatrix[input.y][input.x] == null) {
      this.current2DConnectionMatrix[input.y][input.x] = new TileConnectionInfo(NodeType.INPUT,
          this.jumpCount, new LinkedList<>());
      this.current2DConnectionMatrix[input.y][input.x].connectedNodes.addLast(output);
      this.queue.add(input);
    } else if (this.current2DConnectionMatrix[input.y][input.x].type == NodeType.INPUT) {
      this.current2DConnectionMatrix[input.y][input.x].connectedNodes.addLast(output);
      if (this.current2DConnectionMatrix[input.y][input.x].jumpCount > this.jumpCount) {
        this.current2DConnectionMatrix[input.y][input.x].jumpCount = this.jumpCount;
      }
    }
    // Output node.
    if (this.current2DConnectionMatrix[output.y][output.x] == null) {
      this.current2DConnectionMatrix[output.y][output.x] = new TileConnectionInfo(NodeType.OUTPUT,
          this.jumpCount, new LinkedList<>());
    }
    this.current2DConnectionMatrix[output.y][output.x].connectedNodes.addLast(input);
    // HANDLE NODES IN BETWEEN.
    switch (orientation) {
      case NORTH -> {
        this.current2DConnectionMatrix[output.y - 1][output.x] = new TileConnectionInfo(
            NodeType.IN_BETWEEN, this.jumpCount, null);
        if (output.y - input.y > 2) {
          this.current2DConnectionMatrix[output.y - 2][output.x] = new TileConnectionInfo(
              NodeType.IN_BETWEEN, this.jumpCount, null);
        }
      }
      case EAST -> {
        this.current2DConnectionMatrix[output.y][output.x + 1] = new TileConnectionInfo(
            NodeType.IN_BETWEEN, this.jumpCount, null);
        if (input.x - output.x > 2) {
          this.current2DConnectionMatrix[output.y][output.x + 2] = new TileConnectionInfo(
              NodeType.IN_BETWEEN, this.jumpCount, null);
        }
      }
      case SOUTH -> {
        this.current2DConnectionMatrix[output.y + 1][output.x] = new TileConnectionInfo(
            NodeType.IN_BETWEEN, this.jumpCount, null);
        if (input.y - output.y > 2) {
          this.current2DConnectionMatrix[output.y + 2][output.x] = new TileConnectionInfo(
              NodeType.IN_BETWEEN, this.jumpCount, null);
        }
      }
      case WEST -> {
        this.current2DConnectionMatrix[output.y][output.x - 1] = new TileConnectionInfo(
            NodeType.IN_BETWEEN, this.jumpCount, null);
        if (output.x - input.x > 2) {
          this.current2DConnectionMatrix[output.y][output.x - 2] = new TileConnectionInfo(
              NodeType.IN_BETWEEN, this.jumpCount, null);
        }
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
     * The amount of {@link Conveyer} jumps that must be performed from this tile to reach the
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
     * The input of a {@link Conveyer} can be placed here.
     */
    INPUT,
    /**
     * The output of a {@link Conveyer} can be placed here.
     */
    OUTPUT,
    /**
     * The passable part of a {@link Conveyer} can be placed here.
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

    int x;
    int y;
    NodeType type;


    Point(int x, int y, NodeType type) {
      this.x = x;
      this.y = y;
      this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj != null && obj.getClass() == Point.class) {
        var lhs = (Point) obj;
        return this.x == lhs.x && this.y == lhs.y && this.type == lhs.type;
      }
      return false;
    }
  }

  /**
   * A {@link Conveyer} described by an input an output and the number of jumps that are required
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
