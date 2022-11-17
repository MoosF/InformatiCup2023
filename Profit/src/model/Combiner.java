package model;

import java.util.Map;

/**
 * This class models a {@link Combiner}.
 *
 * @author Yannick Kraml
 */
public class Combiner extends MovableObject {


  /**
   * Constructor of {@link Combiner}.
   *
   * @param xCoord X-Coordinate of the {@link Combiner}.
   * @param yCoord Y-Coordinate of the {@link Combiner}.
   * @param tiles  Tiles, that construct the {@link Combiner}.
   */
  private Combiner(int xCoord, int yCoord, Tile[] tiles) {
    super(xCoord, yCoord, tiles);
  }

  /**
   * Creates an instance of {@link Combiner}.
   *
   * @param xCoord  X-Coordinate of the {@link Combiner}.
   * @param yCoord  Y-Coordinate of the {@link Combiner}.
   * @param subtype Subtype of the {@link Combiner}.
   * @return New instnace of {@link Combiner}.
   */
  public static Combiner createCombiner(int xCoord, int yCoord, CombinerSubtype subtype) {

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

    return new Combiner(xCoord, yCoord, tiles);
  }

  @Override
  public Map<ResourceType, Integer> getResourcesToOutput(
      Map<ResourceType, Integer> storedResources) {
    return storedResources;
  }
}
