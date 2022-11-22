package service;

import java.util.List;
import model.Deposit;
import model.FixedObject;
import model.Obstacle;
import model.ResourceType;
import model.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Fabian Moos
 */
class JsonInputTest {

  @Test
  void testConstructor001() {
    String testFile = "001.task.json";
    Input input = new JsonInput(testFile);

    assertEquals(input.getWidth(), 30);
    assertEquals(input.getHeight(), 20);
    assertEquals(input.getTurns(), 50);
    assertEquals(input.getTime(), -1);

    List<FixedObject> actualObjects = input.getInputObjects();
    FixedObject[] expectedObjects = {
        Deposit.createDeposit(ResourceType.ZERO, 1, 1, 5, 5),
        Deposit.createDeposit(ResourceType.ONE, 1, 14, 5, 5),
        Deposit.createDeposit(ResourceType.TWO, 22, 1, 7, 7),
        Obstacle.createObstacle(11, 9, 19, 2),
        Obstacle.createObstacle(11, 1, 2, 8),
    };

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
        assertEquals(expectedTiles[i], actualTiles[i]);
      }

      if (actualObjects.get(i).getClass().equals(Deposit.class)) {
        Deposit actualDeposit = (Deposit) actualObject;
        Deposit expectedDeposit = (Deposit) expectedObject;
        assertEquals(actualDeposit.getResourceType(), expectedDeposit.getResourceType());
      }
    }
  }

  @Test
  void testConstructor002() {
    String testFile = "002.task.json";
  }

  @Test
  void getWidth() {
  }

  @Test
  void getHeight() {
  }

  @Test
  void getInputObjects() {
  }

  @Test
  void getProducts() {
  }

  @Test
  void getTurns() {
  }

  @Test
  void getTime() {
  }
}