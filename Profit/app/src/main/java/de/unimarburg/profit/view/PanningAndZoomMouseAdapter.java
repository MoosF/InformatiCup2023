package de.unimarburg.profit.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.SwingUtilities;

/**
 * This class extends {@link MouseAdapter} and should always be paired with a
 * {@link FieldDrawPanel}. The {@link PanningAndZoomMouseAdapter} enables the panning of zooming of
 * world space with the mouse.
 *
 * @author Yannick Kraml
 */
public class PanningAndZoomMouseAdapter extends MouseAdapter {


  private boolean cameraIsMoving;

  private double originalMousePositionX;
  private double originalMousePositionY;
  private final Environment env;


  public PanningAndZoomMouseAdapter(Environment env) {
    this.env = env;
  }


  @Override
  public void mouseDragged(MouseEvent e) {

    if (cameraIsMoving && SwingUtilities.isRightMouseButton(e)) {

      double newOffsetX =
          env.getWorldHorOffset() + (e.getX() - originalMousePositionX) / env.getZoomHor();
      double newOffsetY =
          env.getWorldVerOffset() + (e.getY() - originalMousePositionY) / env.getZoomVer();

      this.env.setWorldHorOffset(newOffsetX);
      this.env.setWorldVerOffset(newOffsetY);

      originalMousePositionX = e.getX();
      originalMousePositionY = e.getY();

      return;
    }

    DoublePoint point = PointCalculator.calcScreenToWorld(env, e.getPoint());
    env.setMouseWorldHorPos(point.getX());
    env.setMouseWorldVerPos(point.getY());

  }


  @Override
  public void mouseMoved(MouseEvent e) {
    DoublePoint point = PointCalculator.calcScreenToWorld(env, e.getPoint());
    env.setMouseWorldHorPos(point.getX());
    env.setMouseWorldVerPos(point.getY());
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    DoublePoint point = PointCalculator.calcScreenToWorld(env, e.getPoint());
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

    DoublePoint mousePosBefore = PointCalculator.calcScreenToWorld(env, e.getPoint());

    int rotation = e.getWheelRotation();
    if (rotation < 0) {
      env.setZoomHor(env.getZoomHor() * 1.05);
      env.setZoomVer(env.getZoomVer() * 1.05);
    } else if (rotation > 0) {
      env.setZoomHor(env.getZoomHor() * 0.95);
      env.setZoomVer(env.getZoomVer() * 0.95);
    }
    DoublePoint mousePosAfter = PointCalculator.calcScreenToWorld(env, e.getPoint());

    double horVector = mousePosBefore.getX() - mousePosAfter.getX();
    double verVector = mousePosBefore.getY() - mousePosAfter.getY();

    env.setWorldHorOffset(env.getWorldHorOffset() - horVector);
    env.setWorldVerOffset(env.getWorldVerOffset() - verVector);

    DoublePoint point = PointCalculator.calcScreenToWorld(env, e.getPoint());
    env.setMouseWorldHorPos(point.getX());
    env.setMouseWorldVerPos(point.getY());
  }


}
