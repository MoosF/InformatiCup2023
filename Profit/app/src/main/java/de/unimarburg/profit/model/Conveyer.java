package de.unimarburg.profit.model;

import de.unimarburg.profit.model.enums.ConveyorSubType;
import de.unimarburg.profit.model.enums.ResourceType;
import de.unimarburg.profit.model.enums.TileType;
import java.util.Map;

/**
 * This class models a {@link Conveyer}.
 *
 * @author Yannick Kraml
 */
public class Conveyer extends MovableObject {

  private final ConveyorSubType subType;
  private Factory connectedFactory;

  /**
   * Constructor of {@link Conveyer}.
   *
   * @param horPos X-Coordinate of the {@link Conveyer}.
   * @param verPos Y-Coordinate of the {@link Conveyer}.
   * @param tiles  Tiles, that construct the {@link Conveyer}.
   */
  private Conveyer(int horPos, int verPos, Tile[] tiles, MovableObjectType type,
      ConveyorSubType subType) {
    super(horPos, verPos, tiles, type);
    this.subType = subType;
  }

  /**
   * Creates an instance of a {@link Conveyer}.
   *
   * @param horPos  X-Coordinate of the {@link Conveyer}.
   * @param verPos  Y-Coordinate of the {@link Conveyer}.
   * @param subtype Subtype of the {@link Conveyer}.
   * @return New instance of a {@link Conveyer}.
   */
  public static Conveyer createConveyor(int horPos, int verPos, ConveyorSubType subtype) {

    Tile[] tiles = null;
    switch (subtype) {

      case SHORT_OUTPUT_SOUTH -> {
        tiles = new Tile[]{new Tile(0, -1, TileType.INPUT), new Tile(0, 0, TileType.CROSSABLE),
            new Tile(0, 1, TileType.OUTPUT)};
      }
      case SHORT_OUTPUT_EAST -> {
        tiles = new Tile[]{new Tile(-1, 0, TileType.INPUT), new Tile(0, 0, TileType.CROSSABLE),
            new Tile(1, 0, TileType.OUTPUT)};
      }
      case SHORT_OUTPUT_NORTH -> {
        tiles = new Tile[]{new Tile(0, 1, TileType.INPUT), new Tile(0, 0, TileType.CROSSABLE),
            new Tile(0, -1, TileType.OUTPUT)};
      }
      case SHORT_OUTPUT_WEST -> {
        tiles = new Tile[]{new Tile(1, 0, TileType.INPUT), new Tile(0, 0, TileType.CROSSABLE),
            new Tile(-1, 0, TileType.OUTPUT)};
      }
      case LONG_OUTPUT_SOUTH -> {
        tiles = new Tile[]{new Tile(0, -1, TileType.INPUT), new Tile(0, 0, TileType.CROSSABLE),
            new Tile(0, 1, TileType.CROSSABLE), new Tile(0, 2, TileType.OUTPUT)};
      }
      case LONG_OUTPUT_WEST -> {
        tiles = new Tile[]{new Tile(2, 0, TileType.INPUT), new Tile(1, 0, TileType.CROSSABLE),
            new Tile(0, 0, TileType.CROSSABLE), new Tile(-1, 0, TileType.OUTPUT)};
      }
      case LONG_OUTPUT_NORTH -> {
        tiles = new Tile[]{new Tile(0, 2, TileType.INPUT), new Tile(0, 1, TileType.CROSSABLE),
            new Tile(0, 0, TileType.CROSSABLE), new Tile(0, -1, TileType.OUTPUT)};
      }
      case LONG_OUTPUT_EAST -> {
        tiles = new Tile[]{new Tile(-1, 0, TileType.INPUT), new Tile(0, 0, TileType.CROSSABLE),
            new Tile(1, 0, TileType.CROSSABLE), new Tile(2, 0, TileType.OUTPUT)};
      }
      default -> {
        throw new RuntimeException("Unknown branch.");
      }
    }

    return new Conveyer(horPos, verPos, tiles, MovableObjectType.CONVEYER, subtype);
  }

  public Factory getConnectedFactory() {
    return this.connectedFactory;
  }

  @Override
  public Map<ResourceType, Integer> getResourcesToOutput(
      Map<ResourceType, Integer> storedResources) {
    return storedResources;
  }

  public ConveyorSubType getSubType() {
    return this.subType;
  }

  public void setConnectedFactory(Factory factory) {
    this.connectedFactory = factory;
  }
}
