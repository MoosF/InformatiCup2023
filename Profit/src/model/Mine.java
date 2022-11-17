package model;

import java.util.Map;

/**
 * This class models a {@link Mine}.
 *
 * @author Yannick Kraml
 */
public class Mine extends MovableObject {


  /**
   * Constructor of {@link Mine}.
   *
   * @param xCoord X-Coordinate of the {@link Mine}.
   * @param yCoord Y-Coordinate of the {@link Mine}.
   * @param tiles  Tiles, that constructs the {@link Mine}.
   */
  private Mine(int xCoord, int yCoord, Tile[] tiles) {
    super(xCoord, yCoord, tiles);
  }

  /**
   * Creates a new instance of {@link Mine}.
   *
   * @param xCoord      X-Coordinate of the {@link Mine}.
   * @param yCoord      Y-Coordinate of the {@link Mine}.
   * @param mineSubtype Subtype of the {@link Mine}.
   * @return New instance of {@link Mine}.
   */
  public static Mine createMine(int xCoord, int yCoord, MineSubtype mineSubtype) {

    Tile[] tiles = null;
    switch (mineSubtype) {

      case OUTPUT_EAST -> {
        tiles = new Tile[]{
            new Tile(-1, 1, TileType.MINE_INPUT),
            new Tile(0, 0, TileType.SOLID),
            new Tile(0, 1, TileType.SOLID),
            new Tile(1, 1, TileType.SOLID),
            new Tile(1, 0, TileType.SOLID),
            new Tile(2, 1, TileType.OUTPUT)
        };
      }
      case OUTPUT_SOUTH -> {
        tiles = new Tile[]{
            new Tile(0, -1, TileType.MINE_INPUT),
            new Tile(0, 0, TileType.SOLID),
            new Tile(0, 1, TileType.SOLID),
            new Tile(1, 1, TileType.SOLID),
            new Tile(1, 0, TileType.SOLID),
            new Tile(0, 2, TileType.OUTPUT)
        };
      }
      case OUTPUT_WEST -> {
        tiles = new Tile[]{
            new Tile(2, 0, TileType.MINE_INPUT),
            new Tile(0, 0, TileType.SOLID),
            new Tile(0, 1, TileType.SOLID),
            new Tile(1, 1, TileType.SOLID),
            new Tile(1, 0, TileType.SOLID),
            new Tile(-1, 0, TileType.OUTPUT)
        };
      }
      case OUTPUT_NORTH -> {
        tiles = new Tile[]{
            new Tile(1, 2, TileType.MINE_INPUT),
            new Tile(0, 0, TileType.SOLID),
            new Tile(0, 1, TileType.SOLID),
            new Tile(1, 1, TileType.SOLID),
            new Tile(1, 0, TileType.SOLID),
            new Tile(1, -1, TileType.OUTPUT)
        };
      }
      default -> {
        throw new RuntimeException("Unknown branch.");
      }
    }

    return new Mine(xCoord, yCoord, tiles);
  }

  @Override
  public Map<ResourceType, Integer> getResourcesToOutput(
      Map<ResourceType, Integer> storedResources) {
    return storedResources;
  }
}
