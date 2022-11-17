package model;

import java.util.Map;

/**
 * This class models a {@link Conveyor}.
 *
 * @author Yannick Kraml
 */
public class Conveyor extends MovableObject {


  /**
   * Constructor of {@link Conveyor}.
   *
   * @param xCoord X-Coordinate of the {@link Conveyor}.
   * @param yCoord Y-Coordinate of the {@link Conveyor}.
   * @param tiles  Tiles, that construct the {@link Conveyor}.
   */
  private Conveyor(int xCoord, int yCoord, Tile[] tiles) {
    super(xCoord, yCoord, tiles);
  }

  /**
   * Creates an instance of a {@link Conveyor}.
   *
   * @param xCoord  X-Coordinate of the {@link Conveyor}.
   * @param yCoord  Y-Coordinate of the {@link Conveyor}.
   * @param subType Subtype of the {@link Conveyor}.
   * @return New instance of a {@link Conveyor}.
   */
  public static Conveyor createConveyor(int xCoord, int yCoord, ConveyorSubType subType) {

    Tile[] tiles = null;
    switch (subType) {

      case SHORT_NORTH_SOUTH -> {
        tiles = new Tile[]{
            new Tile(0, -1, TileType.INPUT),
            new Tile(0, 0, TileType.CROSSABLE),
            new Tile(0, 1, TileType.OUTPUT)};
      }
      case SHORT_EAST_WEST -> {
        tiles = new Tile[]{
            new Tile(-1, 0, TileType.INPUT),
            new Tile(0, 0, TileType.CROSSABLE),
            new Tile(1, 0, TileType.OUTPUT)};
      }
      case SHORT_SOUTH_NORTH -> {
        tiles = new Tile[]{
            new Tile(0, 1, TileType.INPUT),
            new Tile(0, 0, TileType.CROSSABLE),
            new Tile(0, -1, TileType.OUTPUT)};
      }
      case SHORT_WEST_EAST -> {
        tiles = new Tile[]{
            new Tile(1, 0, TileType.INPUT),
            new Tile(0, 0, TileType.CROSSABLE),
            new Tile(-1, 0, TileType.OUTPUT)};
      }
      case LONG_NORTH_SOUTH -> {
        tiles = new Tile[]{
            new Tile(0, -1, TileType.INPUT),
            new Tile(0, 0, TileType.CROSSABLE),
            new Tile(0, 1, TileType.CROSSABLE),
            new Tile(0, 2, TileType.OUTPUT)};
      }
      case LONG_EAST_WEST -> {
        tiles = new Tile[]{
            new Tile(2, 0, TileType.INPUT),
            new Tile(1, 0, TileType.CROSSABLE),
            new Tile(0, 0, TileType.CROSSABLE),
            new Tile(-1, 0, TileType.OUTPUT)};
      }
      case LONG_SOUTH_NORTH -> {
        tiles = new Tile[]{
            new Tile(0, 2, TileType.INPUT),
            new Tile(0, 1, TileType.CROSSABLE),
            new Tile(0, 0, TileType.CROSSABLE),
            new Tile(0, -1, TileType.OUTPUT)};
      }
      case LONG_WEST_EAST -> {
        tiles = new Tile[]{
            new Tile(-1, 0, TileType.INPUT),
            new Tile(0, 0, TileType.CROSSABLE),
            new Tile(1, 0, TileType.CROSSABLE),
            new Tile(2, 0, TileType.OUTPUT)};
      }
      default -> {
        throw new RuntimeException("Unknown branch.");
      }
    }

    return new Conveyor(xCoord, yCoord, tiles);
  }


  @Override
  public Map<ResourceType, Integer> getResourcesToOutput(
      Map<ResourceType, Integer> storedResources) {
    return storedResources;
  }
}
