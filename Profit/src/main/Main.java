package main;

import model.BaseObject;
import model.Combiner;
import model.CombinerSubtype;
import model.Conveyor;
import model.ConveyorSubType;
import model.CouldNotPlaceObjectException;
import model.CouldNotRemoveObjectException;
import model.Deposit;
import model.Factory;
import model.Field;
import model.Mine;
import model.MineSubtype;
import model.Obstacle;
import model.Product;
import model.ResourceType;

public class Main {

  public static void main(String[] args)
      throws CouldNotPlaceObjectException, CouldNotRemoveObjectException {

    Field field = new Field(20, 30);

    field.addBaseObject(Combiner.createCombiner(10, 10, CombinerSubtype.OUTPUT_NORTH));
    field.addBaseObject(Factory.createFactory(3, 3, new Product(100)));
    field.addBaseObject(Conveyor.createConveyor(7, 4, ConveyorSubType.LONG_EAST_WEST));
    field.addBaseObject(Mine.createMine(13, 12, MineSubtype.OUTPUT_EAST));
    field.addBaseObject(Deposit.createDeposit(ResourceType.FOUR, 12, 0, 6, 6));
    field.addBaseObject(Obstacle.createObstacle(0, 9, 5, 6));

    BaseObject baseObject = Conveyor.createConveyor(17, 17, ConveyorSubType.SHORT_EAST_WEST);
    field.addBaseObject(baseObject);
    field.removeBaseObject(baseObject);

    field.show();

  }

}
