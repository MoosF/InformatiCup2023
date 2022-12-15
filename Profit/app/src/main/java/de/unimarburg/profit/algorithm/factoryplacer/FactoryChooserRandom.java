package de.unimarburg.profit.algorithm.factoryplacer;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import java.util.Collection;
import java.util.Optional;

public class FactoryChooserRandom extends FactoryChooser {


  protected FactoryChooserRandom(Field field, Collection<Factory> possibleFactories) {
    super(field, possibleFactories);
  }

  @Override
  public Optional<Factory> chooseFactory() {
    Collection<Factory> possibleFactories = getPossibleFactories();
    return possibleFactories.stream()
        .skip((int) (possibleFactories.size() * Math.random()))
        .findFirst();
  }
}
