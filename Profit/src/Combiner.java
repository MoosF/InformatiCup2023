public class Combiner extends MovableObject {

    public Combiner(int xCoord, int yCoord, CombinerSubtype combinerSubtype) {
        super(xCoord, yCoord);

        switch (combinerSubtype) {
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
