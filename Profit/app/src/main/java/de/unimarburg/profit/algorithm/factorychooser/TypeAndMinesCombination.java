package de.unimarburg.profit.algorithm.factorychooser;

import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.enums.ProductType;
import java.util.Collection;

public class TypeAndMinesCombination {


  private final ProductType productType;

  private final Collection<Mine> minesToConnect;


  public TypeAndMinesCombination(ProductType productType, Collection<Mine> minesToConnect) {
    this.productType = productType;
    this.minesToConnect = minesToConnect;
  }

  public ProductType getProductType() {
    return productType;
  }

  public Collection<Mine> getMinesToConnect() {
    return minesToConnect;
  }
}
