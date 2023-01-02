package de.unimarburg.profit.model;

import de.unimarburg.profit.model.enums.ProductType;
import de.unimarburg.profit.model.enums.ResourceType;
import de.unimarburg.profit.model.enums.TileType;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class models a {@link Factory}.
 *
 * @author Yannick Kraml
 */
public class Factory extends MovableObject {

  private Product product;
  private ProductType productType;

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

  public Factory(int horPos, int verPos, Tile[] tiles, MovableObjectType type) {
    super(horPos, verPos, tiles, type);
  }


  public void setProduct(Product product) {
    this.product = product;
  }

  public void setProductType(ProductType productType) {
    this.productType = productType;
  }

  /**
   * Creates an instance of a {@link Factory}.
   *
   * @param horPos  X-Coordinate of the {@link Factory}.
   * @param verPos  Y-Coordinate of the {@link Factory}.
   * @param product {@link Product} that is produced by the {@link Factory}.
   * @return New instance of {@link Factory}.
   */
  public static Factory createFactoryWithProduct(int horPos, int verPos, Product product) {
    Tile[] tiles = createTilesArray();
    return new Factory(horPos, verPos, tiles, product, MovableObjectType.FACTORY);
  }

  /**
   * Creates an instance of a {@link Factory} without a product.
   *
   * @param horPos X-Coordinate of the {@link Factory}.
   * @param verPos Y-Coordinate of the {@link Factory}.
   * @return New instance of {@link Factory}.
   */
  public static Factory createFactoryWithoutProduct(int horPos, int verPos) {
    Tile[] tiles = createTilesArray();
    return new Factory(horPos, verPos, tiles, MovableObjectType.FACTORY);
  }

  @Override
  public int doWorkForPoints(Map<ResourceType, Integer> storedResources) {
    int points = 0;

    boolean addedPoints = true;
    while (addedPoints) {
      addedPoints = false;

      Map<ResourceType, Integer> neededResourcesMap = product.getNeededResources();

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

  private static Tile[] createTilesArray() {
    return new Tile[] {
        new Tile(0, 0, TileType.INPUT),
        new Tile(1, 0, TileType.INPUT),
        new Tile(2, 0, TileType.INPUT),
        new Tile(3, 0, TileType.INPUT),
        new Tile(4, 0, TileType.INPUT),
        new Tile(0, 1, TileType.INPUT),
        new Tile(1, 1, TileType.SOLID),
        new Tile(2, 1, TileType.SOLID),
        new Tile(3, 1, TileType.SOLID),
        new Tile(4, 1, TileType.INPUT),
        new Tile(0, 2, TileType.INPUT),
        new Tile(1, 2, TileType.SOLID),
        new Tile(2, 2, TileType.SOLID),
        new Tile(3, 2, TileType.SOLID),
        new Tile(4, 2, TileType.INPUT),
        new Tile(0, 3, TileType.INPUT),
        new Tile(1, 3, TileType.SOLID),
        new Tile(2, 3, TileType.SOLID),
        new Tile(3, 3, TileType.SOLID),
        new Tile(4, 3, TileType.INPUT),
        new Tile(0, 4, TileType.INPUT),
        new Tile(1, 4, TileType.INPUT),
        new Tile(2, 4, TileType.INPUT),
        new Tile(3, 4, TileType.INPUT),
        new Tile(4, 4, TileType.INPUT),
    };
  }
}
