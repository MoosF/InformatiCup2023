package de.uni_marburg.profit.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import de.uni_marburg.profit.model.Field;
import de.uni_marburg.profit.model.enums.ResourceType;
import de.uni_marburg.profit.model.Tile;
import de.uni_marburg.profit.model.enums.TileType;

/**
 * This class is responsible for simulating a given {@link Field}. {@link Simulator} is a
 * singleton.
 *
 * @author Yannick Kraml
 */
public class Simulator {

  private static Simulator instance;

  private Simulator() {
  }

  /**
   * Returns an instance of {@link Simulator}.
   *
   * @return Instance of {@link Simulator}.
   */
  public static Simulator getInstance() {
    if (instance == null) {
      instance = new Simulator();
    }
    return instance;
  }

  /**
   * Starts the Simulation with the given {@link Field}.
   *
   * @param field {@link Field} to simulate.
   * @return Earned points.
   */
  public int simulate(Field field, int turns) throws SimulateException {

    Collection<SimulatableObject> objects = field.getAllObjects().stream()
        .map(SimulatableObject::new).toList();

    Collection<Coupled> coupledObjects = findCoupledObjects(objects, field);

    int points = 0;
    for (int i = 0; i < turns; i++) {
      points += simulateRound(objects, coupledObjects);
    }

    return points;
  }

  private int simulateRound(Collection<SimulatableObject> objects, Collection<Coupled> couples)
      throws SimulateException {

    for (Coupled coupledObject : couples) {
      for (SimulatableObject giver : coupledObject.givers()) {

        Map<ResourceType, Integer> resourcesToBeMoved = giver.getResourcesToOutput();
        giver.removeResources(resourcesToBeMoved);
        coupledObject.receiver().inputResources(resourcesToBeMoved);

      }
    }

    int points = objects.stream().mapToInt(SimulatableObject::doWorkForPoints).sum();
    objects.forEach(SimulatableObject::transfer);

    return points;
  }

  private Collection<Coupled> findCoupledObjects(Collection<SimulatableObject> simulatableObjects,
      Field field) throws SimulateException {

    Collection<Coupled> coupledObjects = new ArrayList<>();

    for (SimulatableObject simulatableObject : simulatableObjects) {

      Collection<SimulatableObject> objectsToCouple = new ArrayList<>();

      for (Tile tile : simulatableObject.getWorker().getTiles()) {

        int x = tile.getRelHorPos() + simulatableObject.getWorker().getX();
        int y = tile.getRelVerPos() + simulatableObject.getWorker().getY();

        for (Tile neighbor : getNeighbors(field, x, y)) {
          if (areConnected(tile, neighbor)) {
            objectsToCouple.add(getSimulatedObjectByTile(simulatableObjects, neighbor));
          }
        }

      }

      if (!objectsToCouple.isEmpty()) {
        coupledObjects.add(new Coupled(simulatableObject, objectsToCouple));
      }

    }

    return coupledObjects;
  }

  private Collection<Tile> getNeighbors(Field field, int x, int y) {
    Collection<Tile> neighbors = new LinkedList<>();
    addNeighbor(neighbors, field, x + 1, y);
    addNeighbor(neighbors, field, x - 1, y);
    addNeighbor(neighbors, field, x, y + 1);
    addNeighbor(neighbors, field, x, y - 1);
    return neighbors;
  }

  private void addNeighbor(Collection<Tile> neighbors, Field field, int x, int y) {
    Tile[][] array = field.getTiles();
    if (x >= 0 && y >= 0 && x < array.length && y < array[x].length) {
      neighbors.add(array[x][y]);
    }
  }

  private boolean areConnected(Tile tile, Tile neighbor) {
    return neighbor.getType().equals(TileType.OUTPUT) && tile.getType().equals(TileType.INPUT)
        || neighbor.getType().equals(TileType.DEPOSIT_OUTPUT) && tile.getType()
        .equals(TileType.MINE_INPUT);
  }


  private SimulatableObject getSimulatedObjectByTile(
      Collection<SimulatableObject> simulatableObjects, Tile tile) throws SimulateException {

    for (SimulatableObject simulatableObject : simulatableObjects) {
      if (tile.getObject().isPresent() && simulatableObject.getWorker()
          .equals(tile.getObject().get())) {
        return simulatableObject;
      }
    }

    throw new SimulateException("Simulatable object not found.");
  }
}
