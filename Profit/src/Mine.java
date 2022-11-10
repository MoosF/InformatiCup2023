public class Mine extends MovableObject {


    private Mine(int xCoord, int yCoord, Tile[] tiles) {
        super(xCoord, yCoord, tiles);
    }

    public static Mine createMine(int xCoord, int yCoord, MineSubtype mineSubtype) {

        Tile[] tiles = null;
        switch (mineSubtype) {

            case OUTPUT_EAST -> {
                tiles = new Tile[]{
                        new Tile(-1, 1, TileType.INPUT),
                        new Tile(0, 0, TileType.SOLID),
                        new Tile(0, 1, TileType.SOLID),
                        new Tile(1, 1, TileType.SOLID),
                        new Tile(1, 0, TileType.SOLID),
                        new Tile(2, 1, TileType.OUTPUT)
                };
            }
            case OUTPUT_SOUTH -> {
                tiles = new Tile[]{
                        new Tile(0, -1, TileType.INPUT),
                        new Tile(0, 0, TileType.SOLID),
                        new Tile(0, 1, TileType.SOLID),
                        new Tile(1, 1, TileType.SOLID),
                        new Tile(1, 0, TileType.SOLID),
                        new Tile(0, 2, TileType.OUTPUT)
                };
            }
            case OUTPUT_WEST -> {
                tiles = new Tile[]{
                        new Tile(2, 0, TileType.INPUT),
                        new Tile(0, 0, TileType.SOLID),
                        new Tile(0, 1, TileType.SOLID),
                        new Tile(1, 1, TileType.SOLID),
                        new Tile(1, 0, TileType.SOLID),
                        new Tile(-1, 0, TileType.OUTPUT)
                };
            }
            case OUTPUT_NORTH -> {
                tiles = new Tile[]{
                        new Tile(1, 2, TileType.INPUT),
                        new Tile(0, 0, TileType.SOLID),
                        new Tile(0, 1, TileType.SOLID),
                        new Tile(1, 1, TileType.SOLID),
                        new Tile(1, 0, TileType.SOLID),
                        new Tile(1, -1, TileType.OUTPUT)
                };
            }
        }

        return new Mine(xCoord, yCoord, tiles);
    }
}
