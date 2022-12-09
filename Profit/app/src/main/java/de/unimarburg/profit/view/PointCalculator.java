package de.unimarburg.profit.view;

import java.awt.Point;

public final class PointCalculator {

  public static MyPoint calcWorldToScreen(Environment environment, MyPoint worldPoint) {

    double worldHorOffset = environment.getWorldHorOffset();
    double worldPointHor = worldPoint.getX();
    double zoomHor = environment.getZoomHor();

    double worldVerOffset = environment.getWorldVerOffset();
    double worldPointVer = worldPoint.getY();
    double zoomVer = environment.getZoomVer();

    double screenHorPos = (worldHorOffset + worldPointHor) * zoomHor;
    double screenVerPos = (worldVerOffset + worldPointVer) * zoomVer;

    return new MyPoint(screenHorPos, screenVerPos);
  }

  public static MyPoint calcScreenToWorld(Environment environment, MyPoint screenPoint) {

    double zoomHor = environment.getZoomHor();
    double zoomVer = environment.getZoomVer();

    double worldHorOffset = environment.getWorldHorOffset();
    double worldVerOffset = environment.getWorldVerOffset();

    double worldHorPos = screenPoint.getX() / zoomHor - worldHorOffset;
    double worldVerPos = screenPoint.getY() / zoomVer - worldVerOffset;

    return new MyPoint(worldHorPos, worldVerPos);
  }

  public static MyPoint calcScreenToWorld(Environment environment, Point screenPoint) {
    return calcScreenToWorld(environment, new MyPoint(screenPoint.x, screenPoint.y));
  }

}
