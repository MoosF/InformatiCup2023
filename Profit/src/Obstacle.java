/**
 * This class models {@link Obstacle}.
 *
 * @author Yevheniia Makara
 */
public class Obstacle extends FixedObject {
    /**
     * @param xCoord X-Coordinate of the {@link Obstacle}.
     * @param yCoord Y-Coordinate of the {@link Obstacle}.
     * @param tiles  Tiles, that construct the {@link Obstacle}.
     * @param height height of the {@link Obstacle}.
     * @param width  width of the {@link Obstacle}.
     */
    public Obstacle(int xCoord, int yCoord, int width, int height, Tile[] tiles) {
        super(xCoord, yCoord, tiles, width, height);

        Tile[] newTiles = new Tile[width * height];
        int x = 0, y = 0; // relative Position (0, 0) ist linke obere Ecke
        for (int i = 0; i < newTiles.length; i++) {

            newTiles[i] = new Tile(x, y, TileType.SOLID);

            if (x == width - 1) {
                x = 0;
                y++;
            } else {
                x++;
            }
        }

        setTiles(newTiles);
    }
}
