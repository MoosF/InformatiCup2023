package model;

import java.util.HashMap;
import java.util.Map;
import model.enums.ResourceType;

/**
 * Interface for anything, that can work and therefore can be simulated.
 *
 * @author Yannick Kraml
 */
public interface Works {


  /**
   * Takes a map of stored resources, removes resources from it and generates points.
   *
   * @param storedResources Map, which contains the current resources
   * @return Points generated
   */
  default int doWorkForPoints(Map<ResourceType, Integer> storedResources) {
    return 0;
  }


  /**
   * Generates a map, that contains all resources the worker should start with.
   *
   * @return Map of all starting resources
   */
  default Map<ResourceType, Integer> getStartResources() {
    return new HashMap<>();
  }


  /**
   * Checks the stored resources and decides, which of these should be outputted next.
   *
   * @param storedResources Map, which contains the current resources
   * @return Map, which contains all resources, which should be outputted next
   */
  default Map<ResourceType, Integer> getResourcesToOutput(
      Map<ResourceType, Integer> storedResources) {
    return new HashMap<>();
  }
}
