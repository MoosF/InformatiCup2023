package de.unimarburg.profit.algorithm.factoryplacing.connector;

import de.unimarburg.profit.model.Conveyor;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Tile;
import de.unimarburg.profit.model.enums.TileType;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

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
   * The current distance from the factory in {@link Conveyor} jumps.
   */
  private int jumpCount;
  /**
   * The queue of next {@link Point}s in the current BFS.
   */
  private Deque<Point> queue;
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
    this.connectionMatrix3D =
        new TileConnectionInfo[FACTORY_OUTPUT_COUNT][this.field.getHeight()][this.field.getWidth()];
    for (TileConnectionInfo[][] rows : this.connectionMatrix3D) {
      for (TileConnectionInfo[] columns : rows) {
        Arrays.fill(columns, null);
      }
    }
  }

  /**
   * TODO
   *
   * @param mines TODO
   * @param field TODO
   */
  @Override
  public void connect(Collection<Mine> mines, Field field) {
    /* */
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
   * @param minesToConnect The {@link Mine}s that are supposed to be connected to the given
   *                       {@link Factory}.
   * @return {@code true} if all mines in the {@link Collection} could be connected, false
   * otherwise.
   */
  @Override
  public boolean connectMines(Collection<Mine> minesToConnect) {
    // TODO
    //  Just use the results of getReachableMines to find paths from the given Mines to the Factory.
    //  Loop over collection:
    //  1. For the mine's output determine all inputs that are in the connectionMatrix.
    //  2. Choose the output that has the lowest value of jumps that must be performed to reach the
    //     factory.
    //  3. Repeat for the output of the conveyors on the way.
    //  4. Save all conveyors that are added to the path in a set in the current Connector-object.
    //  5. When an input is reached that belongs to the factory, or a conveyor that is in the set of
    //     placed conveyors, proceed with next iteration of the loop.
    return false;
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
   * Removes all objects that have been placed by the last call to
   * {@code connectMines(Collection<Mine> minesToConnect)}.
   */
  @Override
  public void removeAllPlacedObjects() {
    // TODO Remove all objects that have been saved in this object.
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
        if ((x == factoryX || y == factoryY || x == factoryX + 4 || y == factoryY + 4) &&
            count < factoryOutputs.length) {
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
      if (tileIsVacant(output.x, output.y - 1, NodeType.IN_BETWEEN) &&
          tileIsVacant(output.x, output.y - 2, NodeType.IN_BETWEEN) &&
          tileIsVacant(output.x, output.y - 3, NodeType.INPUT)) {
        inputDeque.add(new Point(output.x, output.y - 3, NodeType.INPUT));
      } else {
        conveyorCanBePlaced = false;
      }
    }
    if (!conveyorCanBePlaced && output.y - 2 >= 0) {
      if (tileIsVacant(output.x, output.y - 1, NodeType.IN_BETWEEN) &&
          tileIsVacant(output.x, output.y - 2, NodeType.INPUT)) {
        inputDeque.add(new Point(output.x, output.y - 2, NodeType.INPUT));
      }
    }

    // EAST POINTING CONVEYOR
    conveyorCanBePlaced = output.x + 3 < this.field.getWidth();
    if (conveyorCanBePlaced) {
      if (tileIsVacant(output.x + 1, output.y, NodeType.IN_BETWEEN) &&
          tileIsVacant(output.x + 2, output.y, NodeType.IN_BETWEEN) &&
          tileIsVacant(output.x + 3, output.y, NodeType.INPUT)) {
        inputDeque.add(new Point(output.x + 3, output.y, NodeType.INPUT));
      } else {
        conveyorCanBePlaced = false;
      }
    }
    if (!conveyorCanBePlaced && output.x + 2 < this.field.getWidth()) {
      if (tileIsVacant(output.x + 1, output.y, NodeType.IN_BETWEEN) &&
          tileIsVacant(output.x + 2, output.y, NodeType.INPUT)) {
        inputDeque.add(new Point(output.x + 2, output.y, NodeType.INPUT));
      }
    }

    // SOUTH POINTING CONVEYOR
    conveyorCanBePlaced = output.y + 3 < this.field.getHeight();
    if (conveyorCanBePlaced) {
      if (tileIsVacant(output.x, output.y + 1, NodeType.IN_BETWEEN) &&
          tileIsVacant(output.x, output.y + 2, NodeType.IN_BETWEEN) &&
          tileIsVacant(output.x, output.y + 3, NodeType.INPUT)) {
        inputDeque.add(new Point(output.x, output.y + 3, NodeType.INPUT));
      } else {
        conveyorCanBePlaced = false;
      }
    }
    if (!conveyorCanBePlaced && output.y + 2 < this.field.getHeight()) {
      if (tileIsVacant(output.x, output.y + 1, NodeType.IN_BETWEEN) &&
          tileIsVacant(output.x, output.y + 2, NodeType.INPUT)) {
        inputDeque.add(new Point(output.x, output.y + 2, NodeType.IN_BETWEEN));
      }
    }

    // WEST POINTING CONVEYOR
    conveyorCanBePlaced = output.x - 3 >= 0;
    if (conveyorCanBePlaced) {
      if (tileIsVacant(output.x - 1, output.y, NodeType.IN_BETWEEN) &&
          tileIsVacant(output.x - 2, output.y, NodeType.IN_BETWEEN) &&
          tileIsVacant(output.x - 3, output.y, NodeType.INPUT)) {
        inputDeque.add(new Point(output.x - 3, output.y, NodeType.INPUT));
      } else {
        conveyorCanBePlaced = false;
      }
    }
    if (!conveyorCanBePlaced && output.x - 2 >= 0) {
      if (tileIsVacant(output.x - 1, output.y, NodeType.IN_BETWEEN) &&
          tileIsVacant(output.x - 2, output.y, NodeType.IN_BETWEEN)) {
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
      if (baseObject != null && baseObject.getClass() == Mine.class &&
          currTile.getType() == TileType.OUTPUT) {
        if (!reachableMines.contains((Mine) baseObject)) {
          reachableMines.add((Mine) baseObject);
        }
      } else {
        if (currTile.getType() == TileType.EMPTY &&
            this.current2DConnectionMatrix[center.y - 1][center.x] == null) {
          possibleOutputCoords.add(new Point(center.x, center.y - 1, NodeType.OUTPUT));
        }
      }
    }

    if (center.x + 1 < this.field.getWidth()) {
      var currTile = this.field.getTiles()[center.x + 1][center.y];
      var baseObject = currTile.getObject().orElse(null);
      if (baseObject != null && baseObject.getClass() == Mine.class &&
          currTile.getType() == TileType.OUTPUT) {
        if (!reachableMines.contains((Mine) baseObject)) {
          reachableMines.add((Mine) baseObject);
        }
      } else {
        if (currTile.getType() == TileType.EMPTY &&
            this.current2DConnectionMatrix[center.y][center.x + 1] == null) {
          possibleOutputCoords.add(new Point(center.x + 1, center.y, NodeType.OUTPUT));
        }
      }
    }

    if (center.y + 1 < this.field.getHeight()) {
      var currTile = this.field.getTiles()[center.x][center.y + 1];
      var baseObject = currTile.getObject().orElse(null);
      if (baseObject != null && baseObject.getClass() == Mine.class &&
          currTile.getType() == TileType.OUTPUT) {
        if (!reachableMines.contains((Mine) baseObject)) {
          reachableMines.add((Mine) baseObject);
        }
      } else {
        if (currTile.getType() == TileType.EMPTY &&
            this.current2DConnectionMatrix[center.y + 1][center.x] == null) {
          possibleOutputCoords.add(new Point(center.x, center.y + 1, NodeType.OUTPUT));
        }
      }
    }

    if (center.x - 1 >= 0) {
      var currTile = this.field.getTiles()[center.x - 1][center.y];
      var baseObject = currTile.getObject().orElse(null);
      if (baseObject != null && baseObject.getClass() == Mine.class &&
          currTile.getType() == TileType.OUTPUT) {
        if (!reachableMines.contains((Mine) baseObject)) {
          reachableMines.add((Mine) baseObject);
        }
      } else {
        if (currTile.getType() == TileType.EMPTY &&
            this.current2DConnectionMatrix[center.y][center.x - 1] == null) {
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
      return this.fieldTiles[xPos][yPos].getType() == TileType.EMPTY &&
          (this.current2DConnectionMatrix[yPos][xPos] == null ||
              this.current2DConnectionMatrix[yPos][xPos].type == NodeType.INPUT);
    } else {
      return (this.fieldTiles[xPos][yPos].getType() == TileType.EMPTY ||
          this.fieldTiles[xPos][yPos].getType() == TileType.CROSSABLE) &&
          this.current2DConnectionMatrix[yPos][xPos] == null;
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
  private void writeConveyorPositionToConnectionMatrix(Point output, Point input) {
    var orientation = output.x - input.x < 0 ? Orientation.EAST :
        output.x - input.x > 0 ? Orientation.WEST :
            output.y - input.y < 0 ? Orientation.SOUTH : Orientation.NORTH;
    // Input node handling.
    // In this function the input tile can only be of type input or equal to null.
    if (this.current2DConnectionMatrix[input.y][input.x] == null) {
      this.current2DConnectionMatrix[input.y][input.x] =
          new TileConnectionInfo(NodeType.INPUT, this.jumpCount, new LinkedList<>());
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
      this.current2DConnectionMatrix[output.y][output.x] =
          new TileConnectionInfo(NodeType.OUTPUT, this.jumpCount, new LinkedList<>());
    }
    this.current2DConnectionMatrix[output.y][output.x].connectedNodes.addLast(input);
    // HANDLE NODES IN BETWEEN.
    switch (orientation) {
      case NORTH -> {
        this.current2DConnectionMatrix[output.y - 1][output.x] =
            new TileConnectionInfo(NodeType.IN_BETWEEN, this.jumpCount, null);
        if (output.y - input.y > 2) {
          this.current2DConnectionMatrix[output.y - 2][output.x] =
              new TileConnectionInfo(NodeType.IN_BETWEEN, this.jumpCount, null);
        }
      }
      case EAST -> {
        this.current2DConnectionMatrix[output.y][output.x + 1] =
            new TileConnectionInfo(NodeType.IN_BETWEEN, this.jumpCount, null);
        if (input.x - output.x > 2) {
          this.current2DConnectionMatrix[output.y][output.x + 2] =
              new TileConnectionInfo(NodeType.IN_BETWEEN, this.jumpCount, null);
        }
      }
      case SOUTH -> {
        this.current2DConnectionMatrix[output.y + 1][output.x] =
            new TileConnectionInfo(NodeType.IN_BETWEEN, this.jumpCount, null);
        if (input.y - output.y > 2) {
          this.current2DConnectionMatrix[output.y + 2][output.x] =
              new TileConnectionInfo(NodeType.IN_BETWEEN, this.jumpCount, null);
        }
      }
      case WEST -> {
        this.current2DConnectionMatrix[output.y][output.x - 1] =
            new TileConnectionInfo(NodeType.IN_BETWEEN, this.jumpCount, null);
        if (output.x - input.x > 2) {
          this.current2DConnectionMatrix[output.y][output.x - 2] =
              new TileConnectionInfo(NodeType.IN_BETWEEN, this.jumpCount, null);
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
    NORTH,
    EAST,
    SOUTH,
    WEST,
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
