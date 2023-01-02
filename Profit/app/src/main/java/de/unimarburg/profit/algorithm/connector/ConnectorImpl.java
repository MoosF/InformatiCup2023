package de.unimarburg.profit.algorithm.connector;

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
import de.unimarburg.profit.service.InputOutputHandle;
import de.unimarburg.profit.service.Settings;
import de.unimarburg.profit.simulation.SimulateException;
import de.unimarburg.profit.simulation.Simulator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

/**
 * This class supplies functionality for finding reachable {@link Mine}s for a {@link Factory} and
 * for connecting {@link Mine}s to a {@link Factory}.
 *
 * @author Fabian Moos
 */
public class ConnectorImpl implements Connector {
  public static void main(String[] args) throws InputOutputException, CouldNotPlaceObjectException {
    var field = new Field(100, 100);
    var requiredResources = new HashMap<ResourceType, Integer>();
    requiredResources.put(ResourceType.ZERO, 1);
    requiredResources.put(ResourceType.ONE, 1);
    requiredResources.put(ResourceType.TWO, 0);
    requiredResources.put(ResourceType.THREE, 0);
    requiredResources.put(ResourceType.FOUR, 0);
    requiredResources.put(ResourceType.FIVE, 0);
    requiredResources.put(ResourceType.SIX, 0);
    requiredResources.put(ResourceType.SEVEN, 0);
    var factoryToConnect = Factory.createFactoryWithProduct(5, 6,
        new Product(1, ProductType.ZERO, requiredResources));
    System.out.println(factoryToConnect.getX());
    System.out.println(factoryToConnect.getY());
    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 0, 0, 3, 3));
    field.addBaseObject(Deposit.createDeposit(ResourceType.ONE, 97, 97, 3, 3));
    var mine1 = Mine.createMine(0, 4, MineSubType.OUTPUT_SOUTH);
    var mine2 = Mine.createMine(94, 98, MineSubType.OUTPUT_WEST);
    field.addBaseObject(mine1);
    field.addBaseObject(mine2);
//    field.addBaseObject(Obstacle.createObstacle(1, 7, 4, 1));
//    field.addBaseObject(Obstacle.createObstacle(6, 1, 1, 5));
    field.addBaseObject(Obstacle.createObstacle(0, 7, 5, 1));
    field.addBaseObject(Obstacle.createObstacle(6, 0, 1, 6));
    field.addBaseObject(Obstacle.createObstacle(0, 27, 13, 3));
    field.addBaseObject(Obstacle.createObstacle(13, 0, 3, 30));
    field.addBaseObject(factoryToConnect);
    var connector = new ConnectorImpl(field);
    field.show();
  }

//    void connectMinesTest1() throws CouldNotPlaceObjectException, SimulateException {
//      assertFalse(this.connector.connectMines(this.factoryToConnect, new ArrayList<>()));
//
//      var simulator = Simulator.getInstance();
//      assertEquals(simulator.simulate(this.field, 23), 0);
//    }
//
//    @Test
//    void connectMinesTest2() throws CouldNotPlaceObjectException, SimulateException {
//      this.field.addBaseObject(Obstacle.createObstacle(0, 10, 8, 1));
//      this.field.addBaseObject(Obstacle.createObstacle(10, 0, 1, 8));
//      var mine1 = Mine.createMine(0, 5, MineSubType.OUTPUT_SOUTH);
//      var mine2 = Mine.createMine(25, 25, MineSubType.OUTPUT_WEST);
//      this.field.addBaseObject(mine1);
//      this.field.addBaseObject(mine2);
//
//      var minesToConnect = new ArrayList<Mine>();
//      minesToConnect.add(mine1);
//      minesToConnect.add(mine2);
//
//      assertTrue(this.connector.connectMines(this.factoryToConnect, minesToConnect));
//
//      var simulator = Simulator.getInstance();
//      // 23 turns is the optimal value for the setup, it can't get any better.
//      assertEquals(simulator.simulate(this.field, 23), 45);
//    }
//
//    @Test
//    void connectMinesTest3() throws CouldNotPlaceObjectException, SimulateException {
//      this.field.addBaseObject(Obstacle.createObstacle(2, 10, 6, 1));
//      this.field.addBaseObject(Obstacle.createObstacle(10, 2, 1, 6));
//      var mine1 = Mine.createMine(0, 5, MineSubType.OUTPUT_SOUTH);
//      var mine2 = Mine.createMine(25, 25, MineSubType.OUTPUT_WEST);
//      this.field.addBaseObject(mine1);
//      this.field.addBaseObject(mine2);
//
//      var minesToConnect = new ArrayList<Mine>();
//      minesToConnect.add(mine1);
//      minesToConnect.add(mine2);
//
//      assertTrue(this.connector.connectMines(this.factoryToConnect, minesToConnect));
//
//      var simulator = Simulator.getInstance();
//      assertEquals(simulator.simulate(this.field, 22), 42);
//      // 23 turns is the optimal value for the setup, it can't get any better.
//      assertEquals(simulator.simulate(this.field, 23), 45);
//    }
//
//    @Test
//    void connectMinesTest4() throws CouldNotPlaceObjectException, SimulateException {
//      this.field.addBaseObject(Obstacle.createObstacle(2, 10, 6, 1));
//      this.field.addBaseObject(Obstacle.createObstacle(10, 2, 1, 6));
//      this.field.addBaseObject(Obstacle.createObstacle(4, 0, 1, 9));
//      this.field.addBaseObject(Obstacle.createObstacle(0, 8, 4, 2));
//      this.field.addBaseObject(Obstacle.createObstacle(13, 0, 3, 30));
//      var mine1 = Mine.createMine(0, 5, MineSubType.OUTPUT_SOUTH);
//      var mine2 = Mine.createMine(25, 25, MineSubType.OUTPUT_WEST);
//      this.field.addBaseObject(mine1);
//      this.field.addBaseObject(mine2);
//
//      var minesToConnect = new ArrayList<Mine>();
//      minesToConnect.add(mine1);
//      minesToConnect.add(mine2);
//
//      assertFalse(this.connector.connectMines(this.factoryToConnect, minesToConnect));
//
//      var simulator = Simulator.getInstance();
//      assertEquals(simulator.simulate(this.field, 23), 0);
//    }
//
//    @Test
//    void getReachableMinesTest1() throws CouldNotPlaceObjectException {
//      var reachableMines = this.connector.getReachableMines(this.factoryToConnect);
//      assertNotEquals(reachableMines, null);
//      assertTrue(reachableMines.isEmpty());
//    }
//
//    @Test
//    void getReachableMinesTest2() throws CouldNotPlaceObjectException {
//      this.field.addBaseObject(Obstacle.createObstacle(0, 7, 5, 1));
//      this.field.addBaseObject(Obstacle.createObstacle(6, 0, 1, 6));
//      var mine1 = Mine.createMine(0, 4, MineSubType.OUTPUT_SOUTH);
//      var mine2 = Mine.createMine(94, 98, MineSubType.OUTPUT_WEST);
//      this.field.addBaseObject(mine1);
//      this.field.addBaseObject(mine2);
//
//      var connectableMines = this.connector.getReachableMines(this.factoryToConnect);
//
//      assertNotEquals(connectableMines, null);
//      assertEquals(connectableMines.size(), 2);
//      assertTrue(connectableMines.contains(mine1));
//      assertTrue(connectableMines.contains(mine2));
//    }
//
//    @Test
//    void getReachableMinesTest3() throws CouldNotPlaceObjectException {
//      this.field.addBaseObject(Obstacle.createObstacle(1, 7, 4, 1));
//      this.field.addBaseObject(Obstacle.createObstacle(6, 1, 1, 5));
//      var mine1 = Mine.createMine(0, 4, MineSubType.OUTPUT_SOUTH);
//      var mine2 = Mine.createMine(94, 98, MineSubType.OUTPUT_WEST);
//      this.field.addBaseObject(mine1);
//      this.field.addBaseObject(mine2);
//
//      var connectableMines = this.connector.getReachableMines(this.factoryToConnect);
//
//      assertNotEquals(connectableMines, null);
//      assertEquals(connectableMines.size(), 2);
//      assertTrue(connectableMines.contains(mine1));
//      assertTrue(connectableMines.contains(mine2));
//    }
//
//    @Test
//    void getReachableMinesTest4() throws CouldNotPlaceObjectException {
//      this.field.addBaseObject(Obstacle.createObstacle(0, 27, 13, 3));
//      this.field.addBaseObject(Obstacle.createObstacle(13, 0, 3, 30));
//      this.field.addBaseObject(Obstacle.createObstacle(1, 7, 4, 1));
//      this.field.addBaseObject(Obstacle.createObstacle(6, 1, 1, 5));
//      var mine1 = Mine.createMine(0, 4, MineSubType.OUTPUT_SOUTH);
//      var mine2 = Mine.createMine(94, 98, MineSubType.OUTPUT_WEST);
//      this.field.addBaseObject(mine1);
//      this.field.addBaseObject(mine2);
//
//      var connectableMines = this.connector.getReachableMines(this.factoryToConnect);
//
//      assertNotEquals(connectableMines, null);
//      assertEquals(connectableMines.size(), 1);
//      assertTrue(connectableMines.contains(mine1));
//      assertFalse(connectableMines.contains(mine2));
//    }
//  }

  private Field field;
  private TileConnectionInfo[][] connectionMatrix;


  /**
   * TODO
   * @param field TODO
   */
  public ConnectorImpl(Field field) {
    this.field = field;
    this.connectionMatrix = new TileConnectionInfo[field.getHeight()][field.getWidth()];
    for (TileConnectionInfo[] matrix : this.connectionMatrix) {
      Arrays.fill(matrix, null);
    }
  }

  /**
   * TODO
   *
   * @param mines TODO
   * @param field TODO
   */
  @Override
  public void connect(Collection<Mine> mines, Field field) {
    /* */
  }

  /**
   * Connects all mines to the factory if possible.
   *
   * <p>A modified BFS is used to find positions for the placed conveyor belts that are as optimal
   * as possible. This method does not check if the given mines can actually be connected to the
   * given {@link Factory}. The method {@code getReachableMines(Field, Factory)} can be used to find
   * all {@link Mine}s that are not separated from the {@link Factory} by obstacles.</p>
   *
   * @param factory        The {@link Factory} the {@link Mine}s will be connected to.
   * @param minesToConnect The {@link Mine}s that are supposed to be connected to the given
   *                       {@link Factory}.
   * @return {@code true} if all mines in the {@link Collection} could be connected, false
   * otherwise.
   */
  @Override
  public boolean connectMines(Factory factory, Collection<Mine> minesToConnect) {
    // Just use the results of getReachableMines to find paths from the given Mines to the Factory.
    return false;
  }

  /**
   * Finds all {@link Mine}s that are not separated from the given {@link Factory} by
   * obstacles
   *
   * @param factory The {@link Factory} the reachable mines are searched for.
   * @return a list of mines that potentially can be connected to the factory.
   */
  @Override
  public Collection<Mine> getReachableMines(Factory factory) {
    // For every free Tile around the Factory do the following (modified BFS)
    //  1. Try to place all possible variants of a conveyor. The conveyor can be placed if the
    //     following is true:
    //     i. The output field of the conveyor must be on the current Tile. */
    //     ii. The input field of the conveyor lies on a Tile that is either unhandled or has been
    //         handled (and doesn't deny the conveyor to be placed?).
    //     iii. The field allows the conveyor to be placed.
    //  2. Write all relevant information into the current Tile and all target Tiles.
    //  3. If the input Tile of the placable conveyor hasn't been handled, queue all Tiles around
    //     the input that are not blocked and haven't been handled yet, into a FIFO-queue.
    //  4. If the Tiles around the Input Tile have been handled already, check if it is an input or
    //     an output that has been placed there, if it was an input, queue the Tile, too.
    //  5. The newly placed conveyor gets a special number 0 if it is directly connected to the
    //     factory or the special number of the previous conveyor input + 1.
    //  6. When there is a Mine connected to the input of the conveyor, add the Mine to the list
    //     of reachable mines.
    // When the FIFO queue is empty (or every Mine on the field has been reached?), start search
    // from the next free Tile around the Factory.
    // Comment 1. In the end this method tries to determine for every Tile:
    //            o Which Tiles are reachable from the Tile (starting from the mine).
    //            o How far away from the factory is the Tile (measured in conveyor steps).
    //            This information is saved in an 2-dimensional array. Most likely as a map of
    //            Coordinates, the key being the next input field, the value being a list of
    //            Coordinates where the output of the field can possibly be. Together with the
    //            rest steps that must be taken if using the respective output coordinates.
    return new Vector<>();
  }

  private static class TileConnectionInfo {

  }
}
