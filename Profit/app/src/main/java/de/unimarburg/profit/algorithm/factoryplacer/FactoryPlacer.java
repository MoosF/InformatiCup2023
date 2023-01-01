package de.unimarburg.profit.algorithm.factoryplacer;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import de.unimarburg.profit.model.exceptions.CouldNotRemoveObjectException;

public class FactoryPlacer {

  public boolean placeFactory(Field field, Factory factory) {
    try {
      field.addBaseObject(factory);
      return true;
    } catch (CouldNotPlaceObjectException e) {
      return false;
    }
  }

  public void removeFactory(Field field, Factory factory) {
    try {
      field.removeBaseObject(factory);
    } catch (CouldNotRemoveObjectException e) {
      throw new RuntimeException(e); //Should be never thrown
    }
  }
}
