public class BaseObject {
    private final int xCoord, yCoord;
    private final Tile[] tiles;

    public BaseObject(int xCoord, int yCoord, Tile[] tiles) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.tiles = tiles;
    }
}
