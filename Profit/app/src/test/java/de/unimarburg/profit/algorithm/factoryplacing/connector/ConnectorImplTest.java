package de.unimarburg.profit.algorithm.factoryplacing.connector;

import static org.junit.jupiter.api.Assertions.*;

import de.unimarburg.profit.algorithm.factoryplacing.connector.Connector;
import de.unimarburg.profit.algorithm.factoryplacing.connector.ConnectorImpl;
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
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * @author Fabian Moos
 */
class ConnectorImplTest {

  @Test
  void connectorTest1() throws SimulateException, CouldNotPlaceObjectException {
    // Prepare field *******************************************************************************
    var field = new Field(100, 100);
    // Create product ******************************************************************************
    var product = new Product(1, ProductType.ZERO,
        Map.of(ResourceType.ZERO, 1, ResourceType.ONE, 1));
    // Create factory ******************************************************************************
    var factoryToConnect = Factory.createFactoryWithProduct(5, 6, product);
    // Add deposits ********************************************************************************
    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 0, 0, 3, 3));
    field.addBaseObject(Deposit.createDeposit(ResourceType.ONE, 97, 97, 3, 3));
    // Add factory *********************************************************************************
    field.addBaseObject(factoryToConnect);
    // Create connector ****************************************************************************
    var connector = new ConnectorImpl(field);
    // Find reachable mines ************************************************************************
    var reachableMines = connector.getReachableMines(factoryToConnect);
    assertNotNull(reachableMines);
    assertTrue(reachableMines.isEmpty());
    // Try to connect reachable mines (Depends on previous method "getReachableMines") *************
    assertTrue(connector.connectMines(reachableMines));
    var simulator = Simulator.getInstance();
    assertEquals(0, simulator.simulate(field, 60));
  }

  @Test
  void connectorTest2() throws CouldNotPlaceObjectException, SimulateException {
    // Prepare field *******************************************************************************
    var field = new Field(100, 100);
    // Create product ******************************************************************************
    var product = new Product(1, ProductType.ZERO,
        Map.of(ResourceType.ZERO, 1, ResourceType.ONE, 1));
    // Create factory ******************************************************************************
    var factoryToConnect = Factory.createFactoryWithProduct(5, 6, product);
    // Create mines ********************************************************************************
    var mine1 = Mine.createMine(0, 4, MineSubType.OUTPUT_SOUTH);
    var mine2 = Mine.createMine(94, 98, MineSubType.OUTPUT_WEST);
    // Create obstacles ****************************************************************************
    field.addBaseObject(Obstacle.createObstacle(0, 7, 5, 1));
    field.addBaseObject(Obstacle.createObstacle(6, 0, 1, 6));
    // Add objects *********************************************************************************
    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 0, 0, 3, 3));
    field.addBaseObject(Deposit.createDeposit(ResourceType.ONE, 97, 97, 3, 3));
    field.addBaseObject(mine1);
    field.addBaseObject(mine2);
    // Add factory *********************************************************************************
    field.addBaseObject(factoryToConnect);
    // Create connector with field *****************************************************************
    var connector = new ConnectorImpl(field);
    // Find reachableMines *************************************************************************
    var reachableMines = connector.getReachableMines(factoryToConnect);
    assertNotEquals(null, reachableMines);
    assertEquals(2, reachableMines.size());
    assertTrue(reachableMines.contains(mine1));
    assertTrue(reachableMines.contains(mine2));
    // Try to connect reachable mines (Depends on previous method "getReachableMines") *************
    assertTrue(connector.connectMines(reachableMines));
    var simulator = Simulator.getInstance();
    assertEquals(45, simulator.simulate(field, 60));
  }

  @Test
  void connectorTest3() throws CouldNotPlaceObjectException, SimulateException {
    var field = new Field(100, 100);
    var product = new Product(1, ProductType.ZERO,
        Map.of(ResourceType.ZERO, 1, ResourceType.ONE, 1));
    var mine1 = Mine.createMine(0, 4, MineSubType.OUTPUT_SOUTH);
    var mine2 = Mine.createMine(94, 98, MineSubType.OUTPUT_WEST);
    var factoryToConnect = Factory.createFactoryWithProduct(5, 6, product);
    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 0, 0, 3, 3));
    field.addBaseObject(Deposit.createDeposit(ResourceType.ONE, 97, 97, 3, 3));
    field.addBaseObject(factoryToConnect);
    field.addBaseObject(Obstacle.createObstacle(1, 7, 4, 1));
    field.addBaseObject(Obstacle.createObstacle(6, 1, 1, 5));
    field.addBaseObject(mine1);
    field.addBaseObject(mine2);

    // Find reachable mines
    var connector = new ConnectorImpl(field);

    var reachableMines = connector.getReachableMines(factoryToConnect);

    assertNotNull(reachableMines);
    assertEquals(2, reachableMines.size());
    assertTrue(reachableMines.contains(mine1));
    assertTrue(reachableMines.contains(mine2));

    // Try to connect reachable mines (Depends on previous method "getReachableMines")
    var allPlaced = connector.connectMines(reachableMines);
    assertTrue(allPlaced);
    var simulator = Simulator.getInstance();
    assertEquals(45, simulator.simulate(field, 60));
  }

  @Test
  void connectorTest4() throws CouldNotPlaceObjectException, SimulateException {
    var field = new Field(100, 100);
    var product = new Product(1, ProductType.ZERO,
        Map.of(ResourceType.ZERO, 1, ResourceType.ONE, 1));
    var factoryToConnect = Factory.createFactoryWithProduct(5, 6, product);
    var mine1 = Mine.createMine(0, 4, MineSubType.OUTPUT_SOUTH);
    var mine2 = Mine.createMine(94, 98, MineSubType.OUTPUT_WEST);
    field.addBaseObject(mine1);
    field.addBaseObject(mine2);
    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 0, 0, 3, 3));
    field.addBaseObject(Deposit.createDeposit(ResourceType.ONE, 97, 97, 3, 3));
    field.addBaseObject(factoryToConnect);
    field.addBaseObject(Obstacle.createObstacle(0, 27, 13, 3));
    field.addBaseObject(Obstacle.createObstacle(13, 0, 3, 30));
    field.addBaseObject(Obstacle.createObstacle(1, 7, 4, 1));
    field.addBaseObject(Obstacle.createObstacle(6, 1, 1, 5));

    // Find reachable mines
    var connector = new ConnectorImpl(field);
    var reachableMines = connector.getReachableMines(factoryToConnect);

    assertNotNull(reachableMines);
    assertEquals(1, reachableMines.size());
    assertTrue(reachableMines.contains(mine1));
    assertFalse(reachableMines.contains(mine2));

    // Try to connect reachable mines (Depends on previous method "getReachableMines")
    assertTrue(connector.connectMines(reachableMines));
    var simulator = Simulator.getInstance();
    assertEquals(0, simulator.simulate(field, 60));
  }
}
