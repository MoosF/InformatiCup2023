package model;

/**
 * This class models {@link MovableObject}.
 *
 * @author Yannick Kraml
 */
public abstract class MovableObject extends BaseObject {

  private final MovableObjectType type;

  /**
   * Constructor of {@link MovableObject}.
   *
   * @param horPos X-Coordinate of the {@link MovableObject}.
   * @param verPos Y-Coordinate of the {@link MovableObject}.
   * @param tiles  Tiles, that construct the {@link MovableObject}.
   */
  public MovableObject(int horPos, int verPos, Tile[] tiles, MovableObjectType type) {
    super(horPos, verPos, tiles);
    this.type = type;
  }

  public MovableObjectType getType() {
    return this.type;
  }

  /**
   * This enum represents all types of {@link MovableObject}.
   *
   * @author Fabian Moos
   */
  public enum MovableObjectType {
    COMBINER,
    CONVEYER,
    FACTORY,
    MINE,
  }
}
