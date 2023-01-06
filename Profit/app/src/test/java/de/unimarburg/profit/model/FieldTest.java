package de.unimarburg.profit.model;

import static org.junit.jupiter.api.Assertions.assertThrows;

import de.unimarburg.profit.model.enums.CombinerSubType;
import de.unimarburg.profit.model.enums.ConveyorSubType;
import de.unimarburg.profit.model.enums.MineSubType;
import de.unimarburg.profit.model.enums.ProductType;
import de.unimarburg.profit.model.enums.ResourceType;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import de.unimarburg.profit.model.exceptions.CouldNotRemoveObjectException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FieldTest {

  Field field = new Field(100, 100);


  @Test
  public void testAddBaseObject() throws CouldNotPlaceObjectException {
    BaseObject deposit = Deposit.createDeposit(ResourceType.ZERO, 0, 0, 3, 2);
    BaseObject mine = Mine.createMine(4, 0, MineSubType.OUTPUT_EAST);
    field.addBaseObject(deposit);
    Assertions.assertTrue(field.getAllObjects().contains(deposit));
    field.addBaseObject(mine);
    Assertions.assertTrue(field.getAllObjects().contains(mine));

    assertThrows(CouldNotPlaceObjectException.class, () -> field.addBaseObject(
        Conveyer.createConveyor(1, 2, ConveyorSubType.SHORT_OUTPUT_EAST)));
    assertThrows(CouldNotPlaceObjectException.class,
        () -> field.addBaseObject(Mine.createMine(1, 0, MineSubType.OUTPUT_EAST)));
    assertThrows(CouldNotPlaceObjectException.class,
        () -> field.addBaseObject(Mine.createMine(2, 3, MineSubType.OUTPUT_NORTH)));
    assertThrows(CouldNotPlaceObjectException.class,
        () -> field.addBaseObject(Mine.createMine(8, 0, MineSubType.OUTPUT_EAST)));
  }

  @Test
  public void testRemoveBaseObject()
      throws CouldNotPlaceObjectException, CouldNotRemoveObjectException {
    assertThrows(CouldNotRemoveObjectException.class,
        () -> field.removeBaseObject(Mine.createMine(5, 5, MineSubType.OUTPUT_EAST)));

    BaseObject o1 = Mine.createMine(5, 5, MineSubType.OUTPUT_EAST);
    field.addBaseObject(o1);
    field.removeBaseObject(o1);

    Assertions.assertFalse(field.getAllObjects().contains(o1));
  }

  @Test
  public void testGetObjectsByClass() throws CouldNotPlaceObjectException {

    Product product0 = new Product(10, ProductType.ZERO,
        Map.of(ResourceType.ZERO, 3, ResourceType.ONE, 10));
    Product product1 = new Product(24, ProductType.ONE,
        Map.of(ResourceType.TWO, 4, ResourceType.THREE, 6));

    Field field = new Field(40, 20);

    Deposit deposit = Deposit.createDeposit(ResourceType.ZERO, 0, 0, 5, 5);
    Mine mine = Mine.createMine(6, 0, MineSubType.OUTPUT_EAST);
    Mine mine1 = Mine.createMine(6, 2, MineSubType.OUTPUT_EAST);
    Combiner combiner = Combiner.createCombiner(10, 2, CombinerSubType.OUTPUT_EAST);
    Conveyer conveyer = Conveyer.createConveyor(13, 2, ConveyorSubType.SHORT_OUTPUT_EAST);
    Conveyer conveyer1 = Conveyer.createConveyor(14, 4, ConveyorSubType.SHORT_OUTPUT_SOUTH);
    Conveyer conveyer2 = Conveyer.createConveyor(16, 5, ConveyorSubType.SHORT_OUTPUT_EAST);
    field.addBaseObject(deposit);
    field.addBaseObject(mine);
    field.addBaseObject(mine1);
    field.addBaseObject(combiner);
    field.addBaseObject(conveyer);
    field.addBaseObject(conveyer1);
    field.addBaseObject(conveyer2);

    Deposit deposit1 = Deposit.createDeposit(ResourceType.ONE, 35, 0, 5, 5);
    Mine mine2 = Mine.createMine(32, 1, MineSubType.OUTPUT_WEST);
    Mine mine3 = Mine.createMine(32, 3, MineSubType.OUTPUT_WEST);
    Combiner combiner1 = Combiner.createCombiner(29, 2, CombinerSubType.OUTPUT_WEST);
    Conveyer conveyer3 = Conveyer.createConveyor(26, 2, ConveyorSubType.SHORT_OUTPUT_WEST);
    Conveyer conveyer4 = Conveyer.createConveyor(25, 4, ConveyorSubType.SHORT_OUTPUT_SOUTH);
    Conveyer conveyer5 = Conveyer.createConveyor(24, 6, ConveyorSubType.SHORT_OUTPUT_WEST);
    field.addBaseObject(deposit1);
    field.addBaseObject(mine2);
    field.addBaseObject(mine3);
    field.addBaseObject(combiner1);
    field.addBaseObject(conveyer3);
    field.addBaseObject(conveyer4);
    field.addBaseObject(conveyer5);

    Deposit deposit2 = Deposit.createDeposit(ResourceType.TWO, 0, 15, 5, 5);
    Mine mine4 = Mine.createMine(6, 15, MineSubType.OUTPUT_EAST);
    Mine mine5 = Mine.createMine(6, 17, MineSubType.OUTPUT_EAST);
    Combiner combiner2 = Combiner.createCombiner(10, 17, CombinerSubType.OUTPUT_EAST);
    Conveyer conveyer6 = Conveyer.createConveyor(13, 17, ConveyorSubType.SHORT_OUTPUT_EAST);
    Conveyer conveyer7 = Conveyer.createConveyor(14, 15, ConveyorSubType.SHORT_OUTPUT_NORTH);
    Conveyer conveyer8 = Conveyer.createConveyor(16, 14, ConveyorSubType.SHORT_OUTPUT_EAST);
    field.addBaseObject(deposit2);
    field.addBaseObject(mine4);
    field.addBaseObject(mine5);
    field.addBaseObject(combiner2);
    field.addBaseObject(conveyer6);
    field.addBaseObject(conveyer7);
    field.addBaseObject(conveyer8);

    Deposit deposit3 = Deposit.createDeposit(ResourceType.THREE, 35, 15, 5, 5);
    Mine mine6 = Mine.createMine(32, 16, MineSubType.OUTPUT_WEST);
    Mine mine7 = Mine.createMine(32, 18, MineSubType.OUTPUT_WEST);
    Combiner combiner3 = Combiner.createCombiner(29, 17, CombinerSubType.OUTPUT_WEST);
    Conveyer conveyer9 = Conveyer.createConveyor(26, 17, ConveyorSubType.SHORT_OUTPUT_WEST);
    Conveyer conveyer10 = Conveyer.createConveyor(25, 15, ConveyorSubType.SHORT_OUTPUT_NORTH);
    Conveyer conveyer11 = Conveyer.createConveyor(24, 13, ConveyorSubType.SHORT_OUTPUT_WEST);
    field.addBaseObject(deposit3);
    field.addBaseObject(mine6);
    field.addBaseObject(mine7);
    field.addBaseObject(combiner3);
    field.addBaseObject(conveyer9);
    field.addBaseObject(conveyer10);
    field.addBaseObject(conveyer11);

    Factory factory = Factory.createFactoryWithProduct(18, 5, product0);
    Factory factory1 = Factory.createFactoryWithProduct(18, 10, product1);
    field.addBaseObject(factory);
    field.addBaseObject(factory1);

    Assertions.assertTrue(field.getObjectsOfClass(Deposit.class)
        .containsAll(List.of(deposit, deposit1, deposit2, deposit3)));
    Assertions.assertTrue(field.getObjectsOfClass(Mine.class)
        .containsAll(List.of(mine, mine1, mine2, mine3, mine4, mine5, mine6, mine7)));
    Assertions.assertTrue(field.getObjectsOfClass(Conveyer.class).containsAll(
        List.of(conveyer, conveyer1, conveyer2, conveyer3, conveyer4, conveyer5, conveyer6,
            conveyer7, conveyer8, conveyer9, conveyer10, conveyer11)));
    Assertions.assertTrue(field.getObjectsOfClass(Combiner.class)
        .containsAll(List.of(combiner, combiner1, combiner2, combiner3)));
    Assertions.assertTrue(
        field.getObjectsOfClass(Factory.class).containsAll(List.of(factory, factory1)));

    Assertions.assertTrue(field.getAllObjects().containsAll(
        List.of(deposit, deposit1, deposit2, deposit3, mine, mine1, mine2, mine3, mine4, mine5,
            mine6, mine7, conveyer, conveyer1, conveyer2, conveyer3, conveyer4, conveyer5,
            conveyer6, conveyer7, conveyer8, conveyer9, conveyer10, conveyer11, combiner, combiner1,
            combiner2, combiner3, factory, factory1)));

  }

  @Test
  public void testIllegalConveyerPlacements() throws CouldNotPlaceObjectException {

    Field field = new Field(20, 20);

    field.addBaseObject(Conveyer.createConveyor(4, 4, ConveyorSubType.LONG_OUTPUT_EAST));
    field.addBaseObject(Conveyer.createConveyor(4, 5, ConveyorSubType.LONG_OUTPUT_WEST));
    field.addBaseObject(Conveyer.createConveyor(4, 4, ConveyorSubType.LONG_OUTPUT_NORTH));
    field.addBaseObject(Conveyer.createConveyor(5, 4, ConveyorSubType.LONG_OUTPUT_SOUTH));

    field.addBaseObject(Conveyer.createConveyor(4, 1, ConveyorSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyer.createConveyor(5, 1, ConveyorSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyer.createConveyor(7, 3, ConveyorSubType.SHORT_OUTPUT_WEST));
    field.addBaseObject(Conveyer.createConveyor(8, 5, ConveyorSubType.SHORT_OUTPUT_WEST));

    Assertions.assertThrows(CouldNotPlaceObjectException.class, () -> field.addBaseObject(
        Conveyer.createConveyor(4, 1, ConveyorSubType.SHORT_OUTPUT_NORTH)));
    Assertions.assertThrows(CouldNotPlaceObjectException.class, () -> field.addBaseObject(
        Conveyer.createConveyor(7, 3, ConveyorSubType.SHORT_OUTPUT_EAST)));

    Assertions.assertThrows(CouldNotPlaceObjectException.class,
        () -> field.addBaseObject(Conveyer.createConveyor(4, 4, ConveyorSubType.LONG_OUTPUT_EAST)));


  }


  @Test
  public void testIllegalConveyerPlacements2() throws CouldNotPlaceObjectException {

    Field field = new Field(20, 20);

    field.addBaseObject(Conveyer.createConveyor(1,1, ConveyorSubType.SHORT_OUTPUT_NORTH));
    field.addBaseObject(Conveyer.createConveyor(3,1, ConveyorSubType.SHORT_OUTPUT_NORTH));

    Assertions.assertThrows(CouldNotPlaceObjectException.class,
        () -> field.addBaseObject(Conveyer.createConveyor(2,3, ConveyorSubType.SHORT_OUTPUT_NORTH)));

  }

  @Test
  public void testGetMovableObjects() throws CouldNotPlaceObjectException {


    Deposit deposit = Deposit.createDeposit(ResourceType.ZERO,0,0,3,3);
    Obstacle obstacle = Obstacle.createObstacle(3,3,3,3);
    Conveyer conveyer = Conveyer.createConveyor(10,10, ConveyorSubType.SHORT_OUTPUT_EAST);
    Combiner combiner = Combiner.createCombiner(20,10,CombinerSubType.OUTPUT_EAST);
    Mine mine = Mine.createMine(10,20,MineSubType.OUTPUT_EAST);
    Factory factory = Factory.createFactoryWithoutProduct(28,28);

    Field field = new Field(100,100);
    field.addBaseObject(deposit);
    field.addBaseObject(obstacle);
    field.addBaseObject(combiner);
    field.addBaseObject(conveyer);
    field.addBaseObject(mine);
    field.addBaseObject(factory);

    Collection<MovableObject> movableObjects = field.getMovableObjects();

    Assertions.assertEquals(4, movableObjects.size());
    Assertions.assertTrue(movableObjects.contains(conveyer));
    Assertions.assertTrue(movableObjects.contains(combiner));
    Assertions.assertTrue(movableObjects.contains(mine));
    Assertions.assertTrue(movableObjects.contains(factory));

  }
}