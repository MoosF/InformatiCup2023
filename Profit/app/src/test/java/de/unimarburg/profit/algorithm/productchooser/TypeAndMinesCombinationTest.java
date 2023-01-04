package de.unimarburg.profit.algorithm.productchooser;

import de.unimarburg.profit.algorithm.factoryplacing.combination.TypeAndMinesCombination;
import de.unimarburg.profit.algorithm.mineplacing.MineWithResources;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.enums.MineSubType;
import de.unimarburg.profit.model.enums.ProductType;
import de.unimarburg.profit.model.enums.ResourceType;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TypeAndMinesCombinationTest {

  private static TypeAndMinesCombination comb1;
  private static TypeAndMinesCombination comb2;
  private static TypeAndMinesCombination comb3;

  @BeforeAll
  public static void init(){

    //Comb 1
    Factory factory1 = Factory.createFactoryWithoutProduct(0,0);
    Product product1 = new Product(10, ProductType.ZERO, Map.of(ResourceType.ZERO, 5, ResourceType.ONE, 10));

    MineWithResources mine11 = new MineWithResources(Mine.createMine(0,0, MineSubType.OUTPUT_NORTH), ResourceType.ZERO, 30);
    MineWithResources mine12 = new MineWithResources(Mine.createMine(0,0, MineSubType.OUTPUT_NORTH), ResourceType.ONE, 40);

    comb1 = new TypeAndMinesCombination(factory1, product1, Set.of(mine11, mine12));

    Assertions.assertEquals(Set.of(mine11, mine12), comb1.getMinesWithResources());
    Assertions.assertEquals(factory1,comb1.getFactory());
    Assertions.assertEquals(product1, comb1.getProduct());

    //Comb 2
    Factory factory2 = Factory.createFactoryWithoutProduct(10,10);
    Product product2 = new Product(10, ProductType.ZERO, Map.of(ResourceType.ZERO, 5, ResourceType.ONE, 10));

    MineWithResources mine21 = new MineWithResources(Mine.createMine(0,10, MineSubType.OUTPUT_NORTH), ResourceType.ZERO, 30);
    MineWithResources mine22 = new MineWithResources(Mine.createMine(10,0, MineSubType.OUTPUT_NORTH), ResourceType.ONE, 40);
    MineWithResources mine23 = new MineWithResources(Mine.createMine(7,6, MineSubType.OUTPUT_NORTH), ResourceType.THREE, 40); //Unnecessary Mine


    comb2 = new TypeAndMinesCombination(factory2, product2, Set.of(mine21, mine22, mine23));

    //Comb 3
    Factory factory3 = Factory.createFactoryWithoutProduct(0,0);
    Product product3 = new Product(10, ProductType.ZERO, Map.of(ResourceType.ZERO, 50, ResourceType.ONE, 10));

    MineWithResources mine31 = new MineWithResources(Mine.createMine(-10,0, MineSubType.OUTPUT_NORTH), ResourceType.ZERO, 30); //Not enough resource Zero
    MineWithResources mine32 = new MineWithResources(Mine.createMine(0,-20, MineSubType.OUTPUT_NORTH), ResourceType.ONE, 40);

    comb3 = new TypeAndMinesCombination(factory3, product3, Set.of(mine31, mine32));

  }

  @Test
  void getValue() {
    Assertions.assertEquals(10 * 4,comb1.getValue());
    Assertions.assertEquals(0,comb2.getValue());
    Assertions.assertEquals(0,comb3.getValue());
  }

  @Test
  void getDistances() {
    Assertions.assertEquals(0, comb1.getDistances());
    Assertions.assertEquals(25, comb2.getDistances());
    Assertions.assertEquals(30, comb3.getDistances());
  }
}