package de.unimarburg.profit.service;

import de.unimarburg.profit.model.Combiner;
import de.unimarburg.profit.model.Conveyer;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.MovableObject;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.enums.CombinerSubType;
import de.unimarburg.profit.model.enums.ConveyerSubType;
import de.unimarburg.profit.model.enums.MineSubType;
import de.unimarburg.profit.model.enums.ProductType;
import de.unimarburg.profit.service.InputOutputHandle.FileType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Fabian Moos
 */
class JsonOutputTest {

  @Test
  void testOutputToStdOut1() {
    this.setSettingsForStdOutTests();

    var product0 = new Product(Integer.MAX_VALUE, ProductType.ZERO, new HashMap<>());

    var outputObjects = new ArrayList<MovableObject>();
    outputObjects.add(Mine.createMine(3, 7, MineSubType.OUTPUT_SOUTH));
    outputObjects.add(Mine.createMine(7, 14, MineSubType.OUTPUT_EAST));
    outputObjects.add(Mine.createMine(19, 4, MineSubType.OUTPUT_WEST));
    outputObjects.add(Conveyer.createConveyor(18, 1, ConveyerSubType.LONG_OUTPUT_NORTH));
    outputObjects.add(Conveyer.createConveyor(15, 0, ConveyerSubType.LONG_OUTPUT_WEST));
    outputObjects.add(Conveyer.createConveyor(11, 0, ConveyerSubType.LONG_OUTPUT_WEST));
    outputObjects.add(Conveyer.createConveyor(10, 2, ConveyerSubType.LONG_OUTPUT_SOUTH));
    outputObjects.add(Conveyer.createConveyor(10, 6, ConveyerSubType.LONG_OUTPUT_SOUTH));
    outputObjects.add(Conveyer.createConveyor(10, 10, ConveyerSubType.LONG_OUTPUT_SOUTH));
    outputObjects.add(Combiner.createCombiner(11, 14, CombinerSubType.OUTPUT_EAST));
    outputObjects.add(Conveyer.createConveyor(5, 9, ConveyerSubType.SHORT_OUTPUT_EAST));
    outputObjects.add(Conveyer.createConveyor(6, 11, ConveyerSubType.LONG_OUTPUT_SOUTH));
    outputObjects.add(Conveyer.createConveyor(8, 13, ConveyerSubType.SHORT_OUTPUT_EAST));
    outputObjects.add(Factory.createFactory(13, 12, product0));

    String expectedOutput = "["
        + "{\"type\":\"mine\",\"x\":3,\"y\":7,\"subtype\":1},"
        + "{\"type\":\"mine\",\"x\":7,\"y\":14,\"subtype\":0},"
        + "{\"type\":\"mine\",\"x\":19,\"y\":4,\"subtype\":2},"
        + "{\"type\":\"conveyor\",\"x\":18,\"y\":1,\"subtype\":7},"
        + "{\"type\":\"conveyor\",\"x\":15,\"y\":0,\"subtype\":6},"
        + "{\"type\":\"conveyor\",\"x\":11,\"y\":0,\"subtype\":6},"
        + "{\"type\":\"conveyor\",\"x\":10,\"y\":2,\"subtype\":5},"
        + "{\"type\":\"conveyor\",\"x\":10,\"y\":6,\"subtype\":5},"
        + "{\"type\":\"conveyor\",\"x\":10,\"y\":10,\"subtype\":5},"
        + "{\"type\":\"combiner\",\"x\":11,\"y\":14,\"subtype\":0},"
        + "{\"type\":\"conveyor\",\"x\":5,\"y\":9,\"subtype\":0},"
        + "{\"type\":\"conveyor\",\"x\":6,\"y\":11,\"subtype\":5},"
        + "{\"type\":\"conveyor\",\"x\":8,\"y\":13,\"subtype\":0},"
        + "{\"type\":\"factory\",\"x\":13,\"y\":12,\"subtype\":0}"
        + "]";

    String outputString = Json.generateOutputString(outputObjects);

    assertEquals(expectedOutput, outputString);
  }

  @Test
  void testOutputToStdOut2() {
    this.setSettingsForStdOutTests();

    var outputObjects = new ArrayList<MovableObject>();

    String outputString = Json.generateOutputString(outputObjects);

    String expectedOutput = "[]";
    assertEquals(expectedOutput, outputString);
  }

  @Test
  void testOutputToStdOut3() {
    this.setSettingsForStdOutTests();

    var product0 = new Product(Integer.MAX_VALUE, ProductType.ZERO, new HashMap<>());
    var product1 = new Product(Integer.MAX_VALUE, ProductType.ONE, new HashMap<>());
    var product2 = new Product(Integer.MAX_VALUE, ProductType.TWO, new HashMap<>());
    var product3 = new Product(Integer.MAX_VALUE, ProductType.THREE, new HashMap<>());
    var product4 = new Product(Integer.MAX_VALUE, ProductType.FOUR, new HashMap<>());
    var product5 = new Product(Integer.MAX_VALUE, ProductType.FIVE, new HashMap<>());
    var product6 = new Product(Integer.MAX_VALUE, ProductType.SIX, new HashMap<>());
    var product7 = new Product(Integer.MAX_VALUE, ProductType.SEVEN, new HashMap<>());

    var outputObjects = new ArrayList<MovableObject>();
    outputObjects.add(Mine.createMine(7, 13, MineSubType.OUTPUT_EAST));
    outputObjects.add(Mine.createMine(13, 7, MineSubType.OUTPUT_WEST));
    outputObjects.add(Mine.createMine(89, 5, MineSubType.OUTPUT_SOUTH));
    outputObjects.add(Mine.createMine(26, 77, MineSubType.OUTPUT_NORTH));
    outputObjects.add(Factory.createFactory(0, 0, product0));
    outputObjects.add(Factory.createFactory(30, 30, product1));
    outputObjects.add(Conveyer.createConveyor(25, 52, ConveyerSubType.SHORT_OUTPUT_EAST));
    outputObjects.add(Conveyer.createConveyor(59, 69, ConveyerSubType.SHORT_OUTPUT_SOUTH));
    outputObjects.add(Conveyer.createConveyor(32, 11, ConveyerSubType.SHORT_OUTPUT_WEST));
    outputObjects.add(Conveyer.createConveyor(96, 74, ConveyerSubType.SHORT_OUTPUT_NORTH));
    outputObjects.add(Conveyer.createConveyor(50, 54, ConveyerSubType.LONG_OUTPUT_EAST));
    outputObjects.add(Conveyer.createConveyor(45, 3, ConveyerSubType.LONG_OUTPUT_SOUTH));
    outputObjects.add(Combiner.createCombiner(46, 7, CombinerSubType.OUTPUT_EAST));
    outputObjects.add(Combiner.createCombiner(49, 7, CombinerSubType.OUTPUT_SOUTH));
    outputObjects.add(Combiner.createCombiner(52, 7, CombinerSubType.OUTPUT_WEST));
    outputObjects.add(Conveyer.createConveyor(13, 84, ConveyerSubType.LONG_OUTPUT_NORTH));
    outputObjects.add(Conveyer.createConveyor(14, 75, ConveyerSubType.LONG_OUTPUT_SOUTH));
    outputObjects.add(Conveyer.createConveyor(13, 42, ConveyerSubType.LONG_OUTPUT_WEST));
    outputObjects.add(Conveyer.createConveyor(91, 13, ConveyerSubType.SHORT_OUTPUT_WEST));
    outputObjects.add(Combiner.createCombiner(67, 34, CombinerSubType.OUTPUT_NORTH));
    outputObjects.add(Combiner.createCombiner(14, 55, CombinerSubType.OUTPUT_NORTH));
    outputObjects.add(Conveyer.createConveyor(33, 98, ConveyerSubType.SHORT_OUTPUT_EAST));
    outputObjects.add(Conveyer.createConveyor(54, 22, ConveyerSubType.SHORT_OUTPUT_NORTH));
    outputObjects.add(Factory.createFactory(13, 15, product0));
    outputObjects.add(Factory.createFactory(77, 77, product2));
    outputObjects.add(Factory.createFactory(90, 10, product2));
    outputObjects.add(Factory.createFactory(5, 30, product3));
    outputObjects.add(Factory.createFactory(90, 30, product4));
    outputObjects.add(Factory.createFactory(5, 40, product5));
    outputObjects.add(Factory.createFactory(90, 50, product6));
    outputObjects.add(Factory.createFactory(5, 14, product7));

    String expectedOutput = "["
        + "{\"type\":\"mine\",\"x\":7,\"y\":13,\"subtype\":0},"
        + "{\"type\":\"mine\",\"x\":13,\"y\":7,\"subtype\":2},"
        + "{\"type\":\"mine\",\"x\":89,\"y\":5,\"subtype\":1},"
        + "{\"type\":\"mine\",\"x\":26,\"y\":77,\"subtype\":3},"
        + "{\"type\":\"factory\",\"x\":0,\"y\":0,\"subtype\":0},"
        + "{\"type\":\"factory\",\"x\":30,\"y\":30,\"subtype\":1},"
        + "{\"type\":\"conveyor\",\"x\":25,\"y\":52,\"subtype\":0},"
        + "{\"type\":\"conveyor\",\"x\":59,\"y\":69,\"subtype\":1},"
        + "{\"type\":\"conveyor\",\"x\":32,\"y\":11,\"subtype\":2},"
        + "{\"type\":\"conveyor\",\"x\":96,\"y\":74,\"subtype\":3},"
        + "{\"type\":\"conveyor\",\"x\":50,\"y\":54,\"subtype\":4},"
        + "{\"type\":\"conveyor\",\"x\":45,\"y\":3,\"subtype\":5},"
        + "{\"type\":\"combiner\",\"x\":46,\"y\":7,\"subtype\":0},"
        + "{\"type\":\"combiner\",\"x\":49,\"y\":7,\"subtype\":1},"
        + "{\"type\":\"combiner\",\"x\":52,\"y\":7,\"subtype\":2},"
        + "{\"type\":\"conveyor\",\"x\":13,\"y\":84,\"subtype\":7},"
        + "{\"type\":\"conveyor\",\"x\":14,\"y\":75,\"subtype\":5},"
        + "{\"type\":\"conveyor\",\"x\":13,\"y\":42,\"subtype\":6},"
        + "{\"type\":\"conveyor\",\"x\":91,\"y\":13,\"subtype\":2},"
        + "{\"type\":\"combiner\",\"x\":67,\"y\":34,\"subtype\":3},"
        + "{\"type\":\"combiner\",\"x\":14,\"y\":55,\"subtype\":3},"
        + "{\"type\":\"conveyor\",\"x\":33,\"y\":98,\"subtype\":0},"
        + "{\"type\":\"conveyor\",\"x\":54,\"y\":22,\"subtype\":3},"
        + "{\"type\":\"factory\",\"x\":13,\"y\":15,\"subtype\":0},"
        + "{\"type\":\"factory\",\"x\":77,\"y\":77,\"subtype\":2},"
        + "{\"type\":\"factory\",\"x\":90,\"y\":10,\"subtype\":2},"
        + "{\"type\":\"factory\",\"x\":5,\"y\":30,\"subtype\":3},"
        + "{\"type\":\"factory\",\"x\":90,\"y\":30,\"subtype\":4},"
        + "{\"type\":\"factory\",\"x\":5,\"y\":40,\"subtype\":5},"
        + "{\"type\":\"factory\",\"x\":90,\"y\":50,\"subtype\":6},"
        + "{\"type\":\"factory\",\"x\":5,\"y\":14,\"subtype\":7}"
        + "]";

    String outputString = Json.generateOutputString(outputObjects);

    assertEquals(expectedOutput, outputString);
  }

  @Test
  void testOutputToFile1() {
    this.setSettingsForFileOutTests();

    InputOutputHandle.writeOutObjects(new ArrayList<>());

    String expectedInput = "[]";

    checkFileInput(expectedInput);
  }

  @Test
  void testOutputToFile2() {
    this.setSettingsForFileOutTests();

    var product0 = new Product(Integer.MAX_VALUE, ProductType.ZERO, new HashMap<>());

    var outputObjects = new ArrayList<MovableObject>();
    outputObjects.add(Mine.createMine(3, 7, MineSubType.OUTPUT_SOUTH));
    outputObjects.add(Mine.createMine(7, 14, MineSubType.OUTPUT_EAST));
    outputObjects.add(Mine.createMine(19, 4, MineSubType.OUTPUT_WEST));
    outputObjects.add(Conveyer.createConveyor(18, 1, ConveyerSubType.LONG_OUTPUT_NORTH));
    outputObjects.add(Conveyer.createConveyor(15, 0, ConveyerSubType.LONG_OUTPUT_WEST));
    outputObjects.add(Conveyer.createConveyor(11, 0, ConveyerSubType.LONG_OUTPUT_WEST));
    outputObjects.add(Conveyer.createConveyor(10, 2, ConveyerSubType.LONG_OUTPUT_SOUTH));
    outputObjects.add(Conveyer.createConveyor(10, 6, ConveyerSubType.LONG_OUTPUT_SOUTH));
    outputObjects.add(Conveyer.createConveyor(10, 10, ConveyerSubType.LONG_OUTPUT_SOUTH));
    outputObjects.add(Combiner.createCombiner(11, 14, CombinerSubType.OUTPUT_EAST));
    outputObjects.add(Conveyer.createConveyor(5, 9, ConveyerSubType.SHORT_OUTPUT_EAST));
    outputObjects.add(Conveyer.createConveyor(6, 11, ConveyerSubType.LONG_OUTPUT_SOUTH));
    outputObjects.add(Conveyer.createConveyor(8, 13, ConveyerSubType.SHORT_OUTPUT_EAST));
    outputObjects.add(Factory.createFactory(13, 12, product0));

    String expectedInput = "["
        + "{\"type\":\"mine\",\"x\":3,\"y\":7,\"subtype\":1},"
        + "{\"type\":\"mine\",\"x\":7,\"y\":14,\"subtype\":0},"
        + "{\"type\":\"mine\",\"x\":19,\"y\":4,\"subtype\":2},"
        + "{\"type\":\"conveyor\",\"x\":18,\"y\":1,\"subtype\":7},"
        + "{\"type\":\"conveyor\",\"x\":15,\"y\":0,\"subtype\":6},"
        + "{\"type\":\"conveyor\",\"x\":11,\"y\":0,\"subtype\":6},"
        + "{\"type\":\"conveyor\",\"x\":10,\"y\":2,\"subtype\":5},"
        + "{\"type\":\"conveyor\",\"x\":10,\"y\":6,\"subtype\":5},"
        + "{\"type\":\"conveyor\",\"x\":10,\"y\":10,\"subtype\":5},"
        + "{\"type\":\"combiner\",\"x\":11,\"y\":14,\"subtype\":0},"
        + "{\"type\":\"conveyor\",\"x\":5,\"y\":9,\"subtype\":0},"
        + "{\"type\":\"conveyor\",\"x\":6,\"y\":11,\"subtype\":5},"
        + "{\"type\":\"conveyor\",\"x\":8,\"y\":13,\"subtype\":0},"
        + "{\"type\":\"factory\",\"x\":13,\"y\":12,\"subtype\":0}"
        + "]";

    InputOutputHandle.writeOutObjects(outputObjects);

    checkFileInput(expectedInput);
  }

  private void checkFileInput(String expectedInput) {
    var settings = Settings.getInstance();

    try {
      BufferedReader reader = new BufferedReader(new FileReader(settings.getExportFileName()));
      assertEquals(expectedInput, reader.readLine());
      assertNull(reader.readLine());
      reader.close();
    } catch (IOException e) {
      System.err.println(e.getMessage());
      System.err.println(Arrays.toString(e.getStackTrace()));
    }

    File outputFile = new File(settings.getExportFileName());
    assertTrue(outputFile.delete());
  }

  private void setSettingsForStdOutTests() {
    var settings = Settings.getInstance();
    settings.updateExportFileType(FileType.JSON);
    settings.updateExportTarget(true);
  }

  private void setSettingsForFileOutTests() {
    var settings = Settings.getInstance();
    settings.updateExportFileType(FileType.JSON);
    settings.updateExportTarget(false);
  }
}
