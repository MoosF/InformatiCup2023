public class Conveyer extends MovableObject {


    public Conveyer(int xCoord, int yCoord, ConveyerSubType subType) {
        super(xCoord, yCoord);


        switch (subType) {

            case SHORT_NORTH_SOUTH -> {
            }
            case SHORT_EAST_WEST -> {
            }
            case SHORT_SOUTH_NORTH -> {
            }
            case SHORT_WEST_EAST -> {
            }
            case LONG_NORTH_SOUTH -> {
            }
            case LONG_EAST_WEST -> {
            }
            case LONG_SOUTH_NORTH -> {
            }
            case LONG_WEST_EAST -> {
            }
        }
    }


}
