package de.unimarburg.profit.algorithm.productchooser;

import de.unimarburg.profit.algorithm.mineplacer.MineResourcePair;
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


  private final Product product;

  private final Collection<MineResourcePair> mineResourcePairs;


  public TypeAndMinesCombination(Product product, Collection<MineResourcePair> mineResourcePairs) {
    this.product = product;
    this.mineResourcePairs = mineResourcePairs;
  }

  public Product getProduct() {
    return product;
  }

  public Collection<MineResourcePair> getMineResourcePairs() {
    return mineResourcePairs;
  }

  /**
   * Calculates how good the {@link TypeAndMinesCombination} is. This method uses for the
   * calculation the points of the {@link Product}.
   *
   * @return Value of the {@link TypeAndMinesCombination}.
   */
  public double getValue() {


    int points = product.getPoints();
    Map<ResourceType, Integer> neededResources = product.getNeededResources();

    Map<ResourceType, Integer> availableResources = new HashMap<>();
    for (ResourceType resourceType : ResourceType.values()) {
      availableResources.put(resourceType, 0);
    }

    for (MineResourcePair mineResourcePair : mineResourcePairs) {

      Mine mine = mineResourcePair.getMine();
      int amount = mineResourcePair.getAmount();

      ResourceType resourceType = mineResourcePair.getResourceType();

      //If there exists an unnecessary mine.
      if (!neededResources.containsKey(resourceType)) {
        return 0;
      }

      availableResources.put(resourceType, availableResources.get(resourceType) + amount);

    }

    double value = Integer.MAX_VALUE;
    for (ResourceType resourceType : neededResources.keySet()) {

      double available = availableResources.get(resourceType);
      double needed = neededResources.get(resourceType);

      //If there are not enough resources.
      if (needed > available) {
        return 0;
      }

      double resourceRatio = available / needed;
      if (resourceRatio < value) {
        value = resourceRatio;
      }
    }

    return value;
  }

}
