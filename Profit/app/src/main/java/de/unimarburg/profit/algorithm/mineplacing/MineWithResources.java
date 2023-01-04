package de.unimarburg.profit.algorithm.mineplacing;

import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.enums.ResourceType;

/**
 * An object of {@link MineWithResources} wraps a single {@link Mine} and adds more information to
 * it. It stores what {@link ResourceType} the Resources have, that the {@link Mine} produces and
 * how much the mine will produce in its life cicle.
 *
 * @author Yannick Kraml
 */
public class MineWithResources {

  private final Mine mine;
  private final ResourceType resourceType;
  private final int amount;

  /**
   * Constructor of {@link MineWithResources}.
   *
   * @param mine         {@link Mine}, to which more information is added.
   * @param resourceType {@link ResourceType} of the Resources the {@link Mine} produces.
   * @param amount       Amount of Resources the {@link Mine} will produce.
   */
  public MineWithResources(Mine mine, ResourceType resourceType, int amount) {
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
