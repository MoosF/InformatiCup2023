package de.unimarburg.profit.algorithm.factoryplacer;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;

public class FactoryPlacer {

  public static void placeFactory(Field field, Factory factory)
      throws CouldNotPlaceObjectException {
    field.addBaseObject(factory);
  }

  public static void placeFactory(Field field, FactoryChooser factoryChooser)
      throws CouldNotPlaceObjectException {
    field.addBaseObject(factoryChooser.chooseFactory().orElseThrow());
  }
}
