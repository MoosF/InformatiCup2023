package main;

import java.util.Map;
import model.BaseObject;
import model.Combiner;
import model.Conveyer;
import model.Deposit;
import model.Factory;
import model.Field;
import model.Mine;
import model.Product;
import model.enums.CombinerSubType;
import model.enums.ConveyerSubType;
import model.enums.MineSubType;
import model.enums.ProductType;
import model.enums.ResourceType;
import model.exceptions.CouldNotPlaceObjectException;

public class TestMain {

  public static void main(String[] args) throws CouldNotPlaceObjectException {

    Field field = new Field(100, 100);
    BaseObject deposit = Deposit.createDeposit(ResourceType.ZERO, 0, 0, 3, 2);
    BaseObject mine = Mine.createMine(4, 0, MineSubType.OUTPUT_EAST);
    field.addBaseObject(deposit);
    field.addBaseObject(mine);

    field.addBaseObject(Mine.createMine(8, 0, MineSubType.OUTPUT_EAST));

    field.show();


  }

}
