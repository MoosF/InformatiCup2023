package model;

/**
 * This class models {@link FixedObject}.
 *
 * @author Yevheniia Makara
 */
public abstract class FixedObject extends BaseObject {

  private final int height;
  private final int width;

  /**
   * Constructor of {@link FixedObject}.
   *
   * @param horPos X-Coordinate of the {@link FixedObject}
   * @param verPos Y-Coordinate of the {@link FixedObject}
   * @param tiles  Tiles, that construct the {@link FixedObject}
   * @param height height of the {@link FixedObject}
   * @param width  width of the {@link FixedObject}
   */
  protected FixedObject(int horPos, int verPos, Tile[] tiles, int width, int height) {
    super(horPos, verPos, tiles);
    this.height = height;
    this.width = width;
  }

  protected int getHeight() {
    return height;
  }

  protected int getWidth() {
    return width;
  }
}
