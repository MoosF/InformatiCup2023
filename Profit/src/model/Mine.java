package model;

import java.util.Map;
import model.enums.MineSubType;
import model.enums.ResourceType;
import model.enums.TileType;

/**
 * This class models a {@link Mine}.
 *
 * @author Yannick Kraml
 */
public class Mine extends MovableObject {

  private MineSubType subType;

  /**
   * Constructor of {@link Mine}.
   *
   * @param horPos X-Coordinate of the {@link Mine}.
   * @param verPos Y-Coordinate of the {@link Mine}.
   * @param tiles  Tiles, that constructs the {@link Mine}.
   */
  private Mine(int horPos, int verPos, Tile[] tiles, MovableObjectType type, MineSubType subType) {
    super(horPos, verPos, tiles, type);
    this.subType = subType;
  }

  /**
   * Creates a new instance of {@link Mine}.
   *
   * @param horPos X-Coordinate of the {@link Mine}.
   * @param verPos Y-Coordinate of the {@link Mine}.
   * @param type   Subtype of the {@link Mine}.
   * @return New instance of {@link Mine}.
   */
  public static Mine createMine(int horPos, int verPos, MineSubType type) {

    Tile[] tiles = null;
    switch (type) {

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

    return new Mine(horPos, verPos, tiles, MovableObjectType.MINE, type);
  }

  @Override
  public Map<ResourceType, Integer> getResourcesToOutput(
      Map<ResourceType, Integer> storedResources) {
    return storedResources;
  }

  public MineSubType getSubType() {
    return this.subType;
  }

}
