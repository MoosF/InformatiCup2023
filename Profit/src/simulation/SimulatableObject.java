package simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import model.BaseObject;
import model.ResourceType;

public class SimulatableObject {

  private final BaseObject worker;
  private final Map<ResourceType, Integer> storedResources;
  private final Map<ResourceType, Integer> inputtedResources;

  public SimulatableObject(BaseObject worker) {
    this.worker = worker;
    storedResources = worker.getStartResources();
    inputtedResources = new HashMap<>();
  }

  public void inputResources(Map<ResourceType, Integer> resources) {
    resources.forEach(this::putResourceIntoStorage);
  }

  public void removeResources(Map<ResourceType, Integer> resources)
      throws CouldNotRemoveResourceFromStorageException {
    for (Entry<ResourceType, Integer> entry : resources.entrySet()) {
      removeResourceFromStorage(entry.getKey(), entry.getValue());
    }
  }

  public int doWorkForPoints() {
    return worker.doWorkForPoints(storedResources);
  }

  public Map<ResourceType, Integer> getResourcesToOutput() {
    return new HashMap<>(worker.getResourcesToOutput(Collections.unmodifiableMap(storedResources)));
  }

  public BaseObject getWorker() {
    return worker;
  }

  private void putResourceIntoStorage(ResourceType resourceType, Integer amount) {
    if (inputtedResources.containsKey(resourceType)) {
      inputtedResources.put(resourceType, inputtedResources.get(resourceType) + amount);
    } else {
      inputtedResources.put(resourceType, amount);
    }
  }

  private void removeResourceFromStorage(ResourceType key, Integer amount)
      throws CouldNotRemoveResourceFromStorageException {

    if (!storedResources.containsKey(key)) {
      throw new CouldNotRemoveResourceFromStorageException("Resource was not in Map.");
    }

    int newAmount = storedResources.get(key) - amount;
    if (newAmount < 0) {
      throw new CouldNotRemoveResourceFromStorageException("Not enough Resource in Storage.");
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

  public Map<ResourceType, Integer> getStoredResources() {
    return storedResources;
  }

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
}
