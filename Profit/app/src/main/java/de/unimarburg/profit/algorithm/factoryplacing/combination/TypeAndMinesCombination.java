package de.unimarburg.profit.algorithm.factoryplacing.combination;

import de.unimarburg.profit.algorithm.mineplacing.MineWithResources;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.enums.ProductType;
import de.unimarburg.profit.model.enums.ResourceType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class saves a single combination of a {@link ProductType} and a set of {@link Mine}s.
 *
 * @author Yannick Kraml
 */
public class TypeAndMinesCombination {


  private final Factory factory;
  private final Product product;

  private final Collection<MineWithResources> minesWithResources;


  /**
   * Constructor of this class.
   *
   * @param factory            {@link Factory}, with which the {@link Mine}s are connected.
   * @param product            {@link Product}, which the {@link Factory} should produce.
   * @param minesWithResources Collection of {@link Mine}s with their mined Resources.
   */
  public TypeAndMinesCombination(Factory factory, Product product,
      Collection<MineWithResources> minesWithResources) {
    this.factory = factory;
    this.product = product;
    this.minesWithResources = minesWithResources;
  }

  public Product getProduct() {
    return product;
  }

  public Collection<MineWithResources> getMinesWithResources() {
    return minesWithResources;
  }

  /**
   * Calculates how good the {@link TypeAndMinesCombination} is. This method uses for the
   * calculation the points of the {@link Product}.
   *
   * @return Value of the {@link TypeAndMinesCombination}.
   */
  public double getValue() {

    Map<ResourceType, Integer> neededResources = product.getNeededResources();

    Map<ResourceType, Integer> availableResources = new HashMap<>();
    for (ResourceType resourceType : ResourceType.values()) {
      availableResources.put(resourceType, 0);
    }

    for (MineWithResources mineWithResources : minesWithResources) {

      int amount = mineWithResources.getAmount();

      ResourceType resourceType = mineWithResources.getResourceType();

      //If there exists an unnecessary mine.
      if (!neededResources.containsKey(resourceType)) {
        return 0;
      }

      availableResources.put(resourceType, availableResources.get(resourceType) + amount);

    }

    double amount = Integer.MAX_VALUE;
    for (ResourceType resourceType : neededResources.keySet()) {

      double available = availableResources.get(resourceType);
      double needed = neededResources.get(resourceType);

      //If there are not enough resources.
      if (needed > available) {
        amount = 0;
      }

      double resourceRatio = available / needed;
      if (resourceRatio < amount) {
        amount = resourceRatio;
      }
    }

    return amount * product.getPoints();
  }

  /**
   * Calculates the cumulative distance between the {@link Factory} to each {@link Mine}.
   *
   * @return Sum of distances.
   */
  public int getDistances() {

    int distance = 0;

    int factoryX = factory.getX();
    int factoryY = factory.getY();

    for (MineWithResources mineWithResources : minesWithResources) {
      int mineX = mineWithResources.getMine().getX();
      int mineY = mineWithResources.getMine().getY();

      distance += Math.sqrt(Math.pow(factoryX - mineX, 2) + Math.pow(factoryY - mineY, 2));
    }

    return distance;
  }


  public Factory getFactory() {
    return factory;
  }

  public Collection<Mine> getMines() {
    return minesWithResources.stream().map(MineWithResources::getMine).toList();
  }
}
