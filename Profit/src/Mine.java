public class Mine extends MovableObject {

    public Mine(int xCoord, int yCoord, MineSubtype subtype) {
        super(xCoord, yCoord);

        switch (subtype) {
            case OUTPUT_NORTH -> {
            }
            case OUTPUT_EAST -> {
            }
            case OUTPUT_SOUTH -> {
            }
            case OUTPUT_WEST -> {
            }
        }
    }
}
