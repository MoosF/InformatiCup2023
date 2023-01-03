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
import de.unimarburg.profit.service.InputOutputException;
import de.unimarburg.profit.simulation.SimulateException;
import de.unimarburg.profit.simulation.Simulator;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Fabian Moos
 */
class ConnectorImplTest {

  Connector connector = null;
  Field field = null;
  Factory factoryToConnect = null;

  @BeforeEach
  void createBasicTestField() throws CouldNotPlaceObjectException, InputOutputException {
    this.field = new Field(100, 100);
    var requiredResources = new HashMap<ResourceType, Integer>();
    requiredResources.put(ResourceType.ZERO, 1);
    requiredResources.put(ResourceType.ONE, 1);
    requiredResources.put(ResourceType.TWO, 0);
    requiredResources.put(ResourceType.THREE, 0);
    requiredResources.put(ResourceType.FOUR, 0);
    requiredResources.put(ResourceType.FIVE, 0);
    requiredResources.put(ResourceType.SIX, 0);
    requiredResources.put(ResourceType.SEVEN, 0);
    this.factoryToConnect = Factory.createFactoryWithProduct(5, 6,
        new Product(1, ProductType.ZERO, requiredResources));
    this.field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 0, 0, 3, 3));
    this.field.addBaseObject(Deposit.createDeposit(ResourceType.ONE, 97, 97, 3, 3));
    this.field.addBaseObject(factoryToConnect);
    this.connector = new ConnectorImpl(this.field);
  }

  @Test
  void connectorTest1() throws SimulateException {
    // Find reachable mines
    var reachableMines = this.connector.getReachableMines(this.factoryToConnect);
    assertNotEquals(null, reachableMines);
    assertTrue(reachableMines.isEmpty());
    // Try to connect reachable mines (Depends on previous method "getReachableMines")
    assertTrue(this.connector.connectMines(reachableMines));
    var simulator = Simulator.getInstance();
    assertEquals(0, simulator.simulate(this.field, 60));
  }

  @Test
  void connectorTest2() throws CouldNotPlaceObjectException, SimulateException {
    // Find reachable mines
    this.field.addBaseObject(Obstacle.createObstacle(0, 7, 5, 1));
    this.field.addBaseObject(Obstacle.createObstacle(6, 0, 1, 6));
    var mine1 = Mine.createMine(0, 4, MineSubType.OUTPUT_SOUTH);
    var mine2 = Mine.createMine(94, 98, MineSubType.OUTPUT_WEST);
    this.field.addBaseObject(mine1);
    this.field.addBaseObject(mine2);

    var reachableMines = this.connector.getReachableMines(this.factoryToConnect);

    assertNotEquals(null, reachableMines);
    assertEquals(2, reachableMines.size());
    assertTrue(reachableMines.contains(mine1));
    assertTrue(reachableMines.contains(mine2));

    // Try to connect reachable mines (Depends on previous method "getReachableMines")
    assertTrue(this.connector.connectMines(reachableMines));
    var simulator = Simulator.getInstance();
    assertEquals(45, simulator.simulate(this.field, 60), 1);
  }

  @Test
  void connectorTest3() throws CouldNotPlaceObjectException, SimulateException {
    // Find reachable mines
    this.field.addBaseObject(Obstacle.createObstacle(1, 7, 4, 1));
    this.field.addBaseObject(Obstacle.createObstacle(6, 1, 1, 5));
    var mine1 = Mine.createMine(0, 4, MineSubType.OUTPUT_SOUTH);
    var mine2 = Mine.createMine(94, 98, MineSubType.OUTPUT_WEST);
    this.field.addBaseObject(mine1);
    this.field.addBaseObject(mine2);

    var reachableMines = this.connector.getReachableMines(this.factoryToConnect);

    assertNotEquals(null, reachableMines);
    assertEquals(2, reachableMines.size());
    assertTrue(reachableMines.contains(mine1));
    assertTrue(reachableMines.contains(mine2));

    // Try to connect reachable mines (Depends on previous method "getReachableMines")
    assertTrue(this.connector.connectMines(reachableMines));
    var simulator = Simulator.getInstance();
    assertEquals(45, simulator.simulate(this.field, 60), 1);
  }

  @Test
  void connectorTest4() throws CouldNotPlaceObjectException, SimulateException {
    // Find reachable mines
    this.field.addBaseObject(Obstacle.createObstacle(0, 27, 13, 3));
    this.field.addBaseObject(Obstacle.createObstacle(13, 0, 3, 30));
    this.field.addBaseObject(Obstacle.createObstacle(1, 7, 4, 1));
    this.field.addBaseObject(Obstacle.createObstacle(6, 1, 1, 5));
    var mine1 = Mine.createMine(0, 4, MineSubType.OUTPUT_SOUTH);
    var mine2 = Mine.createMine(94, 98, MineSubType.OUTPUT_WEST);
    this.field.addBaseObject(mine1);
    this.field.addBaseObject(mine2);

    var reachableMines = this.connector.getReachableMines(this.factoryToConnect);

    assertNotEquals(null, reachableMines);
    assertEquals(1, reachableMines.size());
    assertTrue(reachableMines.contains(mine1));
    assertFalse(reachableMines.contains(mine2));

    // Try to connect reachable mines (Depends on previous method "getReachableMines")
    assertTrue(this.connector.connectMines(reachableMines));
    var simulator = Simulator.getInstance();
    assertEquals(0, simulator.simulate(this.field, 60));
  }
}
