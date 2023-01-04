package de.unimarburg.profit.algorithm.factoryplacer;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

/**
 * This interface is responsible to calculate which {@link Factory} should be placed next on a given
 * {@link Field}.
 *
 * @author Yannick Kraml
 */
public interface FactoryChooser {


  /**
   * Calculates which {@link Factory} should be placed next.
   *
   * @return Optional of {@link Factory}. Empty, if no {@link Factory} could be found.
   */
  Optional<Factory> chooseFactory(Field field, Collection<Factory> possibleFactories);


}
