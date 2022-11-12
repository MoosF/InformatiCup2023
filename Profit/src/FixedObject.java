/**
 * This class models {@link FixedObject}.
 *
 * @author Yevheniia Makara
 */
public abstract class FixedObject extends BaseObject {
    private final int height, width;

    /**
     * @param xCoord X-Coordinate of the {@link FixedObject}
     * @param yCoord Y-Coordinate of the {@link FixedObject}
     * @param tiles Tiles, that construct the {@link FixedObject}
     * @param height height of the {@link FixedObject}
     * @param width width of the {@link FixedObject}
     */
    public FixedObject(int xCoord, int yCoord, Tile[] tiles, int width, int height) {
        super(xCoord, yCoord, tiles);
        this.height = height;
        this.width = width;
    }
}
