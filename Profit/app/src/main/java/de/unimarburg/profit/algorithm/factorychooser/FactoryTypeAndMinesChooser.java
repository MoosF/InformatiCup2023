package de.unimarburg.profit.algorithm.factorychooser;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Product;
import java.util.Collection;
import java.util.Optional;

public abstract class FactoryTypeAndMinesChooser {

  private final Field field;
  private final Factory factory;
  private final Collection<Mine> connectableMines;

  private final Collection<Product> products;


  protected FactoryTypeAndMinesChooser(Field field, Factory factory, Collection<Mine> connectableMines,
      Collection<Product> products) {
    this.field = field;
    this.factory = factory;
    this.connectableMines = connectableMines;
    this.products = products;
  }

  protected abstract Optional<TypeAndMinesCombination> chooseTypeAndMines();


  protected Field getField() {
    return field;
  }

  protected Factory getFactory() {
    return factory;
  }

  protected Collection<Mine> getConnectableMines() {
    return connectableMines;
  }

  protected Collection<Product> getProducts() {
    return products;
  }
}
