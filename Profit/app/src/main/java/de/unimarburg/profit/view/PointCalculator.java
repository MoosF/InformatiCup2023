package de.unimarburg.profit.view;

/**
 * Helper class for the conversion between world points and screen points. All Methods are static.
 *
 * @author Yannick Kraml
 */
public final class PointCalculator {

  /**
   * Converts a world point to a screen point in the given {@link Environment}.
   *
   * @param environment {@link Environment} in which the conversion takes place.
   * @param worldPoint  World point to be converted.
   * @return Screen point.
   */
  public static DoublePoint calcWorldToScreen(Environment environment, DoublePoint worldPoint) {

    double worldHorOffset = environment.getWorldHorOffset();
    double worldPointHor = worldPoint.getX();
    double zoomHor = environment.getZoomHor();

    double worldVerOffset = environment.getWorldVerOffset();
    double worldPointVer = worldPoint.getY();
    double zoomVer = environment.getZoomVer();

    double screenHorPos = (worldHorOffset + worldPointHor) * zoomHor;
    double screenVerPos = (worldVerOffset + worldPointVer) * zoomVer;

    return new DoublePoint(screenHorPos, screenVerPos);
  }

  /**
   * Converts a screen point to a world point in the given {@link Environment}.
   *
   * @param environment {@link Environment} in which the conversion takes place.
   * @param screenPoint Screen point to be converted.
   * @return World point.
   */
  public static DoublePoint calcScreenToWorld(Environment environment, DoublePoint screenPoint) {

    double zoomHor = environment.getZoomHor();
    double zoomVer = environment.getZoomVer();

    double worldHorOffset = environment.getWorldHorOffset();
    double worldVerOffset = environment.getWorldVerOffset();

    double worldHorPos = screenPoint.getX() / zoomHor - worldHorOffset;
    double worldVerPos = screenPoint.getY() / zoomVer - worldVerOffset;

    return new DoublePoint(worldHorPos, worldVerPos);
  }

  /**
   * Converts a screen point to a world point in the given {@link Environment}. Works with
   * {@link java.awt.Point} instead of {@link DoublePoint}.
   *
   * @param environment {@link Environment} in which the conversion takes place.
   * @param screenPoint Screen point to be converted.
   * @return World point.
   */
  public static DoublePoint calcScreenToWorld(Environment environment, java.awt.Point screenPoint) {
    return calcScreenToWorld(environment, new DoublePoint(screenPoint.x, screenPoint.y));
  }

}
