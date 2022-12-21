package de.unimarburg.profit.algorithm.productchooser;

import de.unimarburg.profit.algorithm.mineplacer.MineResourceAmount;
import de.unimarburg.profit.algorithm.mineplacer.MinePlaceFinderImpl;
import de.unimarburg.profit.model.Deposit;
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

    CombinationFinder chooser = new CombinationFinderImpl();

    Collection<Product> products = new LinkedList<>();
    products.add(
        new Product(10, ProductType.ZERO, Map.of(ResourceType.ZERO, 10, ResourceType.ONE, 10)));
    products.add(
        new Product(10, ProductType.ONE, Map.of(ResourceType.TWO, 10, ResourceType.THREE, 10)));

    Deposit deposit1 = Deposit.createDeposit(ResourceType.ZERO, 10, 10, 3, 3);
    Deposit deposit2 = Deposit.createDeposit(ResourceType.ZERO, 10, 10, 3, 3);
    Deposit deposit3 = Deposit.createDeposit(ResourceType.ZERO, 10, 10, 3, 3);
    Deposit deposit4 = Deposit.createDeposit(ResourceType.ZERO, 10, 10, 3, 3);

    Map<Mine, Deposit> connectableMines = new HashMap<>();
    connectableMines.put(Mine.createMine(10, 10, MineSubType.OUTPUT_NORTH), deposit1);
    connectableMines.put(Mine.createMine(10, 10, MineSubType.OUTPUT_NORTH), deposit2);
    connectableMines.put(Mine.createMine(10, 10, MineSubType.OUTPUT_NORTH), deposit3);
    connectableMines.put(Mine.createMine(10, 10, MineSubType.OUTPUT_NORTH), deposit4);

    Collection<MineResourceAmount> mineResourceAmounts = new MinePlaceFinderImpl().calcResourcesPerMine(
        connectableMines);

    Collection<TypeAndMinesCombination> combs = chooser.findProductMinesCombination(
        connectableMines, mineResourceAmounts, products);

    //Assertions.assertEquals(2, combs.size());

  }


}