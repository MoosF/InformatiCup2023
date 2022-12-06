package de.uni.marburg.profit.model.exceptions;

import de.uni.marburg.profit.model.BaseObject;
import de.uni.marburg.profit.model.Field;

/**
 * This Exception will be thrown, if a {@link BaseObject} could not be removed from a
 * {@link Field}.
 *
 * @author Yannick Kraml
 */
public class CouldNotRemoveObjectException extends Exception {

  /**
   * Constructor of {@link CouldNotRemoveObjectException}.
   *
   * @param baseObject {@link BaseObject}, that could not be removed.
   */
  public CouldNotRemoveObjectException(BaseObject baseObject) {
    super("Could not remove object. Object = " + baseObject);
  }
}
