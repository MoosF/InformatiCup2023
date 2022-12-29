package de.unimarburg.profit.algorithm.mineplacer;

/**
 * This record represents a single position on a two-dimensional plane. The coordinates are
 * integers. This class is only used by the {@link MinePlacingProblem}.
 *
 * @author Yannick Kraml
 */
record Position(int horPos, int verPos) {

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
}
