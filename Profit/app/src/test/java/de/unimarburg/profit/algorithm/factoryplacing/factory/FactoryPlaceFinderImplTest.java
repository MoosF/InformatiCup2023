package de.unimarburg.profit.algorithm.factoryplacing.factory;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Obstacle;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.enums.ProductType;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FactoryPlaceFinderImplTest {

  private final FactoryPlaceFinder factoryPlaceFinder = new FactoryPlaceFinderImpl();
  private final Field field = new Field(20, 20);

  @BeforeEach
  public void init() throws CouldNotPlaceObjectException {
    field.addBaseObject(Obstacle.createObstacle(0, 0, 10, 10));
  }

  @Test
  public void testIfFactoriesArePlaceable() {

    Collection<Factory> factories = factoryPlaceFinder.calculatePossibleFactories(field);

    Assertions.assertEquals(156, factories.size());
    factories.forEach(factory -> Assertions.assertTrue(field.baseObjectCanBePlaced(factory)));

  }

}