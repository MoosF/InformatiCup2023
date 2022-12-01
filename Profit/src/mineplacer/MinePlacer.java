package mineplacer;

import model.Field;
import model.exceptions.CouldNotPlaceObjectException;
import model.exceptions.CouldNotRemoveObjectException;

public abstract class MinePlacer {

  private final Field field;

  protected MinePlacer(Field field) {
    this.field = field;
  }

  public abstract boolean placeRandomMine() throws CouldNotPlaceObjectException;

  public abstract boolean removeRandomMine() throws CouldNotRemoveObjectException;

  public final void fillWithMines() throws CouldNotPlaceObjectException {
    boolean added = true;
    while (added) {
      added = placeRandomMine();
    }
  }

  protected final Field getField() {
    return field;
  }
}
