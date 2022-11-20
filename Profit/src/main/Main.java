package main;

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
import simulation.SimulatableObject;
import simulation.SimulateException;
import simulation.Simulator;

public class Main {

  public static void main(String[] args) throws CouldNotPlaceObjectException, SimulateException {

    Product product = new Product(10);
    product.addNeededRessource(ResourceType.ZERO, 3);

    Field field = new Field(25, 25);

    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 0, 11, 3, 3));
    field.addBaseObject(Mine.createMine(4, 12, MineSubType.OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(8, 12, ConveyerSubType.LONG_OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(12, 12, ConveyerSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(15, 12, ConveyerSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Conveyor.createConveyor(18, 12, ConveyerSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Factory.createFactory(22, 12, product));

    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 11, 0, 3, 3));
    field.addBaseObject(Mine.createMine(12, 4, MineSubType.OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(12, 8, ConveyerSubType.LONG_OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(12, 12, ConveyerSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(12, 15, ConveyerSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyor.createConveyor(12, 18, ConveyerSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Factory.createFactory(12, 22, product));

    field.show();


  }

}
