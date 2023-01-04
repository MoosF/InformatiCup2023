package de.unimarburg.profit.algorithm.factoryplacing.factory;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import de.unimarburg.profit.model.exceptions.CouldNotRemoveObjectException;


/**
 * Implementation {@link FactoryPlacer}.
 *
 * @author Yevheniia Makara
 */
public class FactoryPlacerImpl implements FactoryPlacer {

  @Override
  public boolean placeFactory(Field field, Factory factory) {
    try {
      field.addBaseObject(factory);
      return true;
    } catch (CouldNotPlaceObjectException e) {
      return false;
    }
  }

  @Override
  public boolean removeFactory(Field field, Factory factory) {
    try {
      field.removeBaseObject(factory);
      return true;
    } catch (CouldNotRemoveObjectException e) {
      return false;
    }
  }
}
