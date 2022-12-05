package de.uni_marburg.profit.model.exceptions;

import de.uni_marburg.profit.model.BaseObject;
import de.uni_marburg.profit.model.Field;

/**
 * This Exception will be thrown if a {@link BaseObject} could not be placed on a {@link Field}.
 *
 * @author Yannick Kraml
 */
public class CouldNotPlaceObjectException extends Exception {


  /**
   * Constructor of {@link CouldNotPlaceObjectException}.
   *
   * @param verticalLocation   X-Coordinate of the {@link BaseObject}, that could not be placed.
   * @param horizontalLocation Y-Coordinate of the {@link BaseObject}, that could not be placed.
   */
  public CouldNotPlaceObjectException(int verticalLocation, int horizontalLocation) {
    super(String.format("Could not place BaseObject at (%d|%d).",
        verticalLocation,
        horizontalLocation));
  }
}
