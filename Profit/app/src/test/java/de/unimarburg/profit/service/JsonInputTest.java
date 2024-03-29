package de.unimarburg.profit.service;

import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.FixedObject;
import de.unimarburg.profit.model.Obstacle;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.Tile;
import de.unimarburg.profit.model.enums.ProductType;
import de.unimarburg.profit.model.enums.ResourceType;
import de.unimarburg.profit.service.InputOutputHandle.FileType;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO: Tests for reading from StdIn.
 * @author Fabian Moos
 */
class JsonInputTest {

  @BeforeAll
  static void setSettings() {
    var settings = Settings.getInstance();
    settings.updateImportFileType(FileType.JSON);
    settings.updateImportTarget(false);
  }

  @Test
  void testExampleTask001() {
    String testFile = "../input/001-task.json";
    assertThrows(InputOutputException.class, () -> InputOutputHandle.readInputFrom(testFile));
  }

  @Test
  void testExampleTask001WithTime() throws InputOutputException {
    String testFile = "001-task-with-time.json";
    Input input = InputOutputHandle.readInputFrom(testFile);

    assertNotNull(input);

    FixedObject[] expectedObjects = {
        Deposit.createDeposit(ResourceType.ZERO, 1, 1, 5, 5),
        Deposit.createDeposit(ResourceType.ONE, 1, 14, 5, 5),
        Deposit.createDeposit(ResourceType.TWO, 22, 1, 7, 7),
        Obstacle.createObstacle(11, 9, 19, 2),
        Obstacle.createObstacle(11, 1, 2, 8)
    };

    HashMap<ResourceType, Integer> requirementsForProduct0 = new HashMap<>();
    requirementsForProduct0.put(ResourceType.ZERO, 3);
    requirementsForProduct0.put(ResourceType.ONE, 3);
    requirementsForProduct0.put(ResourceType.TWO, 3);
    requirementsForProduct0.put(ResourceType.THREE, 0);
    requirementsForProduct0.put(ResourceType.FOUR, 0);
    requirementsForProduct0.put(ResourceType.FIVE, 0);
    requirementsForProduct0.put(ResourceType.SIX, 0);
    requirementsForProduct0.put(ResourceType.SEVEN, 0);

    Product[] requestedProducts = {
        new Product(10, ProductType.ZERO, requirementsForProduct0),
    };

    checkInputForCorrectness(input, expectedObjects, requestedProducts,
        30, 20, 50, 300);
  }

  @Test
  void testExampleTask002() {
    String testFile = "002-task.json";
    assertThrows(InputOutputException.class, () -> InputOutputHandle.readInputFrom(testFile));
  }

  @Test
  void testExampleTask002WithTime() throws InputOutputException {
    String testFile = "002-task-with-time.json";
    Input input = InputOutputHandle.readInputFrom(testFile);

    assertNotNull(input);

    FixedObject[] expectedObjects = {
        Deposit.createDeposit(ResourceType.ZERO, 0, 0, 5, 5),
        Obstacle.createObstacle(5, 2, 16, 1),
    };

    HashMap<ResourceType, Integer> requirementsForProduct0 = new HashMap<>();
    requirementsForProduct0.put(ResourceType.ZERO, 10);
    requirementsForProduct0.put(ResourceType.ONE, 0);
    requirementsForProduct0.put(ResourceType.TWO, 0);
    requirementsForProduct0.put(ResourceType.THREE, 0);
    requirementsForProduct0.put(ResourceType.FOUR, 0);
    requirementsForProduct0.put(ResourceType.FIVE, 0);
    requirementsForProduct0.put(ResourceType.SIX, 0);
    requirementsForProduct0.put(ResourceType.SEVEN, 0);

    Product[] requestedProducts = {
        new Product(10, ProductType.ZERO, requirementsForProduct0),
    };

    checkInputForCorrectness(input, expectedObjects, requestedProducts,
        26, 5, 20, 60);
  }

  @Test
  void testExampleTask003() {
    String testFile = "003-task.json";
    assertThrows(InputOutputException.class, () -> InputOutputHandle.readInputFrom(testFile));
  }

  @Test
  void testExampleTask003WithTime() throws InputOutputException {
    String testFile = "003-task-with-time.json";
    Input input = InputOutputHandle.readInputFrom(testFile);

    assertNotNull(input);

    FixedObject[] expectedObjects = {
        Deposit.createDeposit(ResourceType.ZERO, 1, 1, 7, 7),
        Deposit.createDeposit(ResourceType.ONE, 36, 36, 3, 3),
    };

    HashMap<ResourceType, Integer> requirementsForProduct0 = new HashMap<>();
    requirementsForProduct0.put(ResourceType.ZERO, 36);
    requirementsForProduct0.put(ResourceType.ONE, 3);
    requirementsForProduct0.put(ResourceType.TWO, 0);
    requirementsForProduct0.put(ResourceType.THREE, 0);
    requirementsForProduct0.put(ResourceType.FOUR, 0);
    requirementsForProduct0.put(ResourceType.FIVE, 0);
    requirementsForProduct0.put(ResourceType.SIX, 0);
    requirementsForProduct0.put(ResourceType.SEVEN, 0);

    Product[] requestedProducts = {
        new Product(10, ProductType.ZERO, requirementsForProduct0),
    };

    checkInputForCorrectness(input, expectedObjects, requestedProducts,
        40, 40, 14, 300);
  }

  @Test
  void testExampleTask004() {
    String testFile = "004-task.json";
    assertThrows(InputOutputException.class, () -> InputOutputHandle.readInputFrom(testFile));
  }

  @Test
  void testExampleTask004WithTime() throws InputOutputException {
    String testFile = "004-task-with-time.json";
    Input input = InputOutputHandle.readInputFrom(testFile);

    assertNotNull(input);

    FixedObject[] expectedObjects = {
        Obstacle.createObstacle(8, 0, 4, 11),
        Obstacle.createObstacle(8, 12, 4, 11),
        Deposit.createDeposit(ResourceType.ZERO, 0, 0, 8, 9),
        Deposit.createDeposit(ResourceType.ONE, 0, 14, 8, 9),
        Deposit.createDeposit(ResourceType.TWO, 21, 0, 8, 9),
        Deposit.createDeposit(ResourceType.THREE, 21, 14, 8, 9),
        Obstacle.createObstacle(17, 0, 4, 11),
        Obstacle.createObstacle(17, 12, 4, 11),
        Obstacle.createObstacle(14, 10, 1, 3),
        Obstacle.createObstacle(12, 0, 5, 1),
        Obstacle.createObstacle(12, 22, 5, 1)
    };

    HashMap<ResourceType, Integer> requirementsForProduct0 = new HashMap<>();
    requirementsForProduct0.put(ResourceType.ZERO, 10);
    requirementsForProduct0.put(ResourceType.ONE, 10);
    requirementsForProduct0.put(ResourceType.TWO, 0);
    requirementsForProduct0.put(ResourceType.THREE, 0);
    requirementsForProduct0.put(ResourceType.FOUR, 0);
    requirementsForProduct0.put(ResourceType.FIVE, 0);
    requirementsForProduct0.put(ResourceType.SIX, 0);
    requirementsForProduct0.put(ResourceType.SEVEN, 0);

    HashMap<ResourceType, Integer> requirementsForProduct1 = new HashMap<>();
    requirementsForProduct1.put(ResourceType.ZERO, 0);
    requirementsForProduct1.put(ResourceType.ONE, 0);
    requirementsForProduct1.put(ResourceType.TWO, 10);
    requirementsForProduct1.put(ResourceType.THREE, 10);
    requirementsForProduct1.put(ResourceType.FOUR, 0);
    requirementsForProduct1.put(ResourceType.FIVE, 0);
    requirementsForProduct1.put(ResourceType.SIX, 0);
    requirementsForProduct1.put(ResourceType.SEVEN, 0);

    Product[] requestedProducts = {
        new Product(10, ProductType.ZERO, requirementsForProduct0),
        new Product(10, ProductType.ONE, requirementsForProduct1)
    };

    checkInputForCorrectness(input, expectedObjects, requestedProducts,
        29, 23, 50, 120);
  }

  private void checkInputForCorrectness(Input input, FixedObject[] expectedObjects,
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
        Assertions.assertEquals(actualDeposit.getResourceType(), expectedDeposit.getResourceType());
      }
    }

    Collection<Product> actualProducts = input.getProducts();

    assertNotNull(actualProducts);
    assertEquals(expectedProducts.length, actualProducts.size());

    for (Product product : expectedProducts) {
      assertTrue(actualProducts.contains(product));
    }

  }

}