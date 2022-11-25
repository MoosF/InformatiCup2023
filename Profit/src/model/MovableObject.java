package model;

/**
 * This class models {@link MovableObject}.
 *
 * @author Yannick Kraml
 */
public abstract class MovableObject extends BaseObject {

  private MovableObjectType type;

  /**
   * Constructor of {@link MovableObject}.
   *
   * @param xCoord X-Coordinate of the {@link MovableObject}.
   * @param yCoord Y-Coordinate of the {@link MovableObject}.
   * @param tiles  Tiles, that construct the {@link MovableObject}.
   */
  public MovableObject(int xCoord, int yCoord, Tile[] tiles, MovableObjectType type) {
    super(xCoord, yCoord, tiles);
    this.type = type;
  }

  public MovableObjectType getType() {
    return this.type;
  }

  public enum MovableObjectType {
    COMBINER,
    CONVEYER,
    FACTORY,
    MINE,
  }
}
