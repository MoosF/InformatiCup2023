package de.uni_marburg.profit.model;

import de.uni_marburg.profit.model.enums.TileType;

/**
 * This class models {@link Obstacle}.
 *
 * @author Yevheniia Makara
 */
public class Obstacle extends FixedObject {

  /**
   * Constructor of {@link Obstacle}.
   *
   * @param verPos X-Coordinate of the {@link Obstacle}.
   * @param horPos Y-Coordinate of the {@link Obstacle}.
   * @param tiles  Tiles, that construct the {@link Obstacle}.
   * @param height height of the {@link Obstacle}.
   * @param width  width of the {@link Obstacle}.
   */
  private Obstacle(int verPos, int horPos, int width, int height, Tile[] tiles) {
    super(verPos, horPos, tiles, width, height);
  }

  /**
   * Creates an instance of {@link Obstacle}.
   *
   * @param horPos X-Position of the {@link Obstacle}.
   * @param verPos Y-Position of the {@link Obstacle}.
   * @param width  Width of the {@link Obstacle}.
   * @param height Height of the {@link Obstacle}.
   * @return New instance of {@link Obstacle}.
   */
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
