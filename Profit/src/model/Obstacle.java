package model;

/**
 * This class models {@link Obstacle}.
 *
 * @author Yevheniia Makara
 */
public class Obstacle extends FixedObject {

  /**
   * @param verPos X-Coordinate of the {@link Obstacle}.
   * @param horPos Y-Coordinate of the {@link Obstacle}.
   * @param tiles  Tiles, that construct the {@link Obstacle}.
   * @param height height of the {@link Obstacle}.
   * @param width  width of the {@link Obstacle}.
   */
  private Obstacle(int verPos, int horPos, int width, int height, Tile[] tiles) {
    super(verPos, horPos, tiles, width, height);
  }

  public static Obstacle createObstacle(int horPos, int verPos, int width, int height) {
    Tile[] newTiles = new Tile[width * height];
    int x = 0; // relative Position (0, 0) ist linke obere Ecke
    int y = 0;
    for (int i = 0; i < newTiles.length; i++) {

      newTiles[i] = new Tile(x, y, TileType.SOLID);

      if (x == width - 1) {
        x = 0;
        y++;
      } else {
        x++;
      }
    }

    return new Obstacle(horPos, verPos, width, height, newTiles);
  }
}
