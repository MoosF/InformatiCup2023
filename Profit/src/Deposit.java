/**
 * This class models {@link Deposit}.
 *
 * @author Yevheniia Makara
 */
public class Deposit extends FixedObject {
    private ResourceType resourceType;

    /**
     * @param xCoord X-Coordinate of the {@link Deposit}.
     * @param yCoord Y-Coordinate of the {@link Deposit}.
     * @param tiles  Tiles, that construct the {@link Deposit}.
     * @param height height of the {@link Deposit}.
     * @param width  width of the {@link Deposit}.
     * @param subtype is the type of resources of the {@link Deposit}.
     */
    public Deposit(ResourceType subtype, int xCoord, int yCoord, int width, int height, Tile[] tiles) {
        super(xCoord, yCoord, tiles, width, height);
        resourceType = subtype;

        Tile[] newTiles = new Tile[width * height];
        int x = 0, y = 0; // relative Position (0, 0) ist linke obere Ecke
        for (int i = 0; i < newTiles.length; i++) {

            if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                newTiles[i] = new Tile(x, y, TileType.OUTPUT);
            } else {
                newTiles[i] = new Tile(x, y, TileType.SOLID);
            }

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
