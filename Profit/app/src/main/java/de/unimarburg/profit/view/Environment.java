package de.unimarburg.profit.view;

/**
 * This class saves all needed information to convert points between screen space and world space.
 *
 * @author Yannick Kraml
 */
public class Environment {

  protected final int width;
  protected final int height;
  private double mouseWorldHorPos;
  private double mouseWorldVerPos;
  private double worldHorOffset;
  private double worldVerOffset;
  private double zoomHor = 1;
  private double zoomVer = 1;

  public Environment(int width, int height) {
    this.width = width;
    this.height = height;
  }


  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public double getMouseWorldHorPos() {
    return mouseWorldHorPos;
  }

  public void setMouseWorldHorPos(double mouseWorldHorPos) {
    this.mouseWorldHorPos = mouseWorldHorPos;
  }

  public double getMouseWorldVerPos() {
    return mouseWorldVerPos;
  }

  public void setMouseWorldVerPos(double mouseWorldVerPos) {
    this.mouseWorldVerPos = mouseWorldVerPos;
  }

  public double getWorldHorOffset() {
    return worldHorOffset;
  }

  public void setWorldHorOffset(double worldHorOffset) {
    this.worldHorOffset = worldHorOffset;
  }

  public double getWorldVerOffset() {
    return worldVerOffset;
  }

  public void setWorldVerOffset(double worldVerOffset) {
    this.worldVerOffset = worldVerOffset;
  }

  public double getZoomHor() {
    return zoomHor;
  }

  public void setZoomHor(double zoomHor) {
    this.zoomHor = zoomHor;
  }

  public double getZoomVer() {
    return zoomVer;
  }

  public void setZoomVer(double zoomVer) {
    this.zoomVer = zoomVer;
  }

}
