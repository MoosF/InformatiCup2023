package de.unimarburg.profit.algorithm.factoryplacer;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;

public class FactoryPlacer {

  public boolean placeFactory(Field field, Factory factory) {
    try {
      field.addBaseObject(factory);
      return true;
    } catch (CouldNotPlaceObjectException e) {
      return false;
    }
  }

  public void placeFactory(Field field, FactoryChooser factoryChooser)
      throws CouldNotPlaceObjectException {
    field.addBaseObject(factoryChooser.chooseFactory().orElseThrow());
  }
}
