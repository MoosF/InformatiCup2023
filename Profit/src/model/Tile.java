package model;

import java.util.Optional;
import model.enums.TileType;

/**
 * This class models a single {@link Tile} of a {@link BaseObject}. Each {@link BaseObject} is
 * constructed of several {@link Tile}s.
 *
 * @author Yannick Kraml
 */
public class Tile {

  private final int relHorPos;
  private final int relVerPos;
  private final TileType type;
  private BaseObject object;

  /**
   * Constrcutor of {@link Tile}.
   *
   * @param relHorPos Relative horizontal position compared to the {@link BaseObject}.
   * @param relVerPos Relative vertical position compared to the {@link BaseObject}.
   * @param type      {@link TileType} of this {@link Tile}.
   */
  public Tile(int relHorPos, int relVerPos, TileType type) {
    this.relHorPos = relHorPos;
    this.relVerPos = relVerPos;
    this.type = type;
  }

  public int getRelHorPos() {
    return relHorPos;
  }

  public int getRelVerPos() {
    return relVerPos;
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
      boolean result = this.relHorPos == rhs.relHorPos;
      result = result && this.relVerPos == rhs.relVerPos;
      return result && this.type == rhs.type;
    } else {
      return false;
    }
  }
}
