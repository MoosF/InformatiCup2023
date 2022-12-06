package de.uni.marburg.profit.mineplacer;

/**
 * This class represents a single position on a two-dimensional plane. The coordinates are
 * integers.
 *
 * @author Yannick Kraml
 */
public record Position(int horPos, int verPos) {

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Position position = (Position) o;
    return horPos == position.horPos && verPos == position.verPos;
  }

  @Override
  public String toString() {
    return String.format("(%d|%d)", horPos, verPos);
  }
}
