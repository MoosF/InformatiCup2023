package simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Consumer;
import model.Conveyor;
import model.Field;
import model.ResourceType;
import model.Tile;
import model.TileType;

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
  public int simulate(Field field, int turns) {

    int points = 0;

    Collection<SimulatableObject> simulatableObjects = field.getObjects().stream()
        .map(SimulatableObject::new).toList();

    Collection<Coupled> coupledObjects = findCoupledObjects(simulatableObjects, field);

    for (int i = 1; i <= turns; i++) {
      try {
        points += simulateRound(simulatableObjects, coupledObjects);
      } catch (CouldNotRemoveResourceFromStorageException e) {
        throw new RuntimeException(e);
      }
    }

    return points;
  }

  private Collection<Coupled> findCoupledObjects(Collection<SimulatableObject> simulatableObjects,
      Field field) {

    Collection<Coupled> coupledObjects = new ArrayList<>();

    for (SimulatableObject simulatableObject : simulatableObjects) {

      Collection<SimulatableObject> objectsToCouple = new ArrayList<>();

      Tile[] tiles = simulatableObject.getWorker().getTiles();

      for (Tile tile : tiles) {

        int tileXLocation = tile.getX() + simulatableObject.getWorker().getX();
        int tileYLocation = tile.getY() + simulatableObject.getWorker().getY();

        for (Tile neighbor : getNeighbors(field, tileXLocation, tileYLocation)) {
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

  private boolean areConnected(Tile tile, Tile neighbor) {
    return neighbor.getType().equals(TileType.OUTPUT) && tile.getType().equals(TileType.INPUT)
        || neighbor.getType().equals(TileType.DEPOSIT_OUTPUT) && tile.getType()
        .equals(TileType.MINE_INPUT);
  }

  private SimulatableObject getSimulatedObjectByTile(
      Collection<SimulatableObject> simulatableObjects, Tile neighbor) {

    for (SimulatableObject simulatableObject : simulatableObjects) {
      if (neighbor.getObject().isPresent() && simulatableObject.getWorker()
          .equals(neighbor.getObject().get())) {
        return simulatableObject;
      }
    }

    throw new RuntimeException("Simulatable object not found.");
  }

  private Collection<Tile> getNeighbors(Field field, int tileXLocation, int tileYLocation) {
    Collection<Tile> neighbors = new LinkedList<>();

    try {
      neighbors.add(field.getArray()[tileXLocation + 1][tileYLocation]);
    } catch (ArrayIndexOutOfBoundsException ignored) {

    }
    try {
      neighbors.add(field.getArray()[tileXLocation - 1][tileYLocation]);
    } catch (ArrayIndexOutOfBoundsException ignored) {

    }
    try {
      neighbors.add(field.getArray()[tileXLocation][tileYLocation + 1]);
    } catch (ArrayIndexOutOfBoundsException ignored) {

    }
    try {
      neighbors.add(field.getArray()[tileXLocation][tileYLocation - 1]);
    } catch (ArrayIndexOutOfBoundsException ignored) {

    }

    return neighbors;
  }

  private int simulateRound(Collection<SimulatableObject> simulatableObjects,
      Collection<Coupled> coupledObjects)
      throws CouldNotRemoveResourceFromStorageException {

    int points = 0;

    for (Coupled coupledObject : coupledObjects) {

      SimulatableObject receiver = coupledObject.getReceiver();
      Collection<SimulatableObject> givers = coupledObject.getGiver();

      for (SimulatableObject giver : givers) {
        Map<ResourceType, Integer> resourcesToBeMoved = giver.getResourcesToOutput();
        giver.removeResources(resourcesToBeMoved);
        receiver.inputResources(resourcesToBeMoved);
      }
    }

    for (SimulatableObject simulatableObject : simulatableObjects) {
      points += simulatableObject.doWorkForPoints();
    }

    simulatableObjects.forEach(SimulatableObject::transfer);

    return points;
  }
}
