package model;

import java.util.HashMap;
import java.util.Map;
import model.enums.ResourceType;

/**
 * This class models {@link BaseObject}.
 *
 * @author Yevheniia Makara
 */
public abstract class BaseObject {

  private final int horPos;
  private final int verPos;
  private final Tile[] tiles;

  /**
   * Constructor of {@link BaseObject}.
   *
   * @param horPos X-Coordinate of the {@link BaseObject}.
   * @param verPos Y-Coordinate of the {@link BaseObject}.
   * @param tiles  Tiles, that construct the {@link BaseObject}.
   */
  public BaseObject(int horPos, int verPos, Tile[] tiles) {
    this.horPos = horPos;
    this.verPos = verPos;
    this.tiles = tiles;
    for (Tile tile : tiles) {
      tile.setObject(this);
    }
  }

  /**
   * Returns all {@link Tile} from this {@link BaseObject}.
   *
   * @return Tiles, that construct the {@link BaseObject}.
   */
  public Tile[] getTiles() {
    return tiles;
  }

  /**
   * Returns the horizontal position of this {@link BaseObject}.
   *
   * @return X-Coordinate of the {@link BaseObject}.
   */
  public int getX() {
    return horPos;
  }

  /**
   * Returns the vertical position of this {@link BaseObject}.
   *
   * @return Y-Coordinate of the {@link BaseObject}.
   */
  public int getY() {
    return verPos;
  }

  public int doWorkForPoints(Map<ResourceType, Integer> storedResources) {
    return 0;
  }

  public Map<ResourceType, Integer> getStartResources() {
    return new HashMap<>();
  }

  public Map<ResourceType, Integer> getResourcesToOutput(
      Map<ResourceType, Integer> storedResources) {
    return new HashMap<>();
  }

}
