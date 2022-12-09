package de.unimarburg.profit.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.SwingUtilities;

public class MyMouseAdapter extends MouseAdapter {


  private boolean cameraIsMoving;

  private double originalMousePositionX;
  private double originalMousePositionY;
  private final Environment env;


  public MyMouseAdapter(Environment env) {
    this.env = env;
  }


  @Override
  public void mouseDragged(MouseEvent e) {

    if (cameraIsMoving && SwingUtilities.isRightMouseButton(e)) {

      double newOffsetX =
          env.getWorldHorOffset() + (e.getX() - originalMousePositionX) / env.getZoomHor();
      double newOffsetY =
          env.getWorldVerOffset() + (e.getY() - originalMousePositionY) / env.getZoomVer();

      originalMousePositionX = e.getX();
      originalMousePositionY = e.getY();

      this.env.setWorldHorOffset(newOffsetX);
      this.env.setWorldVerOffset(newOffsetY);
      return;
    }

    MyPoint point = PointCalculator.calcScreenToWorld(env, e.getPoint());
    env.setMouseWorldHorPos(point.getX());
    env.setMouseWorldVerPos(point.getY());

  }


  @Override
  public void mouseMoved(MouseEvent e) {
    MyPoint point = PointCalculator.calcScreenToWorld(env, e.getPoint());
    env.setMouseWorldHorPos(point.getX());
    env.setMouseWorldVerPos(point.getY());
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    MyPoint point = PointCalculator.calcScreenToWorld(env, e.getPoint());
    env.setMouseWorldHorPos(point.getX());
    env.setMouseWorldVerPos(point.getY());
  }

  @Override
  public void mousePressed(MouseEvent e) {
    super.mousePressed(e);
    if (SwingUtilities.isRightMouseButton(e)) {
      this.cameraIsMoving = true;

      this.originalMousePositionX = e.getX();
      this.originalMousePositionY = e.getY();
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    super.mouseReleased(e);
    this.cameraIsMoving = false;
  }


  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    super.mouseWheelMoved(e);

    MyPoint mousePosBefore = PointCalculator.calcScreenToWorld(env, e.getPoint());

    int rotation = e.getWheelRotation();
    if (rotation < 0) {
      env.setZoomHor(env.getZoomHor() * 1.05);
      env.setZoomVer(env.getZoomVer() * 1.05);
    } else if (rotation > 0) {
      env.setZoomHor(env.getZoomHor() * 0.95);
      env.setZoomVer(env.getZoomVer() * 0.95);
    }
    MyPoint mousePosAfter = PointCalculator.calcScreenToWorld(env, e.getPoint());

    double horVector = mousePosBefore.getX() - mousePosAfter.getX();
    double verVector = mousePosBefore.getY() - mousePosAfter.getY();

    env.setWorldHorOffset(env.getWorldHorOffset() - horVector);
    env.setWorldVerOffset(env.getWorldVerOffset() - verVector);

    MyPoint point = PointCalculator.calcScreenToWorld(env, e.getPoint());
    env.setMouseWorldHorPos(point.getX());
    env.setMouseWorldVerPos(point.getY());
  }


}
