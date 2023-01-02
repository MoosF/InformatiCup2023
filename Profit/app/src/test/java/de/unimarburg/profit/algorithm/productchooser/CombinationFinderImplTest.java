package de.unimarburg.profit.algorithm.productchooser;

import de.unimarburg.profit.algorithm.mineplacer.MineWithResources;
import de.unimarburg.profit.algorithm.mineplacer.MinePlaceFinderImpl;
import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.enums.MineSubType;
import de.unimarburg.profit.model.enums.ProductType;
import de.unimarburg.profit.model.enums.ResourceType;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CombinationFinderImplTest {

  @Test
  public void test1() {

    Collection<Product> products = new LinkedList<>();
    products.add(
        new Product(10, ProductType.ZERO, Map.of(ResourceType.ZERO, 1, ResourceType.ONE, 1)));
    products.add(
        new Product(10, ProductType.ONE, Map.of(ResourceType.TWO, 1, ResourceType.THREE, 1)));

    Deposit deposit1 = Deposit.createDeposit(ResourceType.ZERO, 10, 10, 3, 3);
    Deposit deposit2 = Deposit.createDeposit(ResourceType.ONE, 10, 10, 3, 3);
    Deposit deposit3 = Deposit.createDeposit(ResourceType.TWO, 10, 10, 3, 3);
    Deposit deposit4 = Deposit.createDeposit(ResourceType.THREE, 10, 10, 3, 3);

    Map<Mine, Deposit> connectableMines = new HashMap<>();
    connectableMines.put(Mine.createMine(10, 10, MineSubType.OUTPUT_NORTH), deposit1);
    connectableMines.put(Mine.createMine(10, 10, MineSubType.OUTPUT_NORTH), deposit2);
    connectableMines.put(Mine.createMine(10, 10, MineSubType.OUTPUT_NORTH), deposit3);
    connectableMines.put(Mine.createMine(10, 10, MineSubType.OUTPUT_NORTH), deposit4);

    Collection<MineWithResources> mineWithResources = new MinePlaceFinderImpl().calcResourcesPerMine(
        connectableMines);

    Factory factory = Factory.createFactoryWithoutProduct(18,18);

    CombinationFinder chooser = new CombinationFinderImpl();
    Collection<TypeAndMinesCombination> combs = chooser.findCombinations(
        connectableMines.keySet(), mineWithResources, products, factory);

    //There can only be two possibilities. Mine1 and Mine2 with Product1 or Mine3 and Mine4 with Product 2
    Assertions.assertEquals(2, combs.size());

  }


}