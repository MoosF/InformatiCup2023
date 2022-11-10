public class Combiner extends MovableObject {


    private Combiner(int xCoord, int yCoord, Tile[] tiles) {
        super(xCoord, yCoord, tiles);
    }

    public static Combiner createCombiner(int xCoord, int yCoord, CombinerSubtype subtype) {

        Tile[] tiles = null;
        switch (subtype) {

            case OUTPUT_EAST -> {
                tiles = new Tile[]{
                        new Tile(-1, -1, TileType.INPUT),
                        new Tile(-1, 0, TileType.INPUT),
                        new Tile(-1, 1, TileType.INPUT),
                        new Tile(0, -1, TileType.SOLID),
                        new Tile(0, 0, TileType.SOLID),
                        new Tile(0, 1, TileType.SOLID),
                        new Tile(1, 0, TileType.OUTPUT)
                };
            }
            case OUTPUT_SOUTH -> {
                tiles = new Tile[]{
                        new Tile(-1, -1, TileType.INPUT),
                        new Tile(0, -1, TileType.INPUT),
                        new Tile(1, -1, TileType.INPUT),
                        new Tile(-1, 0, TileType.SOLID),
                        new Tile(0, 0, TileType.SOLID),
                        new Tile(1, 0, TileType.SOLID),
                        new Tile(0, 1, TileType.OUTPUT)
                };
            }
            case OUTPUT_WEST -> {
                tiles = new Tile[]{
                        new Tile(1, -1, TileType.INPUT),
                        new Tile(1, 0, TileType.INPUT),
                        new Tile(1, 1, TileType.INPUT),
                        new Tile(0, -1, TileType.SOLID),
                        new Tile(0, 0, TileType.SOLID),
                        new Tile(0, 1, TileType.SOLID),
                        new Tile(-1, 0, TileType.OUTPUT)
                };
            }
            case OUTPUT_NORTH -> {
                tiles = new Tile[]{
                        new Tile(-1, 1, TileType.INPUT),
                        new Tile(0, 1, TileType.INPUT),
                        new Tile(1, 1, TileType.INPUT),
                        new Tile(-1, 0, TileType.SOLID),
                        new Tile(0, 0, TileType.SOLID),
                        new Tile(1, 0, TileType.SOLID),
                        new Tile(0, -1, TileType.OUTPUT)
                };
            }
        }

        return new Combiner(xCoord, yCoord, tiles);
    }
}
