package de.unimarburg.profit.model;

import de.unimarburg.profit.model.enums.ResourceType;
import de.unimarburg.profit.model.enums.TileType;
import de.unimarburg.profit.model.enums.CombinerSubType;
import java.util.Map;

/**
 * This class models a {@link Combiner}.
 *
 * @author Yannick Kraml
 */
public class Combiner extends MovableObject {

  private final CombinerSubType subType;

  /**
   * Constructor of {@link Combiner}.
   *
   * @param horPos X-Coordinate of the {@link Combiner}.
   * @param verPos Y-Coordinate of the {@link Combiner}.
   * @param tiles  Tiles, that construct the {@link Combiner}.
   */
  private Combiner(int horPos, int verPos, Tile[] tiles, MovableObjectType type,
      CombinerSubType subType) {
    super(horPos, verPos, tiles, type);
    this.subType = subType;
  }

  /**
   * Creates an instance of {@link Combiner}.
   *
   * @param horPos  X-Coordinate of the {@link Combiner}.
   * @param verPos  Y-Coordinate of the {@link Combiner}.
   * @param subtype Subtype of the {@link Combiner}.
   * @return New instance of {@link Combiner}.
   */
  public static Combiner createCombiner(int horPos, int verPos, CombinerSubType subtype) {

    Tile[] tiles;
    switch (subtype) {

      case OUTPUT_EAST -> tiles = new Tile[]{
          new Tile(-1, -1, TileType.INPUT),
          new Tile(-1, 0, TileType.INPUT),
          new Tile(-1, 1, TileType.INPUT),
          new Tile(0, -1, TileType.SOLID),
          new Tile(0, 0, TileType.SOLID),
          new Tile(0, 1, TileType.SOLID),
          new Tile(1, 0, TileType.OUTPUT)
      };
      case OUTPUT_SOUTH -> tiles = new Tile[]{
          new Tile(-1, -1, TileType.INPUT),
          new Tile(0, -1, TileType.INPUT),
          new Tile(1, -1, TileType.INPUT),
          new Tile(-1, 0, TileType.SOLID),
          new Tile(0, 0, TileType.SOLID),
          new Tile(1, 0, TileType.SOLID),
          new Tile(0, 1, TileType.OUTPUT)
      };
      case OUTPUT_WEST -> tiles = new Tile[]{
          new Tile(1, -1, TileType.INPUT),
          new Tile(1, 0, TileType.INPUT),
          new Tile(1, 1, TileType.INPUT),
          new Tile(0, -1, TileType.SOLID),
          new Tile(0, 0, TileType.SOLID),
          new Tile(0, 1, TileType.SOLID),
          new Tile(-1, 0, TileType.OUTPUT)
      };
      case OUTPUT_NORTH -> tiles = new Tile[]{
          new Tile(-1, 1, TileType.INPUT),
          new Tile(0, 1, TileType.INPUT),
          new Tile(1, 1, TileType.INPUT),
          new Tile(-1, 0, TileType.SOLID),
          new Tile(0, 0, TileType.SOLID),
          new Tile(1, 0, TileType.SOLID),
          new Tile(0, -1, TileType.OUTPUT)
      };
      default -> throw new RuntimeException("Unknown branch.");
    }

    return new Combiner(horPos, verPos, tiles, MovableObjectType.COMBINER, subtype);
  }

  @Override
  public Map<ResourceType, Integer> getResourcesToOutput(
      Map<ResourceType, Integer> storedResources) {
    return storedResources;
  }

  public CombinerSubType getSubType() {
    return this.subType;
  }

}
