package de.uni.marburg.profit.model;

import de.uni.marburg.profit.model.enums.ProductType;
import de.uni.marburg.profit.model.enums.ResourceType;
import de.uni.marburg.profit.model.enums.TileType;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class models a {@link Factory}.
 *
 * @author Yannick Kraml
 */
public class Factory extends MovableObject {

  private Product product;
  private final ProductType productType;

  /**
   * Constructor of {@link Factory}.
   *
   * @param horPos  X-Coordinate of {@link Factory}.
   * @param verPos  Y-Coordinate of {@link Factory}.
   * @param tiles   Tiles, that construct the {@link Factory}.
   * @param product {@link Product} that is produces by the {@link Factory}.
   */
  private Factory(int horPos, int verPos, Tile[] tiles, Product product, MovableObjectType type) {
    super(horPos, verPos, tiles, type);
    this.product = product;
    productType = product.getType();
  }

  private Factory(int horPos, int verPos, Tile[] tiles, int subtype, MovableObjectType type) {
    super(horPos, verPos, tiles, type);
    switch (subtype) {
      case 0 -> productType = ProductType.ZERO;
      case 1 -> productType = ProductType.ONE;
      case 2 -> productType = ProductType.TWO;
      case 3 -> productType = ProductType.THREE;
      case 4 -> productType = ProductType.FOUR;
      case 5 -> productType = ProductType.FIVE;
      case 6 -> productType = ProductType.SIX;
      default -> productType = ProductType.SEVEN;
    }
  }

  /**
   * Creates an instance of a {@link Factory}.
   *
   * @param horPos  X-Coordinate of the {@link Factory}.
   * @param verPos  Y-Coordinate of the {@link Factory}.
   * @param product {@link Product} that is produced by the {@link Factory}.
   * @return New instance of {@link Factory}.
   */
  public static Factory createFactory(int horPos, int verPos, Product product) {

    Tile[] tiles = new Tile[]{
        new Tile(-2, -2, TileType.INPUT),
        new Tile(-1, -2, TileType.INPUT),
        new Tile(0, -2, TileType.INPUT),
        new Tile(1, -2, TileType.INPUT),
        new Tile(2, -2, TileType.INPUT),
        new Tile(-2, -1, TileType.INPUT),
        new Tile(-1, -1, TileType.SOLID),
        new Tile(0, -1, TileType.SOLID),
        new Tile(1, -1, TileType.SOLID),
        new Tile(2, -1, TileType.INPUT),
        new Tile(-2, 0, TileType.INPUT),
        new Tile(-1, 0, TileType.SOLID),
        new Tile(0, 0, TileType.SOLID),
        new Tile(1, 0, TileType.SOLID),
        new Tile(2, 0, TileType.INPUT),
        new Tile(-2, 1, TileType.INPUT),
        new Tile(-1, 1, TileType.SOLID),
        new Tile(0, 1, TileType.SOLID),
        new Tile(1, 1, TileType.SOLID),
        new Tile(2, 1, TileType.INPUT),
        new Tile(-2, 2, TileType.INPUT),
        new Tile(-1, 2, TileType.INPUT),
        new Tile(0, 2, TileType.INPUT),
        new Tile(1, 2, TileType.INPUT),
        new Tile(2, 2, TileType.INPUT),
    };

    return new Factory(horPos, verPos, tiles, product, MovableObjectType.FACTORY);
  }

  public static Factory createFactory(int horPos, int verPos, int subtype) {

    Tile[] tiles = new Tile[]{
        new Tile(-2, -2, TileType.INPUT),
        new Tile(-1, -2, TileType.INPUT),
        new Tile(0, -2, TileType.INPUT),
        new Tile(1, -2, TileType.INPUT),
        new Tile(2, -2, TileType.INPUT),
        new Tile(-2, -1, TileType.INPUT),
        new Tile(-1, -1, TileType.SOLID),
        new Tile(0, -1, TileType.SOLID),
        new Tile(1, -1, TileType.SOLID),
        new Tile(2, -1, TileType.INPUT),
        new Tile(-2, 0, TileType.INPUT),
        new Tile(-1, 0, TileType.SOLID),
        new Tile(0, 0, TileType.SOLID),
        new Tile(1, 0, TileType.SOLID),
        new Tile(2, 0, TileType.INPUT),
        new Tile(-2, 1, TileType.INPUT),
        new Tile(-1, 1, TileType.SOLID),
        new Tile(0, 1, TileType.SOLID),
        new Tile(1, 1, TileType.SOLID),
        new Tile(2, 1, TileType.INPUT),
        new Tile(-2, 2, TileType.INPUT),
        new Tile(-1, 2, TileType.INPUT),
        new Tile(0, 2, TileType.INPUT),
        new Tile(1, 2, TileType.INPUT),
        new Tile(2, 2, TileType.INPUT),
    };

    return new Factory(horPos, verPos, tiles, subtype, MovableObjectType.FACTORY);
  }

  @Override
  public int doWorkForPoints(Map<ResourceType, Integer> storedResources) {
    int points = 0;

    boolean addedPoints = true;
    while (addedPoints) {
      addedPoints = false;

      Map<ResourceType, Integer> neededResourcesMap = product.getNeededResourcesMap();

      //Check if neededResources are present.
      boolean willProduce = true;
      for (Entry<ResourceType, Integer> entry : neededResourcesMap.entrySet()) {
        Integer amountNeeded = entry.getValue();
        ResourceType typeNeeded = entry.getKey();

        if (!storedResources.containsKey(typeNeeded)) {
          willProduce = false;
          break;
        }

        Integer storedAmount = storedResources.get(typeNeeded);
        if (amountNeeded > storedAmount) {
          willProduce = false;
          break;
        }
      }

      //Remove needed resources and add points.
      if (willProduce) {

        for (Entry<ResourceType, Integer> entry : neededResourcesMap.entrySet()) {
          Integer amountNeeded = entry.getValue();
          ResourceType typeNeeded = entry.getKey();
          Integer oldAmount = storedResources.get(typeNeeded);
          storedResources.put(typeNeeded, oldAmount - amountNeeded);
        }

        points += product.getPoints();
        addedPoints = true;
      }
    }

    return points;
  }

  public ProductType getSubType() {
    return this.product.getType();
  }
}
