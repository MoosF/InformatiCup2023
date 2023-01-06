package de.unimarburg.profit.model;

import de.unimarburg.profit.model.enums.ProductType;
import de.unimarburg.profit.model.enums.ResourceType;
import java.util.Collections;
import java.util.Map;

/**
 * This class models a {@link Product}.
 *
 * @author Yannick Kraml
 */
public class Product {

  private final ProductType type;
  private final Map<ResourceType, Integer> neededResourcesMap;
  private final int points;

  /**
   * Constructor of {@link Product}.
   *
   * @param points            Points, that the {@link Product} is worth.
   * @param type              Type of the {@link Product}.
   * @param requiredResources Needed resources for the {@link Product}.
   */
  public Product(int points, ProductType type, Map<ResourceType, Integer> requiredResources) {
    this.type = type;
    this.points = points;
    this.neededResourcesMap = requiredResources;
  }

  /**
   * Returns a map with the needed resources to produce one unit of the {@link Product}.
   *
   * @return Map, that contains the resources as keys and the amount needed per resource as values.
   */
  public Map<ResourceType, Integer> getNeededResources() {
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

  public ProductType getType() {
    return type;
  }

  @Override
  public boolean equals(Object obj) {
    if (!this.getClass().equals(obj.getClass())) {
      return false;
    }
    Product rhs = (Product) obj;
    boolean equality = this.type == rhs.type;
    equality = equality && this.points == rhs.points;
    equality = equality && this.neededResourcesMap.equals(rhs.neededResourcesMap);
    return equality;
  }

  @Override
  public String toString() {
    return "Product{" +
        "type=" + type +
        ", neededResourcesMap=" + neededResourcesMap +
        ", points=" + points +
        '}';
  }
}
