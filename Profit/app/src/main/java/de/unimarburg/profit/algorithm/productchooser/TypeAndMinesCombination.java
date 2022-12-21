package de.unimarburg.profit.algorithm.productchooser;

import de.unimarburg.profit.algorithm.mineplacer.MineResourceAmount;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.enums.ProductType;
import de.unimarburg.profit.model.enums.ResourceType;
import java.util.Collection;
import java.util.Map;

/**
 * This class saves a single combination of a {@link ProductType} and a set of {@link Mine}s.
 *
 * @author Yannick Kraml
 */
public class TypeAndMinesCombination {


  private final Product product;

  private final Collection<MineResourceAmount> mines;


  public TypeAndMinesCombination(Product product,
      Collection<MineResourceAmount> mines) {
    this.product = product;
    this.mines = mines;
  }

  public Product getProductType() {
    return product;
  }

  public Collection<MineResourceAmount> getMines() {
    return mines;
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

    for (MineResourceAmount mineResourceAmount : mines) {

      Mine mine = mineResourceAmount.getMine();
      int amount = mineResourceAmount.getAmount();
      ResourceType resourceType = mineResourceAmount.getResourceType();

    }

    // TODO: 21.12.2022 Calc value of this Combination


    return -1;
  }

}
