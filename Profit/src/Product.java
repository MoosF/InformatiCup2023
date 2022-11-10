import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class models a {@link Product}.
 *
 * @author Yannick Kraml
 */
public class Product {

  private final Map<ResourceType, Integer> neededResourcesMap;
  private final int points;

  /**
   * Constructor of {@link Product}.
   *
   * @param points Points, that the {@link Product} is worth.
   */
  public Product(int points) {
    this.points = points;
    neededResourcesMap = Collections.synchronizedMap(new HashMap<>());
  }

  /**
   * Returns a map with the needed resources to produce one unit of the {@link Product}.
   *
   * @return Map, that contains the resources as keys and the amount needed per resource as values.
   */
  public Map<ResourceType, Integer> getNeededResourcesMap() {
    return Collections.unmodifiableMap(neededResourcesMap);
  }

  /**
   * Returns the points, that the {@link Product} is worth.
   *
   * @return Points, that the {@link Product} is worth.
   */
  public int getPoints() {
    return points;
  }

  /**
   * Adds a single resource to the {@link Product}, that is needed to produce it. Each
   * {@link ResourceType} can only be used once.
   *
   * @param resourceType Needed resource for production.
   * @param amount       Amount of the given resource.
   */
  public void addNeededRessource(ResourceType resourceType, int amount) {
    if (neededResourcesMap.containsKey(resourceType)) {
      throw new RuntimeException("Resource type already registered.");
    }
    neededResourcesMap.put(resourceType, amount);
  }
}
