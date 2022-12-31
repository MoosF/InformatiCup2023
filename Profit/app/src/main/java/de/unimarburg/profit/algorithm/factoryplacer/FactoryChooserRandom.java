package de.unimarburg.profit.algorithm.factoryplacer;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Implementation of {@link FactoryChooser}. Chooses {@link Factory}s random.
 *
 * @author Yannick Kraml.
 */
public final class FactoryChooserRandom extends FactoryChooser {


  public FactoryChooserRandom(Field field, Collection<Factory> possibleFactories) {
    super(field, possibleFactories);
  }

  @Override
  public Optional<Factory> chooseFactory() {
    Collection<Factory> possibleFactories = getPossibleFactories();
    Optional<Factory> first = possibleFactories.stream()
        .skip((int) (possibleFactories.size() * Math.random()))
        .findFirst();

    first.ifPresent(possibleFactories::remove);

    return first;
  }
}
