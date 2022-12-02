package model;

import simulation.Worker;

/**
 * This class models {@link BaseObject}.
 *
 * @author Yevheniia Makara
 */
public abstract class BaseObject implements Worker {

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
  @Override
  public Tile[] getTiles() {
    return tiles;
  }

  /**
   * Returns the horizontal position of this {@link BaseObject}.
   *
   * @return X-Coordinate of the {@link BaseObject}.
   */
  @Override
  public int getX() {
    return horPos;
  }

  /**
   * Returns the vertical position of this {@link BaseObject}.
   *
   * @return Y-Coordinate of the {@link BaseObject}.
   */
  @Override
  public int getY() {
    return verPos;
  }


}
