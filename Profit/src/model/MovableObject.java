package model;

/**
 * This class models {@link MovableObject}.
 *
 * @author Yannick Kraml
 */
public abstract class MovableObject extends BaseObject {


  /**
   * Constructor of {@link MovableObject}.
   *
   * @param xCoord X-Coordinate of the {@link MovableObject}.
   * @param yCoord Y-Coordinate of the {@link MovableObject}.
   * @param tiles  Tiles, that construct the {@link MovableObject}.
   */
  public MovableObject(int xCoord, int yCoord, Tile[] tiles) {
    super(xCoord, yCoord, tiles);
  }
}
