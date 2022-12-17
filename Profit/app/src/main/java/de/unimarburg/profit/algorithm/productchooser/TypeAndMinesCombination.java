package de.unimarburg.profit.algorithm.productchooser;

import de.unimarburg.profit.model.Mine;
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


  private final ProductType productType;

  private final Map<ResourceType, Collection<Mine>> minesToConnect;


  public TypeAndMinesCombination(ProductType productType,
      Map<ResourceType, Collection<Mine>> minesToConnect) {
    this.productType = productType;
    this.minesToConnect = minesToConnect;
  }

  public ProductType getProductType() {
    return productType;
  }

  public Map<ResourceType, Collection<Mine>> getMinesToConnect() {
    return minesToConnect;
  }
}
