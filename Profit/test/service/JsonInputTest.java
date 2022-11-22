package service;

import java.util.HashMap;
import java.util.List;

import model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Fabian Moos
 */
class JsonInputTest {

  @Test
  void testConstructor001() {
    String testFile = "001.task.json";
    InputFile input = JsonInput.createInputFromFile(testFile);

    FixedObject[] expectedObjects = {
            Deposit.createDeposit(ResourceType.ZERO, 1, 1, 5, 5),
            Deposit.createDeposit(ResourceType.ONE, 1, 14, 5, 5),
            Deposit.createDeposit(ResourceType.TWO, 22, 1, 7, 7),
            Obstacle.createObstacle(11, 9, 19, 2),
            Obstacle.createObstacle(11, 1, 2, 8),
    };

    HashMap<ResourceType, Integer> requirementsProduct0 = new HashMap<>();
    requirementsProduct0.put(ResourceType.ZERO, 3);
    requirementsProduct0.put(ResourceType.ONE, 3);
    requirementsProduct0.put(ResourceType.TWO, 3);
    requirementsProduct0.put(ResourceType.THREE, 0);
    requirementsProduct0.put(ResourceType.FOUR, 0);
    requirementsProduct0.put(ResourceType.FIVE, 0);
    requirementsProduct0.put(ResourceType.SIX, 0);
    requirementsProduct0.put(ResourceType.SEVEN, 0);

    Product[] requestedProducts = {
            new Product(10, ProductType.ZERO, requirementsProduct0)
    };

    checkInputForCorrectness(input, expectedObjects, requestedProducts,
            30, 20, 50, -1);
  }

  @Test
  void testConstructor002() {
    String testFile = "002.task.json";
    InputFile input = JsonInput.createInputFromFile(testFile);

    FixedObject[] expectedObjects = {
            Deposit.createDeposit(ResourceType.ZERO, 0, 0, 5, 5),
            Obstacle.createObstacle(5, 2, 16, 1)
    };

    HashMap<ResourceType, Integer> requirementProduct0 = new HashMap<>();
    requirementProduct0.put(ResourceType.ZERO, 10);
    requirementProduct0.put(ResourceType.ONE, 0);
    requirementProduct0.put(ResourceType.TWO, 0);
    requirementProduct0.put(ResourceType.THREE, 0);
    requirementProduct0.put(ResourceType.FOUR, 0);
    requirementProduct0.put(ResourceType.FIVE, 0);
    requirementProduct0.put(ResourceType.SIX, 0);
    requirementProduct0.put(ResourceType.SEVEN, 0);

    Product[] requestedProducts = {
            new Product(10, ProductType.ZERO, requirementProduct0)
    };

    checkInputForCorrectness(input, expectedObjects, requestedProducts,
            26, 5, 20, -1);
  }

  @Test
  void testConstructor003() {
    String testFile = "003.task.json";
    InputFile input = JsonInput.createInputFromFile(testFile);

    FixedObject[] expectedObjects = {
            Deposit.createDeposit(ResourceType.ZERO, 1, 1, 7, 7),
            Deposit.createDeposit(ResourceType.ONE, 36, 36, 3 ,3)
    };

    HashMap<ResourceType, Integer> requirementProduct0 = new HashMap<>();
    requirementProduct0.put(ResourceType.ZERO, 36);
    requirementProduct0.put(ResourceType.ONE, 3);
    requirementProduct0.put(ResourceType.TWO, 0);
    requirementProduct0.put(ResourceType.THREE, 0);
    requirementProduct0.put(ResourceType.FOUR, 0);
    requirementProduct0.put(ResourceType.FIVE, 0);
    requirementProduct0.put(ResourceType.SIX, 0);
    requirementProduct0.put(ResourceType.SEVEN, 0);

    Product[] requestedProducts = {
            new Product(10, ProductType.ZERO, requirementProduct0)
    };

    checkInputForCorrectness(input, expectedObjects, requestedProducts,
            40, 40, 14, -1);
  }

  @Test
  void testConstructor004() {
    String testFile = "004.task.json";
    InputFile input = JsonInput.createInputFromFile(testFile);

    FixedObject[] expectedObjects = {
            Obstacle.createObstacle(8, 0, 4, 11),
            Obstacle.createObstacle(8, 12, 4, 11),
            Deposit.createDeposit(ResourceType.ZERO, 0, 0, 8, 9),
            Deposit.createDeposit(ResourceType.ONE, 0, 14, 8 ,9),
            Deposit.createDeposit(ResourceType.TWO, 21, 0, 8, 9),
            Deposit.createDeposit(ResourceType.THREE, 21, 14, 8, 9),
            Obstacle.createObstacle(17,0,4,11),
            Obstacle.createObstacle(17, 12, 4, 11),
            Obstacle.createObstacle(14, 10, 1, 3),
            Obstacle.createObstacle(12, 0, 5, 1),
            Obstacle.createObstacle(12, 22, 5, 1)
    };

    HashMap<ResourceType, Integer> requirementProduct0 = new HashMap<>();
    requirementProduct0.put(ResourceType.ZERO, 10);
    requirementProduct0.put(ResourceType.ONE, 10);
    requirementProduct0.put(ResourceType.TWO, 0);
    requirementProduct0.put(ResourceType.THREE, 0);
    requirementProduct0.put(ResourceType.FOUR, 0);
    requirementProduct0.put(ResourceType.FIVE, 0);
    requirementProduct0.put(ResourceType.SIX, 0);
    requirementProduct0.put(ResourceType.SEVEN, 0);

    HashMap<ResourceType, Integer> requirementProduct1 = new HashMap<>();
    requirementProduct1.put(ResourceType.ZERO, 0);
    requirementProduct1.put(ResourceType.ONE, 0);
    requirementProduct1.put(ResourceType.TWO, 10);
    requirementProduct1.put(ResourceType.THREE, 10);
    requirementProduct1.put(ResourceType.FOUR, 0);
    requirementProduct1.put(ResourceType.FIVE, 0);
    requirementProduct1.put(ResourceType.SIX, 0);
    requirementProduct1.put(ResourceType.SEVEN, 0);

    Product[] requestedProducts = {
            new Product(10, ProductType.ZERO, requirementProduct0),
            new Product(10, ProductType.ONE, requirementProduct1)
    };

    checkInputForCorrectness(input, expectedObjects, requestedProducts,
            29, 23, 50, -1);
  }

  private void checkInputForCorrectness(InputFile input, FixedObject[] expectedObjects,
                                        Product[] expectedProducts, int expectedWidth,
                                        int expectedHeight, int expectedTurns, int expectedTime) {
    assertEquals(input.getWidth(), expectedWidth);
    assertEquals(input.getHeight(), expectedHeight);
    assertEquals(input.getTurns(), expectedTurns);
    assertEquals(input.getTime(), expectedTime);
    List<FixedObject> actualObjects = input.getInputObjects();

    assertNotNull(actualObjects);
    assertEquals(actualObjects.size(), expectedObjects.length);

    for (int i = 0; i < expectedObjects.length; ++i) {
      FixedObject actualObject = actualObjects.get(i);
      FixedObject expectedObject = expectedObjects[i];

      assertEquals(expectedObject.getX(), actualObject.getX());
      assertEquals(expectedObject.getY(), actualObject.getY());

      Tile[] actualTiles = actualObject.getTiles();
      Tile[] expectedTiles = expectedObject.getTiles();

      assertEquals(expectedTiles.length, actualTiles.length);

      for (int j = 0; j < expectedTiles.length; ++j) {
        assertEquals(expectedTiles[j], actualTiles[j]);
      }

      if (actualObjects.get(i).getClass().equals(Deposit.class)) {
        Deposit actualDeposit = (Deposit) actualObject;
        Deposit expectedDeposit = (Deposit) expectedObject;
        assertEquals(actualDeposit.getResourceType(), expectedDeposit.getResourceType());
      }
    }

    List<Product> actualProducts = input.getProducts();

    assertNotNull(actualProducts);
    assertEquals(expectedProducts.length, actualProducts.size());

    for (int i = 0; i < actualProducts.size(); ++i) {
      Product actualProduct = actualProducts.get(i);
      Product expectedProduct = expectedProducts[i];
      assertTrue(expectedProduct.equals(actualProduct));
    }
  }

}