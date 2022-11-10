public class Conveyer extends MovableObject {


    private Conveyer(int xCoord, int yCoord, Tile[] tiles) {
        super(xCoord, yCoord, tiles);
    }

    public static Conveyer createConveyer(int xCoord, int yCoord, ConveyerSubType subType) {

        Tile[] tiles = null;
        switch (subType) {

            case SHORT_NORTH_SOUTH -> {
                tiles = new Tile[]{
                        new Tile(0, -1, TileType.INPUT),
                        new Tile(0, 0, TileType.CROSSABLE),
                        new Tile(0, 1, TileType.OUTPUT)};
            }
            case SHORT_EAST_WEST -> {
                tiles = new Tile[]{
                        new Tile(-1, 0, TileType.INPUT),
                        new Tile(0, 0, TileType.CROSSABLE),
                        new Tile(1, 0, TileType.OUTPUT)};
            }
            case SHORT_SOUTH_NORTH -> {
                tiles = new Tile[]{
                        new Tile(0, 1, TileType.INPUT),
                        new Tile(0, 0, TileType.CROSSABLE),
                        new Tile(0, -1, TileType.OUTPUT)};
            }
            case SHORT_WEST_EAST -> {
                tiles = new Tile[]{
                        new Tile(1, 0, TileType.INPUT),
                        new Tile(0, 0, TileType.CROSSABLE),
                        new Tile(-1, 0, TileType.OUTPUT)};
            }
            case LONG_NORTH_SOUTH -> {
                tiles = new Tile[]{
                        new Tile(0, -1, TileType.INPUT),
                        new Tile(0, 0, TileType.CROSSABLE),
                        new Tile(0, 1, TileType.CROSSABLE),
                        new Tile(0, 2, TileType.OUTPUT)};
            }
            case LONG_EAST_WEST -> {
                tiles = new Tile[]{
                        new Tile(2, 0, TileType.INPUT),
                        new Tile(1, 0, TileType.CROSSABLE),
                        new Tile(0, 0, TileType.CROSSABLE),
                        new Tile(-1, 0, TileType.OUTPUT)};
            }
            case LONG_SOUTH_NORTH -> {
                tiles = new Tile[]{
                        new Tile(0, 2, TileType.INPUT),
                        new Tile(0, 1, TileType.CROSSABLE),
                        new Tile(0, 0, TileType.CROSSABLE),
                        new Tile(0, -1, TileType.OUTPUT)};
            }
            case LONG_WEST_EAST -> {
                tiles = new Tile[]{
                        new Tile(-1, 0, TileType.INPUT),
                        new Tile(0, 0, TileType.CROSSABLE),
                        new Tile(1, 0, TileType.CROSSABLE),
                        new Tile(2, 0, TileType.OUTPUT)};
            }
        }

        return new Conveyer(xCoord, yCoord, tiles);
    }
}
