package model;

import java.util.HashMap;
import java.util.Map;
import model.enums.ResourceType;
import model.enums.TileType;

/**
 * This class models {@link Deposit}.
 *
 * @author Yevheniia Makara
 */
public class Deposit extends FixedObject {

  private ResourceType resourceType;

  /**
   * Constructor of Deposit.
   *
   * @param horPos  X-Coordinate of the {@link Deposit}.
   * @param verPos  Y-Coordinate of the {@link Deposit}.
   * @param tiles   Tiles, that construct the {@link Deposit}.
   * @param height  height of the {@link Deposit}.
   * @param width   width of the {@link Deposit}.
   * @param subtype is the type of resources of the {@link Deposit}.
   */
  private Deposit(ResourceType subtype, int horPos, int verPos, int width, int height,
      Tile[] tiles) {
    super(horPos, verPos, tiles, width, height);
    resourceType = subtype;
  }

  /**
   * Creates a new instance of {@link Deposit}.
   *
   * @param subtype Subtype of the {@link Deposit}.
   * @param horPos  X-Position of the {@link Deposit}.
   * @param verPos  Y-Position of the {@link Deposit}.
   * @param width   Width of the {@link Deposit}.
   * @param height  Height of the {@link Deposit}.
   * @return New instance of {@link Deposit}.
   */
  public static Deposit createDeposit(ResourceType subtype, int horPos, int verPos, int width,
      int height) {

    Tile[] newTiles = new Tile[width * height];
    int x = 0; // relative Position (0, 0) ist linke obere Ecke
    int y = 0;
    for (int i = 0; i < newTiles.length; i++) {

      if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
        newTiles[i] = new Tile(x, y, TileType.DEPOSIT_OUTPUT);
      } else {
        newTiles[i] = new Tile(x, y, TileType.SOLID);
      }

      if (x == width - 1) {
        x = 0;
        y++;
      } else {
        x++;
      }
    }

    return new Deposit(subtype, horPos, verPos, width, height, newTiles);
  }

  public ResourceType getResourceType() {
    return resourceType;
  }

  @Override
  public Map<ResourceType, Integer> getStartResources() {
    Map<ResourceType, Integer> resources = new HashMap<>();
    int startAmount = getHeight() * getWidth() * 5;
    resources.put(resourceType, startAmount);
    return resources;
  }

  @Override
  public Map<ResourceType, Integer> getResourcesToOutput(
      Map<ResourceType, Integer> storedResources) {

    if (!storedResources.containsKey(resourceType)) {
      return new HashMap<>();
    }

    Map<ResourceType, Integer> resourcesToOutput = new HashMap<>();
    resourcesToOutput.put(resourceType, Math.min(storedResources.get(resourceType), 3));

    return resourcesToOutput;
  }
}
