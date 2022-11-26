package model;

import java.util.Optional;

public class Tile {

  private final int horPos;
  private final int verPos;
  private final TileType type;
  private BaseObject object;

  public Tile(int horPos, int verPos, TileType type) {
    this.horPos = horPos;
    this.verPos = verPos;
    this.type = type;
  }

  /**
   * @return X-Coordinate of the {@link Tile}.
   */
  public int getX() {
    return horPos;
  }

  /**
   * @return Y-Coordinate of the {@link Tile}.
   */
  public int getY() {
    return verPos;
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

  @Override
  public boolean equals(Object obj) {
    if (this.getClass().equals(obj.getClass())) {
      Tile rhs = (Tile) obj;
      boolean result = this.horPos == rhs.horPos;
      result = result && this.verPos == rhs.verPos;
      return result && this.type == rhs.type;
    } else {
      return false;
    }
  }
}
