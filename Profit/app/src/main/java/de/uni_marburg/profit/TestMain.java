package de.uni_marburg.profit;

import java.util.Map;

import de.uni_marburg.profit.model.*;
import de.uni_marburg.profit.model.enums.*;
import de.uni_marburg.profit.model.exceptions.CouldNotPlaceObjectException;

public class TestMain {

  public static void main(String[] args) throws CouldNotPlaceObjectException {

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

    field.addBaseObject(Factory.createFactory(20, 7, product0));
    field.addBaseObject(Factory.createFactory(20, 12, product1));


    field.show();


  }

}
