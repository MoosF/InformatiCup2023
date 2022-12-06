package de.uni_marburg.profit.mineplacer;

import java.util.Objects;

public class Position {

  private final int horPos;
  private final int verPos;


  public Position(int horPos, int verPos) {
    this.horPos = horPos;
    this.verPos = verPos;
  }

  public int getHorPos() {
    return horPos;
  }

  public int getVerPos() {
    return verPos;
  }

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
  public int hashCode() {
    return Objects.hash(horPos, verPos);
  }

  @Override
  public String toString() {
    return String.format("(%d|%d)", horPos, verPos);
  }
}
