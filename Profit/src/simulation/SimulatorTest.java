package simulation;

import model.Conveyor;
import model.ConveyorSubType;
import model.CouldNotPlaceObjectException;
import model.Deposit;
import model.Factory;
import model.Field;
import model.Mine;
import model.MineSubtype;
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
    field.addBaseObject(Mine.createMine(5, 0, MineSubtype.OUTPUT_EAST));
    field.addBaseObject(Mine.createMine(5, 2, MineSubtype.OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(8, 2, ConveyorSubType.SHORT_EAST_WEST));
    field.addBaseObject(Conveyor.createConveyor(11, 2, ConveyorSubType.SHORT_EAST_WEST));
    field.addBaseObject(Factory.createFactory(15, 2, product));

    int points = Simulator.getInstance().simulate(field, 10);
    Assertions.assertEquals(points, 360);
  }

}