package model;

import java.util.Optional;

public class Tile {

  private final int xCoord;
  private final int yCoord;
  private final TileType type;
  private BaseObject object;

  public Tile(int xCoord, int yCoord, TileType type) {
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.type = type;
  }

  /**
   * @return X-Coordinate of the {@link Tile}.
   */
  public int getX() {
    return xCoord;
  }

  /**
   * @return Y-Coordinate of the {@link Tile}.
   */
  public int getY() {
    return yCoord;
  }

  public TileType getType() {
    return type;
  }

  public void setObject(BaseObject object) {
    this.object = object;
  }

  public Optional<BaseObject> getObject() {
    return Optional.ofNullable(object);
  }
}
