package de.unimarburg.profit.algorithm.factorychooser;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.enums.ProductType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class FactoryTypeAndMinesChooserImpl1 extends FactoryTypeAndMinesChooser {

  protected FactoryTypeAndMinesChooserImpl1(Field field, Factory factory,
      Collection<Mine> connectableMines, Collection<Product> products) {
    super(field, factory, connectableMines, products);
  }

  @Override
  protected Optional<TypeAndMinesCombination> chooseTypeAndMines() {

    Field field = getField();
    Factory factory = getFactory();
    Collection<Mine> allMines = getConnectableMines();
    Collection<Product> products = getProducts();



    Product chosenProduct = products.iterator().next();
    ProductType productType = chosenProduct.getType();



    Collection<Mine> minesToConnect = new ArrayList<>();
    TypeAndMinesCombination combination = new TypeAndMinesCombination(productType, minesToConnect);

    return Optional.of(combination);
  }
}
