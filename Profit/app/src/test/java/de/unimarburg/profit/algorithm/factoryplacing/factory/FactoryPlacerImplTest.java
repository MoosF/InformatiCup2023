package de.unimarburg.profit.algorithm.factoryplacing.factory;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Obstacle;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FactoryPlacerImplTest {

  private final FactoryPlacer factoryPlacer = new FactoryPlacerImpl();
  private Field field;

  @BeforeEach
  public void init(){
    field = new Field(10,10);
  }

  @Test
  public void testPositive(){
    Factory factory = Factory.createFactoryWithoutProduct(0,0);
    Assertions.assertTrue(factoryPlacer.placeFactory(field,factory));
    Assertions.assertTrue(factoryPlacer.removeFactory(field, factory));
  }

  @Test
  public void testNegative() throws CouldNotPlaceObjectException {
    field.addBaseObject(Obstacle.createObstacle(0,0,10,10));
    Factory factory = Factory.createFactoryWithoutProduct(0,0);
    Assertions.assertFalse(factoryPlacer.placeFactory(field, factory));
    Assertions.assertFalse(factoryPlacer.removeFactory(field,factory));
  }

}