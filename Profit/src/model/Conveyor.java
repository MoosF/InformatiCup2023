package model;

import java.util.Map;

/**
 * This class models a {@link Conveyor}.
 *
 * @author Yannick Kraml
 */
public class Conveyor extends MovableObject {

  private ConveyerSubType subType;

  /**
   * Constructor of {@link Conveyor}.
   *
   * @param horPos X-Coordinate of the {@link Conveyor}.
   * @param verPos Y-Coordinate of the {@link Conveyor}.
   * @param tiles  Tiles, that construct the {@link Conveyor}.
   */
  private Conveyor(int horPos, int verPos, Tile[] tiles, MovableObjectType type,
      ConveyerSubType subType) {
    super(horPos, verPos, tiles, type);
    this.subType = subType;
  }

  /**
   * Creates an instance of a {@link Conveyor}.
   *
   * @param horPos  X-Coordinate of the {@link Conveyor}.
   * @param verPos  Y-Coordinate of the {@link Conveyor}.
   * @param subType Subtype of the {@link Conveyor}.
   * @return New instance of a {@link Conveyor}.
   */
  public static Conveyor createConveyor(int horPos, int verPos, ConveyerSubType subType) {

    Tile[] tiles = null;
    switch (subType) {

      case SHORT_OUTPUT_SOUTH -> {
        tiles = new Tile[]{
            new Tile(0, -1, TileType.INPUT),
            new Tile(0, 0, TileType.CROSSABLE),
            new Tile(0, 1, TileType.OUTPUT)};
      }
      case SHORT_OUTPUT_EAST -> {
        tiles = new Tile[]{
            new Tile(-1, 0, TileType.INPUT),
            new Tile(0, 0, TileType.CROSSABLE),
            new Tile(1, 0, TileType.OUTPUT)};
      }
      case SHORT_OUTPUT_NORTH -> {
        tiles = new Tile[]{
            new Tile(0, 1, TileType.INPUT),
            new Tile(0, 0, TileType.CROSSABLE),
            new Tile(0, -1, TileType.OUTPUT)};
      }
      case SHORT_OUTPUT_WEST -> {
        tiles = new Tile[]{
            new Tile(1, 0, TileType.INPUT),
            new Tile(0, 0, TileType.CROSSABLE),
            new Tile(-1, 0, TileType.OUTPUT)};
      }
      case LONG_OUTPUT_SOUTH -> {
        tiles = new Tile[]{
            new Tile(0, -1, TileType.INPUT),
            new Tile(0, 0, TileType.CROSSABLE),
            new Tile(0, 1, TileType.CROSSABLE),
            new Tile(0, 2, TileType.OUTPUT)};
      }
      case LONG_OUTPUT_WEST -> {
        tiles = new Tile[]{
            new Tile(2, 0, TileType.INPUT),
            new Tile(1, 0, TileType.CROSSABLE),
            new Tile(0, 0, TileType.CROSSABLE),
            new Tile(-1, 0, TileType.OUTPUT)};
      }
      case LONG_OUTPUT_NORTH -> {
        tiles = new Tile[]{
            new Tile(0, 2, TileType.INPUT),
            new Tile(0, 1, TileType.CROSSABLE),
            new Tile(0, 0, TileType.CROSSABLE),
            new Tile(0, -1, TileType.OUTPUT)};
      }
      case LONG_OUTPUT_EAST -> {
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

    return new Conveyor(horPos, verPos, tiles, MovableObjectType.CONVEYER, subType);
  }


  @Override
  public Map<ResourceType, Integer> getResourcesToOutput(
      Map<ResourceType, Integer> storedResources) {
    return storedResources;
  }

  public ConveyerSubType getSubType() {
    return this.subType;
  }

  /**
   * This enum represents the subtypes of {@link Conveyor}.
   *
   * @author Yannick Kraml
   */
  public enum ConveyerSubType {
    SHORT_OUTPUT_EAST,
    SHORT_OUTPUT_SOUTH,
    SHORT_OUTPUT_WEST,
    SHORT_OUTPUT_NORTH,
    LONG_OUTPUT_EAST,
    LONG_OUTPUT_SOUTH,
    LONG_OUTPUT_WEST,
    LONG_OUTPUT_NORTH,
  }
}
