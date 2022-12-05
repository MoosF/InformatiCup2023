package de.uni_marburg.profit.model;

import java.util.Map;
import de.uni_marburg.profit.model.enums.ConveyerSubType;
import de.uni_marburg.profit.model.enums.ResourceType;
import de.uni_marburg.profit.model.enums.TileType;

/**
 * This class models a {@link Conveyer}.
 *
 * @author Yannick Kraml
 */
public class Conveyer extends MovableObject {

  private final ConveyerSubType subType;

  /**
   * Constructor of {@link Conveyer}.
   *
   * @param horPos X-Coordinate of the {@link Conveyer}.
   * @param verPos Y-Coordinate of the {@link Conveyer}.
   * @param tiles  Tiles, that construct the {@link Conveyer}.
   */
  private Conveyer(int horPos, int verPos, Tile[] tiles, MovableObjectType type,
      ConveyerSubType subType) {
    super(horPos, verPos, tiles, type);
    this.subType = subType;
  }

  /**
   * Creates an instance of a {@link Conveyer}.
   *
   * @param horPos  X-Coordinate of the {@link Conveyer}.
   * @param verPos  Y-Coordinate of the {@link Conveyer}.
   * @param subType Subtype of the {@link Conveyer}.
   * @return New instance of a {@link Conveyer}.
   */
  public static Conveyer createConveyor(int horPos, int verPos, ConveyerSubType subType) {

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

    return new Conveyer(horPos, verPos, tiles, MovableObjectType.CONVEYER, subType);
  }


  @Override
  public Map<ResourceType, Integer> getResourcesToOutput(
      Map<ResourceType, Integer> storedResources) {
    return storedResources;
  }

  public ConveyerSubType getSubType() {
    return this.subType;
  }

}
