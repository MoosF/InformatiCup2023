package de.uni_marburg.profit.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uni_marburg.profit.model.BaseObject;
import de.uni_marburg.profit.model.enums.ResourceType;

/**
 * {@link SimulatableObject} contains a {@link BaseObject} and enables its simulation.
 *
 * @author Yannick Kraml
 */
public class SimulatableObject {

  private final Worker worker;
  private final Map<ResourceType, Integer> storedResources;
  private final Map<ResourceType, Integer> inputtedResources;

  /**
   * Constructor of this class.
   *
   * @param worker {@link BaseObject}, that should be simulated.
   */
  public SimulatableObject(BaseObject worker) {
    this.worker = worker;
    storedResources = worker.getStartResources();
    inputtedResources = new HashMap<>();
  }

  /**
   * Inputs resources into the temporary storage of this {@link SimulatableObject}.
   *
   * @param resources Map, that contains the resources to store.
   */
  public void inputResources(Map<ResourceType, Integer> resources) {
    resources.forEach(this::putResourceIntoStorage);
  }

  /**
   * Removes resources from this {@link SimulatableObject}.
   *
   * @param resources Map, that contains the resources, which will be removed.
   */
  public void removeResources(Map<ResourceType, Integer> resources) throws SimulateException {
    for (Entry<ResourceType, Integer> entry : resources.entrySet()) {
      removeResourceFromStorage(entry.getKey(), entry.getValue());
    }
  }

  /**
   * Starts a work routine, where this {@link SimulatableObject} takes its resources and produces
   * points.
   *
   * @return Points, that were generated.
   */
  public int doWorkForPoints() {
    return worker.doWorkForPoints(storedResources);
  }

  /**
   * Stats a routine to check which resources this {@link SimulatableObject} should output at each
   * output. Attention: this method does not remove the resources. It only checks, which should be
   * removed.
   *
   * @return Resources, that should be outputted.
   */
  public Map<ResourceType, Integer> getResourcesToOutput() {
    return new HashMap<>(worker.getResourcesToOutput(Collections.unmodifiableMap(storedResources)));
  }

  /**
   * Returns the {@link BaseObject}, that is simulated.
   *
   * @return BaseObject, that is wrapped inside this {@link SimulatableObject}.
   */
  public Worker getWorker() {
    return worker;
  }

  /**
   * Transfers all inputted resources from the temporary storage to the final storage.
   */
  public void transfer() {
    inputtedResources.forEach((resourceType, integer) -> {
      if (storedResources.containsKey(resourceType)) {
        storedResources.put(resourceType, storedResources.get(resourceType) + integer);
      } else {
        storedResources.put(resourceType, integer);
      }
    });
    inputtedResources.clear();
  }

  private void putResourceIntoStorage(ResourceType resourceType, Integer amount) {
    if (inputtedResources.containsKey(resourceType)) {
      inputtedResources.put(resourceType, inputtedResources.get(resourceType) + amount);
    } else {
      inputtedResources.put(resourceType, amount);
    }
  }

  private void removeResourceFromStorage(ResourceType key, Integer amount)
      throws SimulateException {

    if (!storedResources.containsKey(key)) {
      throw new SimulateException("Resource was not in Map.");
    }

    int newAmount = storedResources.get(key) - amount;
    if (newAmount < 0) {
      throw new SimulateException("Not enough Resource in Storage.");
    }
    storedResources.put(key, newAmount);

    //Entfernen
    Collection<ResourceType> toBeDeleted = new ArrayList<>();
    for (Entry<ResourceType, Integer> resourceTypeIntegerEntry : storedResources.entrySet()) {
      if (resourceTypeIntegerEntry.getValue() == 0) {
        toBeDeleted.add(resourceTypeIntegerEntry.getKey());
      }
    }
    toBeDeleted.forEach(storedResources::remove);
  }
}
