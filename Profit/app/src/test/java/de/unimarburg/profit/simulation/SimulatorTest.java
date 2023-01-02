package de.unimarburg.profit.simulation;

import de.unimarburg.profit.model.Combiner;
import de.unimarburg.profit.model.Conveyer;
import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.enums.CombinerSubType;
import de.unimarburg.profit.model.enums.ConveyerSubType;
import de.unimarburg.profit.model.enums.MineSubType;
import de.unimarburg.profit.model.enums.ProductType;
import de.unimarburg.profit.model.enums.ResourceType;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SimulatorTest {


  @Test
  public void test1() throws CouldNotPlaceObjectException, SimulateException {

    Product product = new Product(10, ProductType.ZERO, Map.of(ResourceType.ZERO, 1));

    Field field = new Field(20, 20);

    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 0, 0, 4, 4));
    field.addBaseObject(Mine.createMine(5, 0, MineSubType.OUTPUT_EAST));
    field.addBaseObject(Mine.createMine(5, 2, MineSubType.OUTPUT_EAST));
    field.addBaseObject(Conveyer.createConveyor(8, 2, ConveyerSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Conveyer.createConveyor(11, 2, ConveyerSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Factory.createFactoryWithProduct(13, 0, product));

    int points = Simulator.getInstance().simulate(field, 10);
    Assertions.assertEquals(points, 360);
  }

  @Test
  public void test2() throws CouldNotPlaceObjectException, SimulateException {

    Product product = new Product(10, ProductType.ZERO,
        Map.of(ResourceType.ZERO, 3, ResourceType.ONE, 5));

    Field field = new Field(40, 20);

    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 2, 1, 3, 3));
    field.addBaseObject(Deposit.createDeposit(ResourceType.ONE, 2, 16, 3, 3));
    field.addBaseObject(Mine.createMine(6, 2, MineSubType.OUTPUT_EAST));
    field.addBaseObject(Mine.createMine(6, 15, MineSubType.OUTPUT_EAST));
    field.addBaseObject(Combiner.createCombiner(10, 16, CombinerSubType.OUTPUT_EAST));
    field.addBaseObject(Conveyer.createConveyor(9, 4, ConveyerSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyer.createConveyor(9, 7, ConveyerSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyer.createConveyor(9, 10, ConveyerSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyer.createConveyor(9, 13, ConveyerSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyer.createConveyor(13, 16, ConveyerSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Conveyer.createConveyor(16, 16, ConveyerSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Factory.createFactoryWithProduct(18, 14, product));

    int points = Simulator.getInstance().simulate(field, 15);
    Assertions.assertEquals(points, 60);
  }

  @Test
  public void test3() throws CouldNotPlaceObjectException, SimulateException {

    Product product0 = new Product(10, ProductType.ZERO,
        Map.of(ResourceType.ZERO, 3, ResourceType.ONE, 10));
    Product product1 = new Product(24, ProductType.ONE,
        Map.of(ResourceType.TWO, 4, ResourceType.THREE, 6));

    Field field = new Field(40, 20);

    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 0, 0, 5, 5));
    field.addBaseObject(Mine.createMine(6, 0, MineSubType.OUTPUT_EAST));
    field.addBaseObject(Mine.createMine(6, 2, MineSubType.OUTPUT_EAST));
    field.addBaseObject(Combiner.createCombiner(10, 2, CombinerSubType.OUTPUT_EAST));
    field.addBaseObject(Conveyer.createConveyor(13, 2, ConveyerSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Conveyer.createConveyor(14, 4, ConveyerSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyer.createConveyor(16, 5, ConveyerSubType.SHORT_OUTPUT_EAST));

    field.addBaseObject(Deposit.createDeposit(ResourceType.ONE, 35, 0, 5, 5));
    field.addBaseObject(Mine.createMine(32, 1, MineSubType.OUTPUT_WEST));
    field.addBaseObject(Mine.createMine(32, 3, MineSubType.OUTPUT_WEST));
    field.addBaseObject(Combiner.createCombiner(29, 2, CombinerSubType.OUTPUT_WEST));
    field.addBaseObject(Conveyer.createConveyor(26, 2, ConveyerSubType.SHORT_OUTPUT_WEST));
    field.addBaseObject(Conveyer.createConveyor(25, 4, ConveyerSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyer.createConveyor(24, 6, ConveyerSubType.SHORT_OUTPUT_WEST));

    field.addBaseObject(Deposit.createDeposit(ResourceType.TWO, 0, 15, 5, 5));
    field.addBaseObject(Mine.createMine(6, 15, MineSubType.OUTPUT_EAST));
    field.addBaseObject(Mine.createMine(6, 17, MineSubType.OUTPUT_EAST));
    field.addBaseObject(Combiner.createCombiner(10, 17, CombinerSubType.OUTPUT_EAST));
    field.addBaseObject(Conveyer.createConveyor(13, 17, ConveyerSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Conveyer.createConveyor(14, 15, ConveyerSubType.SHORT_OUTPUT_NORTH));
    field.addBaseObject(Conveyer.createConveyor(16, 14, ConveyerSubType.SHORT_OUTPUT_EAST));

    field.addBaseObject(Deposit.createDeposit(ResourceType.THREE, 35, 15, 5, 5));
    field.addBaseObject(Mine.createMine(32, 16, MineSubType.OUTPUT_WEST));
    field.addBaseObject(Mine.createMine(32, 18, MineSubType.OUTPUT_WEST));
    field.addBaseObject(Combiner.createCombiner(29, 17, CombinerSubType.OUTPUT_WEST));
    field.addBaseObject(Conveyer.createConveyor(26, 17, ConveyerSubType.SHORT_OUTPUT_WEST));
    field.addBaseObject(Conveyer.createConveyor(25, 15, ConveyerSubType.SHORT_OUTPUT_NORTH));
    field.addBaseObject(Conveyer.createConveyor(24, 13, ConveyerSubType.SHORT_OUTPUT_WEST));

    field.addBaseObject(Factory.createFactoryWithProduct(18, 5, product0));
    field.addBaseObject(Factory.createFactoryWithProduct(18, 10, product1));

    int points = Simulator.getInstance().simulate(field, 16);
    Assertions.assertEquals(points, 300);
  }


  @Test
  public void test4() throws SimulateException, CouldNotPlaceObjectException {

    Product product = new Product(10, ProductType.ZERO, Map.of(ResourceType.ZERO, 3));

    Field field = new Field(25, 25);

    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 0, 11, 3, 3));
    field.addBaseObject(Mine.createMine(4, 11, MineSubType.OUTPUT_EAST));
    field.addBaseObject(Conveyer.createConveyor(8, 12, ConveyerSubType.LONG_OUTPUT_EAST));
    field.addBaseObject(Conveyer.createConveyor(12, 12, ConveyerSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Conveyer.createConveyor(15, 12, ConveyerSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Conveyer.createConveyor(18, 12, ConveyerSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Factory.createFactoryWithProduct(20, 10, product));

    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 11, 0, 3, 3));
    field.addBaseObject(Mine.createMine(12, 4, MineSubType.OUTPUT_SOUTH));
    field.addBaseObject(Conveyer.createConveyor(12, 8, ConveyerSubType.LONG_OUTPUT_SOUTH));
    field.addBaseObject(Conveyer.createConveyor(12, 12, ConveyerSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyer.createConveyor(12, 15, ConveyerSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyer.createConveyor(12, 18, ConveyerSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Factory.createFactoryWithProduct(10, 20, product));

    int points = Simulator.getInstance().simulate(field, 12);
    Assertions.assertEquals(points, 120);

  }

}