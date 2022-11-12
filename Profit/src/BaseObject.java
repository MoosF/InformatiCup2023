/**
 * This class models {@link BaseObject}.
 *
 * @author Yevheniia Makara
 */
public abstract class BaseObject {
    private final int xCoord, yCoord;
    private Tile[] tiles;

    /**
     * @param xCoord X-Coordinate of the {@link BaseObject}.
     * @param yCoord Y-Coordinate of the {@link BaseObject}.
     * @param tiles Tiles, that construct the {@link BaseObject}.
     */
    public BaseObject(int xCoord, int yCoord, Tile[] tiles) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.tiles = tiles;
    }

    /**
     * @param tiles Tiles, that construct the {@link BaseObject}.
     */
    protected void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }

    /**
     * @return Tiles, that construct the {@link BaseObject}.
     */
    public Tile[] getTiles() {
        return tiles;
    }

    /**
     * @return X-Coordinate of the {@link BaseObject}.
     */
    public int getX() {
        return xCoord;
    }

    /**
     * @return Y-Coordinate of the {@link BaseObject}.
     */
    public int getY() {
        return yCoord;
    }
}
