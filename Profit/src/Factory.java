public class Factory extends MovableObject{

    private final Product product;

    public Factory(int xCoord, int yCoord, Product product) {
        super(xCoord, yCoord);
        this.product = product;
    }
}
