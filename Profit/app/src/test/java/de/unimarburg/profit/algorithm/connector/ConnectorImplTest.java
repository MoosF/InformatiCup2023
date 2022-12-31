package de.unimarburg.profit.algorithm.connector;

import static org.junit.jupiter.api.Assertions.*;

import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Obstacle;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.enums.MineSubType;
import de.unimarburg.profit.model.enums.ProductType;
import de.unimarburg.profit.model.enums.ResourceType;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import de.unimarburg.profit.simulation.SimulateException;
import de.unimarburg.profit.simulation.Simulator;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConnectorImplTest {

  Connector connector = null;
  Field field = null;
  Factory factoryToConnect = null;

  @BeforeEach
  void createBasicTestField() throws CouldNotPlaceObjectException {
    this.field = new Field(30, 30);
    var requiredResources = new HashMap<ResourceType, Integer>();
    requiredResources.put(ResourceType.ZERO, 1);
    requiredResources.put(ResourceType.ONE, 1);
    requiredResources.put(ResourceType.TWO, 0);
    requiredResources.put(ResourceType.THREE, 0);
    requiredResources.put(ResourceType.FOUR, 0);
    requiredResources.put(ResourceType.FIVE, 0);
    requiredResources.put(ResourceType.SIX, 0);
    requiredResources.put(ResourceType.SEVEN, 0);
    this.factoryToConnect = Factory.createFactoryWithProduct(10, 10,
        new Product(1, ProductType.ZERO, requiredResources));
    this.field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 0, 1,
        3, 3));
    this.field.addBaseObject(Deposit.createDeposit(ResourceType.ONE, 27, 26, 3,
        3));
    this.field.addBaseObject(this.factoryToConnect);
    this.connector = new ConnectorImpl(this.field);
  }

  @Test
  void connectMinesTest1() throws CouldNotPlaceObjectException, SimulateException {
    assertFalse(this.connector.connectMines(this.factoryToConnect, new ArrayList<>()));

    var simulator = Simulator.getInstance();
    assertEquals(simulator.simulate(this.field, 23), 0);
  }

  @Test
  void connectMinesTest2() throws CouldNotPlaceObjectException, SimulateException {
    this.field.addBaseObject(Obstacle.createObstacle(0, 10, 8, 1));
    this.field.addBaseObject(Obstacle.createObstacle(10, 0, 1, 8));
    var mine1 = Mine.createMine(0, 5, MineSubType.OUTPUT_SOUTH);
    var mine2 = Mine.createMine(25, 25, MineSubType.OUTPUT_WEST);
    this.field.addBaseObject(mine1);
    this.field.addBaseObject(mine2);

    var minesToConnect = new ArrayList<Mine>();
    minesToConnect.add(mine1);
    minesToConnect.add(mine2);

    assertTrue(this.connector.connectMines(this.factoryToConnect, minesToConnect));

    var simulator = Simulator.getInstance();
    // 23 turns is the optimal value for the setup, it can't get any better.
    assertEquals(simulator.simulate(this.field, 23), 45);
  }

  @Test
  void connectMinesTest3() throws CouldNotPlaceObjectException, SimulateException {
    this.field.addBaseObject(Obstacle.createObstacle(2, 10, 6, 1));
    this.field.addBaseObject(Obstacle.createObstacle(10, 2, 1, 6));
    var mine1 = Mine.createMine(0, 5, MineSubType.OUTPUT_SOUTH);
    var mine2 = Mine.createMine(25, 25, MineSubType.OUTPUT_WEST);
    this.field.addBaseObject(mine1);
    this.field.addBaseObject(mine2);

    var minesToConnect = new ArrayList<Mine>();
    minesToConnect.add(mine1);
    minesToConnect.add(mine2);

    assertTrue(this.connector.connectMines(this.factoryToConnect, minesToConnect));

    var simulator = Simulator.getInstance();
    assertEquals(simulator.simulate(this.field, 22), 42);
    // 23 turns is the optimal value for the setup, it can't get any better.
    assertEquals(simulator.simulate(this.field, 23), 45);
  }

  @Test
  void connectMinesTest4() throws CouldNotPlaceObjectException, SimulateException {
    this.field.addBaseObject(Obstacle.createObstacle(2, 10, 6, 1));
    this.field.addBaseObject(Obstacle.createObstacle(10, 2, 1, 6));
    this.field.addBaseObject(Obstacle.createObstacle(4, 0, 1, 9));
    this.field.addBaseObject(Obstacle.createObstacle(0, 8, 4, 2));
    this.field.addBaseObject(Obstacle.createObstacle(13, 0, 3, 30));
    var mine1 = Mine.createMine(0, 5, MineSubType.OUTPUT_SOUTH);
    var mine2 = Mine.createMine(25, 25, MineSubType.OUTPUT_WEST);
    this.field.addBaseObject(mine1);
    this.field.addBaseObject(mine2);

    var minesToConnect = new ArrayList<Mine>();
    minesToConnect.add(mine1);
    minesToConnect.add(mine2);

    assertFalse(this.connector.connectMines(this.factoryToConnect, minesToConnect));

    var simulator = Simulator.getInstance();
    assertEquals(simulator.simulate(this.field, 23), 0);
  }

  @Test
  void getReachableMinesTest1() throws CouldNotPlaceObjectException {
    var connectableMines = this.connector.getReachableMines(this.factoryToConnect);
    assertNotEquals(connectableMines, null);
    assertTrue(connectableMines.isEmpty());
  }

  @Test
  void getReachableMinesTest2() throws CouldNotPlaceObjectException {
    this.field.addBaseObject(Obstacle.createObstacle(0, 10, 8, 1));
    this.field.addBaseObject(Obstacle.createObstacle(10, 0, 1, 8));
    var mine1 = Mine.createMine(0, 5, MineSubType.OUTPUT_SOUTH);
    var mine2 = Mine.createMine(25, 25, MineSubType.OUTPUT_WEST);
    this.field.addBaseObject(mine1);
    this.field.addBaseObject(mine2);

    var connectableMines = this.connector.getReachableMines(this.factoryToConnect);

    assertNotEquals(connectableMines, null);
    assertEquals(connectableMines.size(), 2);
    assertTrue(connectableMines.contains(mine1));
    assertTrue(connectableMines.contains(mine2));
  }

  @Test
  void getReachableMinesTest3() throws CouldNotPlaceObjectException {
    this.field.addBaseObject(Obstacle.createObstacle(2, 10, 6, 1));
    this.field.addBaseObject(Obstacle.createObstacle(10, 2, 1, 6));
    var mine1 = Mine.createMine(0, 5, MineSubType.OUTPUT_SOUTH);
    var mine2 = Mine.createMine(25, 25, MineSubType.OUTPUT_WEST);
    this.field.addBaseObject(mine1);
    this.field.addBaseObject(mine2);

    var connectableMines = this.connector.getReachableMines(this.factoryToConnect);

    assertNotEquals(connectableMines, null);
    assertEquals(connectableMines.size(), 2);
    assertTrue(connectableMines.contains(mine1));
    assertTrue(connectableMines.contains(mine2));
  }

  @Test
  void getReachableMinesTest4() throws CouldNotPlaceObjectException {
    this.field.addBaseObject(Obstacle.createObstacle(2, 10, 6, 1));
    this.field.addBaseObject(Obstacle.createObstacle(10, 2, 1, 6));
    this.field.addBaseObject(Obstacle.createObstacle(4, 0, 1, 9));
    this.field.addBaseObject(Obstacle.createObstacle(0, 8, 4, 2));
    this.field.addBaseObject(Obstacle.createObstacle(13, 0, 3, 30));
    var mine1 = Mine.createMine(0, 5, MineSubType.OUTPUT_SOUTH);
    var mine2 = Mine.createMine(25, 25, MineSubType.OUTPUT_WEST);
    this.field.addBaseObject(mine1);
    this.field.addBaseObject(mine2);

    var connectableMines = this.connector.getReachableMines(this.factoryToConnect);

    assertNotEquals(connectableMines, null);
    assertTrue(connectableMines.isEmpty());
  }
}
