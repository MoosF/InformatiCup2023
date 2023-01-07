package de.unimarburg.profit.algorithm.factoryplacing.factory;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Product;
import java.util.Collection;


/**
 * This interface is responsible to find all possible {@link Factory} for a given {@link Field}.
 *
 * @author Yevheniia Makara
 */
public interface FactoryPlaceFinder {

  /**
   * Calculates all possible {@link Factory}s, that could be placed on the given {@link Field}.
   *
   * @return {@link Collection} of {@link Factory}s, which can be placed.
   */
  Collection<Factory> calculatePossibleFactories(Field field);
}
