package model;

/**
 * This class models {@link BaseObject}.
 *
 * @author Yevheniia Makara
 */
public abstract class BaseObject {

  private final int xCoord;
  private final int yCoord;
  private final Tile[] tiles;

  /**
   * Constructor of {@link BaseObject}.
   *
   * @param xCoord X-Coordinate of the {@link BaseObject}.
   * @param yCoord Y-Coordinate of the {@link BaseObject}.
   * @param tiles  Tiles, that construct the {@link BaseObject}.
   */
  public BaseObject(int xCoord, int yCoord, Tile[] tiles) {
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.tiles = tiles;
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
    return xCoord;
  }

  /**
   * Returns the vertical position of this {@link BaseObject}.
   *
   * @return Y-Coordinate of the {@link BaseObject}.
   */
  public int getY() {
    return yCoord;
  }

}
