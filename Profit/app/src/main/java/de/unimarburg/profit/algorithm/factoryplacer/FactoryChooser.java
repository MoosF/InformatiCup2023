package de.unimarburg.profit.algorithm.factoryplacer;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import java.util.Collection;
import java.util.Optional;

public abstract class FactoryChooser {

  private final Field field;
  private final Collection<Factory> possibleFactories;

  protected FactoryChooser(Field field, Collection<Factory> possibleFactories) {
    this.field = field;
    this.possibleFactories = possibleFactories;
  }

  public abstract Optional<Factory> chooseFactory();

  protected Field getField() {
    return field;
  }

  protected Collection<Factory> getPossibleFactories() {
    return possibleFactories;
  }
}
