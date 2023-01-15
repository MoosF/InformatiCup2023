package de.unimarburg.profit.simulation;

import de.unimarburg.profit.model.Tile;
import de.unimarburg.profit.model.enums.ResourceType;
import java.util.HashMap;
import java.util.Map;

/**
 * Interface for anything, that can work and therefore can be simulated.
 *
 * @author Yannick Kraml
 */
public interface Worker {


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

  /**
   * Returns all {@link Tile} from this {@link Worker}.
   *
   * @return Tiles, that construct the {@link Worker}.
   */
  Tile[] getTiles();

  /**
   * Returns the horizontal position of this {@link Worker}.
   *
   * @return X-Coordinate of the {@link Worker}.
   */
  int getX();

  /**
   * Returns the vertical position of this {@link Worker}.
   *
   * @return Y-Coordinate of the {@link Worker}.
   */
  int getY();
}
