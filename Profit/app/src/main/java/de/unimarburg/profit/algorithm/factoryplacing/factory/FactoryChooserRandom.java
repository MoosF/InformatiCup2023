package de.unimarburg.profit.algorithm.factoryplacing.factory;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import java.util.Collection;
import java.util.Optional;

/**
 * Implementation of {@link FactoryChooser}. Chooses {@link Factory}s random.
 *
 * @author Yannick Kraml.
 */
public final class FactoryChooserRandom implements FactoryChooser {


  @Override
  public Optional<Factory> chooseFactory(Field field, Collection<Factory> possibleFactories) {
    Optional<Factory> first = possibleFactories.stream()
        .skip((int) (possibleFactories.size() * Math.random()))
        .findFirst();

    first.ifPresent(possibleFactories::remove);

    return first;
  }
}
