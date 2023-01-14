package de.unimarburg.profit.simulation;

import de.unimarburg.profit.model.Combiner;
import de.unimarburg.profit.model.Conveyor;
import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.enums.CombinerSubType;
import de.unimarburg.profit.model.enums.ConveyorSubType;
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
    field.addBaseObject(Conveyor.createConveyor(8, 2, ConveyorSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(11, 2, ConveyorSubType.SHORT_OUTPUT_EAST));
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
    field.addBaseObject(Conveyor.createConveyor(9, 4, ConveyorSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(9, 7, ConveyorSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(9, 10, ConveyorSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(9, 13, ConveyorSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(13, 16, ConveyorSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(16, 16, ConveyorSubType.SHORT_OUTPUT_EAST));
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
    field.addBaseObject(Conveyor.createConveyor(13, 2, ConveyorSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(14, 4, ConveyorSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(16, 5, ConveyorSubType.SHORT_OUTPUT_EAST));

    field.addBaseObject(Deposit.createDeposit(ResourceType.ONE, 35, 0, 5, 5));
    field.addBaseObject(Mine.createMine(32, 1, MineSubType.OUTPUT_WEST));
    field.addBaseObject(Mine.createMine(32, 3, MineSubType.OUTPUT_WEST));
    field.addBaseObject(Combiner.createCombiner(29, 2, CombinerSubType.OUTPUT_WEST));
    field.addBaseObject(Conveyor.createConveyor(26, 2, ConveyorSubType.SHORT_OUTPUT_WEST));
    field.addBaseObject(Conveyor.createConveyor(25, 4, ConveyorSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(24, 6, ConveyorSubType.SHORT_OUTPUT_WEST));

    field.addBaseObject(Deposit.createDeposit(ResourceType.TWO, 0, 15, 5, 5));
    field.addBaseObject(Mine.createMine(6, 15, MineSubType.OUTPUT_EAST));
    field.addBaseObject(Mine.createMine(6, 17, MineSubType.OUTPUT_EAST));
    field.addBaseObject(Combiner.createCombiner(10, 17, CombinerSubType.OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(13, 17, ConveyorSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(14, 15, ConveyorSubType.SHORT_OUTPUT_NORTH));
    field.addBaseObject(Conveyor.createConveyor(16, 14, ConveyorSubType.SHORT_OUTPUT_EAST));

    field.addBaseObject(Deposit.createDeposit(ResourceType.THREE, 35, 15, 5, 5));
    field.addBaseObject(Mine.createMine(32, 16, MineSubType.OUTPUT_WEST));
    field.addBaseObject(Mine.createMine(32, 18, MineSubType.OUTPUT_WEST));
    field.addBaseObject(Combiner.createCombiner(29, 17, CombinerSubType.OUTPUT_WEST));
    field.addBaseObject(Conveyor.createConveyor(26, 17, ConveyorSubType.SHORT_OUTPUT_WEST));
    field.addBaseObject(Conveyor.createConveyor(25, 15, ConveyorSubType.SHORT_OUTPUT_NORTH));
    field.addBaseObject(Conveyor.createConveyor(24, 13, ConveyorSubType.SHORT_OUTPUT_WEST));

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
    field.addBaseObject(Conveyor.createConveyor(8, 12, ConveyorSubType.LONG_OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(12, 12, ConveyorSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(15, 12, ConveyorSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(18, 12, ConveyorSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Factory.createFactoryWithProduct(20, 10, product));

    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 11, 0, 3, 3));
    field.addBaseObject(Mine.createMine(12, 4, MineSubType.OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(12, 8, ConveyorSubType.LONG_OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(12, 12, ConveyorSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(12, 15, ConveyorSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(12, 18, ConveyorSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Factory.createFactoryWithProduct(10, 20, product));

    int points = Simulator.getInstance().simulate(field, 12);
    Assertions.assertEquals(points, 120);

  }


  @Test
  public void test5() throws SimulateException, CouldNotPlaceObjectException {

    Field field = new Field(30,20);

    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO,1,1,5,5));
    field.addBaseObject(Deposit.createDeposit(ResourceType.ONE, 1,14,5,5));
    field.addBaseObject(Deposit.createDeposit(ResourceType.TWO, 22,1,7,7));

    field.addBaseObject(Mine.createMine(19,2,MineSubType.OUTPUT_WEST));
    field.addBaseObject(Mine.createMine(7,3,MineSubType.OUTPUT_EAST));
    field.addBaseObject(Mine.createMine(6,12,MineSubType.OUTPUT_EAST));
    field.addBaseObject(Mine.createMine(7,16,MineSubType.OUTPUT_EAST));

    field.addBaseObject(Factory.createFactoryWithProduct(18,15, new Product(10, ProductType.ZERO, Map.of(ResourceType.ZERO,3,ResourceType.ONE,3,ResourceType.TWO,3))));

    field.addBaseObject(Conveyor.createConveyor(17,3, ConveyorSubType.SHORT_OUTPUT_WEST));
    field.addBaseObject(Conveyor.createConveyor(15,1, ConveyorSubType.LONG_OUTPUT_NORTH));
    field.addBaseObject(Conveyor.createConveyor(12,0, ConveyorSubType.LONG_OUTPUT_WEST));
    field.addBaseObject(Conveyor.createConveyor(10,1, ConveyorSubType.LONG_OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(10,5, ConveyorSubType.LONG_OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(10,9, ConveyorSubType.LONG_OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(12,11, ConveyorSubType.LONG_OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(16,11, ConveyorSubType.LONG_OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(19,12, ConveyorSubType.LONG_OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(10,13, ConveyorSubType.LONG_OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(14,13, ConveyorSubType.LONG_OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(11,17, ConveyorSubType.LONG_OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(17,14, ConveyorSubType.LONG_OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(15,17, ConveyorSubType.LONG_OUTPUT_EAST));

    Assertions.assertEquals(390, Simulator.getInstance().simulate(field, 50));

  }

}