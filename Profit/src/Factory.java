public class Factory extends MovableObject {

    private final Product product;


    private Factory(int xCoord, int yCoord, Tile[] tiles, Product product) {
        super(xCoord, yCoord, tiles);
        this.product = product;
    }

    public static Factory createFactory(int xCoord, int yCoord, Product product) {

        Tile[] tiles = new Tile[]{
                new Tile(-2, -2, TileType.INPUT),
                new Tile(-1, -2, TileType.INPUT),
                new Tile(0, -2, TileType.INPUT),
                new Tile(1, -2, TileType.INPUT),
                new Tile(2, -2, TileType.INPUT),
                new Tile(-2, -1, TileType.INPUT),
                new Tile(-1, -1, TileType.SOLID),
                new Tile(0, -1, TileType.SOLID),
                new Tile(1, -1, TileType.SOLID),
                new Tile(2, -1, TileType.INPUT),
                new Tile(-2, 0, TileType.INPUT),
                new Tile(-1, 0, TileType.SOLID),
                new Tile(0, 0, TileType.SOLID),
                new Tile(1, 0, TileType.SOLID),
                new Tile(2, 0, TileType.INPUT),
                new Tile(-2, 1, TileType.INPUT),
                new Tile(-1, 1, TileType.SOLID),
                new Tile(0, 1, TileType.SOLID),
                new Tile(1, 1, TileType.SOLID),
                new Tile(2, 1, TileType.INPUT),
                new Tile(-2, 2, TileType.INPUT),
                new Tile(-1, 2, TileType.INPUT),
                new Tile(0, 2, TileType.INPUT),
                new Tile(1, 2, TileType.INPUT),
                new Tile(2, 2, TileType.INPUT),
        };

        return new Factory(xCoord, yCoord, tiles, product);
    }
}
