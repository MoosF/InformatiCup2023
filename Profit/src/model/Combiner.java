package model;

import java.util.Map;

/**
 * This class models a {@link Combiner}.
 *
 * @author Yannick Kraml
 */
public class Combiner extends MovableObject {

  private CombinerSubType subType;

  /**
   * Constructor of {@link Combiner}.
   *
   * @param xCoord X-Coordinate of the {@link Combiner}.
   * @param yCoord Y-Coordinate of the {@link Combiner}.
   * @param tiles  Tiles, that construct the {@link Combiner}.
   */
  private Combiner(int xCoord, int yCoord, Tile[] tiles, MovableObjectType type,
      CombinerSubType subType) {
    super(xCoord, yCoord, tiles, type);
    this.subType = subType;
  }

  /**
   * Creates an instance of {@link Combiner}.
   *
   * @param xCoord  X-Coordinate of the {@link Combiner}.
   * @param yCoord  Y-Coordinate of the {@link Combiner}.
   * @param subtype Subtype of the {@link Combiner}.
   * @return New instnace of {@link Combiner}.
   */
  public static Combiner createCombiner(int xCoord, int yCoord, CombinerSubType subtype) {

    Tile[] tiles = null;
    switch (subtype) {

      case OUTPUT_EAST -> {
        tiles = new Tile[]{
            new Tile(-1, -1, TileType.INPUT),
            new Tile(-1, 0, TileType.INPUT),
            new Tile(-1, 1, TileType.INPUT),
            new Tile(0, -1, TileType.SOLID),
            new Tile(0, 0, TileType.SOLID),
            new Tile(0, 1, TileType.SOLID),
            new Tile(1, 0, TileType.OUTPUT)
        };
      }
      case OUTPUT_SOUTH -> {
        tiles = new Tile[]{
            new Tile(-1, -1, TileType.INPUT),
            new Tile(0, -1, TileType.INPUT),
            new Tile(1, -1, TileType.INPUT),
            new Tile(-1, 0, TileType.SOLID),
            new Tile(0, 0, TileType.SOLID),
            new Tile(1, 0, TileType.SOLID),
            new Tile(0, 1, TileType.OUTPUT)
        };
      }
      case OUTPUT_WEST -> {
        tiles = new Tile[]{
            new Tile(1, -1, TileType.INPUT),
            new Tile(1, 0, TileType.INPUT),
            new Tile(1, 1, TileType.INPUT),
            new Tile(0, -1, TileType.SOLID),
            new Tile(0, 0, TileType.SOLID),
            new Tile(0, 1, TileType.SOLID),
            new Tile(-1, 0, TileType.OUTPUT)
        };
      }
      case OUTPUT_NORTH -> {
        tiles = new Tile[]{
            new Tile(-1, 1, TileType.INPUT),
            new Tile(0, 1, TileType.INPUT),
            new Tile(1, 1, TileType.INPUT),
            new Tile(-1, 0, TileType.SOLID),
            new Tile(0, 0, TileType.SOLID),
            new Tile(1, 0, TileType.SOLID),
            new Tile(0, -1, TileType.OUTPUT)
        };
      }
      default -> {
        throw new RuntimeException("Unknown branch.");
      }
    }

    return new Combiner(xCoord, yCoord, tiles, MovableObjectType.COMBINER, subtype);
  }

  @Override
  public Map<ResourceType, Integer> getResourcesToOutput(
      Map<ResourceType, Integer> storedResources) {
    return storedResources;
  }

  public CombinerSubType getSubType() {
    return this.subType;
  }

  /**
   * This enum represents the four subtypes of {@link Combiner}.
   *
   * @author Yannick Kraml.
   */
  public enum CombinerSubType {
    OUTPUT_EAST,
    OUTPUT_SOUTH,
    OUTPUT_WEST,
    OUTPUT_NORTH,
  }
}
