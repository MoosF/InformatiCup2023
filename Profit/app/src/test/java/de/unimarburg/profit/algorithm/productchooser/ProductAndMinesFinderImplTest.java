package de.unimarburg.profit.algorithm.productchooser;

import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.enums.MineSubType;
import de.unimarburg.profit.model.enums.ProductType;
import de.unimarburg.profit.model.enums.ResourceType;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductAndMinesFinderImplTest {

  @Test
  public void test1() {

    ProductAndMinesFinder chooser = new ProductAndMinesFinderImpl();

    Collection<Product> products = new LinkedList<>();
    products.add(
        new Product(10, ProductType.ZERO, Map.of(ResourceType.ZERO, 10, ResourceType.ONE, 10)));
    products.add(
        new Product(10, ProductType.ONE, Map.of(ResourceType.TWO, 10, ResourceType.THREE, 10)));

    Map<Mine, ResourceType> connectableMines = new HashMap<>();
    connectableMines.put(Mine.createMine(10, 10, MineSubType.OUTPUT_NORTH), ResourceType.ZERO);
    connectableMines.put(Mine.createMine(10, 10, MineSubType.OUTPUT_NORTH), ResourceType.ONE);
    connectableMines.put(Mine.createMine(10, 10, MineSubType.OUTPUT_NORTH), ResourceType.TWO);
    connectableMines.put(Mine.createMine(10, 10, MineSubType.OUTPUT_NORTH), ResourceType.THREE);

    List<TypeAndMinesCombination> combs = chooser.findProductMinesCombination(
        connectableMines, products);

    Assertions.assertEquals(2, combs.size());

  }


}