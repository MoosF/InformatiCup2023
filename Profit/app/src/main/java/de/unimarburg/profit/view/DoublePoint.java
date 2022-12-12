package de.unimarburg.profit.view;

/**
 * Models a single point in 2D with double precision.
 *
 * @author Yannick Kraml
 */
public class DoublePoint {

  private final double posX;
  private final double posY;

  public DoublePoint(double x, double y) {
    this.posX = x;
    this.posY = y;
  }

  public double getX() {
    return posX;
  }

  public double getY() {
    return posY;
  }
}
