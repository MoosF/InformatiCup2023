package simulation;

import model.Combiner;
import model.Combiner.CombinerSubType;
import model.Conveyor;
import model.Conveyor.ConveyerSubType;
import model.CouldNotPlaceObjectException;
import model.Deposit;
import model.Factory;
import model.Field;
import model.Mine;
import model.Mine.MineSubType;
import model.Product;
import model.ResourceType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SimulatorTest {


  @Test
  public void test1() throws CouldNotPlaceObjectException {

    Product product = new Product(10);
    product.addNeededRessource(ResourceType.ZERO, 1);

    Field field = new Field(20, 20);

    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 0, 0, 4, 4));
    field.addBaseObject(Mine.createMine(5, 0, MineSubType.OUTPUT_EAST));
    field.addBaseObject(Mine.createMine(5, 2, MineSubType.OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(8, 2, ConveyerSubType.SHORT_EAST_WEST));
    field.addBaseObject(Conveyor.createConveyor(11, 2, ConveyerSubType.SHORT_EAST_WEST));
    field.addBaseObject(Factory.createFactory(15, 2, product));

    int points = Simulator.getInstance().simulate(field, 10);
    Assertions.assertEquals(points, 360);
  }

  @Test
  public void test2() throws CouldNotPlaceObjectException {

    Product product = new Product(10);
    product.addNeededRessource(ResourceType.ZERO, 3);
    product.addNeededRessource(ResourceType.ONE, 5);

    Field field = new Field(40, 20);

    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 2,1,3,3));
    field.addBaseObject(Deposit.createDeposit(ResourceType.ONE,2,16,3,3));
    field.addBaseObject(Mine.createMine(6,2, MineSubType.OUTPUT_EAST));
    field.addBaseObject(Mine.createMine(6,15, MineSubType.OUTPUT_EAST));
    field.addBaseObject(Combiner.createCombiner(10,16, CombinerSubType.OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(9,4,ConveyerSubType.SHORT_NORTH_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(9,7,ConveyerSubType.SHORT_NORTH_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(9,10,ConveyerSubType.SHORT_NORTH_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(9,13,ConveyerSubType.SHORT_NORTH_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(13,16, ConveyerSubType.SHORT_EAST_WEST));
    field.addBaseObject(Conveyor.createConveyor(16,16, ConveyerSubType.SHORT_EAST_WEST));
    field.addBaseObject(Factory.createFactory(20,16,product));

    int points = Simulator.getInstance().simulate(field, 15);
    Assertions.assertEquals(points, 60);
  }

}