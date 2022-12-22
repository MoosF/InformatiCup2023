package de.unimarburg.profit.algorithm.mineplacer;

import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.enums.ResourceType;

public class MineResourcePair {

  private final Mine mine;
  private final ResourceType resourceType;
  private final int amount;

  public MineResourcePair(Mine mine, ResourceType resourceType, int amount) {
    this.mine = mine;
    this.resourceType = resourceType;
    this.amount = amount;
  }

  public Mine getMine() {
    return mine;
  }

  public ResourceType getResourceType() {
    return resourceType;
  }

  public int getAmount() {
    return amount;
  }
}
