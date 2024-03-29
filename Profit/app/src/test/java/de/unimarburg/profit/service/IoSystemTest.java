package de.unimarburg.profit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.unimarburg.profit.algorithm.Algorithm;
import de.unimarburg.profit.algorithm.AlgorithmImpl;
import de.unimarburg.profit.algorithm.factoryplacing.combination.CombinationFinderImpl;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryChooserRandom;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryPlaceFinderImpl;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryPlacerImpl;
import de.unimarburg.profit.algorithm.mineplacing.MinePlaceChooserImpl;
import de.unimarburg.profit.algorithm.mineplacing.MinePlaceFinderImpl;
import de.unimarburg.profit.algorithm.mineplacing.MinePlacerImpl;
import de.unimarburg.profit.controller.ControllerImpl;
import de.unimarburg.profit.controller.Controller;
import de.unimarburg.profit.model.MovableObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class IoSystemTest {

  private static Controller controller;


  @BeforeAll
  public static void setUp() {
    Algorithm algorithm = new AlgorithmImpl(
        new MinePlaceFinderImpl(),
        new MinePlaceChooserImpl(),
        new MinePlacerImpl(),
        new FactoryPlaceFinderImpl(),
        new FactoryChooserRandom(),
        new FactoryPlacerImpl(),
        new CombinationFinderImpl());
    controller = input -> new ControllerImpl(algorithm).startAlgorithm(input);
  }

  @Test
  public void test1() {

    String inputText = "ShouldThrowError\nShouldThrowError\n\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(
        inputText.getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    IoSystem ioSystem = new IoSystem(inputStream, outputStream, controller);
    ioSystem.start();

    String outputText = outputStream.toString(StandardCharsets.UTF_8);
    String[] outputLines = outputText.split("\n");

    assertEquals(2, outputLines.length);
    Assertions.assertTrue(outputLines[0].startsWith("Could not read field."));
    Assertions.assertTrue(outputLines[1].startsWith("Could not read field."));

  }

  @Test
  public void testAlreadyStarted() {

    String inputText = "\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(
        inputText.getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    IoSystem ioSystem = new IoSystem(inputStream, outputStream, controller);
    ioSystem.start();

    Assertions.assertThrows(RuntimeException.class, ioSystem::start);

  }

  @Test
  @Timeout(value = 30)
  public void test2() {

    String inputText = "{\"width\":30,\"height\":20,\"objects\":[{\"type\":\"deposit\",\"x\":1,\"y\":1,\"subtype\":0,\"width\":5,\"height\":5},{\"type\":\"deposit\",\"x\":1,\"y\":14,\"subtype\":1,\"width\":5,\"height\":5},{\"type\":\"deposit\",\"x\":22,\"y\":1,\"subtype\":2,\"width\":7,\"height\":7},{\"type\":\"obstacle\",\"x\":11,\"y\":9,\"width\":19,\"height\":2},{\"type\":\"obstacle\",\"x\":11,\"y\":1,\"width\":2,\"height\":8}],\"products\":[{\"type\":\"product\",\"subtype\":0,\"resources\":[3,3,3,0,0,0,0,0],\"points\":10}],\"turns\":50,\"time\":30}";
    inputText += "\n\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(
        inputText.getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    IoSystem ioSystem = new IoSystem(inputStream, outputStream, controller);
    ioSystem.start();

    String outputText = outputStream.toString(StandardCharsets.UTF_8);
    String[] outputLines = outputText.split("\n");

    Assertions.assertTrue(outputLines.length != 0);
    Assertions.assertFalse(outputLines[0].contains("Could not read field."));

  }

  @Test
  @Timeout(value = 30)
  public void test3() {

    String inputText = "{\"width\":26,\"height\":5,\"objects\":[{\"type\":\"deposit\",\"x\":0,\"y\":0,\"subtype\":0,\"width\":5,\"height\":5},{\"type\":\"obstacle\",\"x\":5,\"y\":2,\"width\":16,\"height\":1}],\"products\":[{\"type\":\"product\",\"subtype\":0,\"resources\":[10,0,0,0,0,0,0,0],\"points\":10}],\"turns\":20,\"time\":30}";
    inputText += "\n\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(
        inputText.getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    IoSystem ioSystem = new IoSystem(inputStream, outputStream, controller);
    ioSystem.start();

    String outputText = outputStream.toString(StandardCharsets.UTF_8);
    String[] outputLines = outputText.split("\n");

    Assertions.assertTrue(outputLines.length != 0);
    Assertions.assertFalse(outputLines[0].contains("Could not read field."));
  }

  @Test
  @Timeout(value = 40)
  public void test4() {

    String inputText = "{\"width\":40,\"height\":40,\"objects\":[{\"type\":\"deposit\",\"x\":1,\"y\":1,\"subtype\":0,\"width\":7,\"height\":7},{\"type\":\"deposit\",\"x\":36,\"y\":36,\"subtype\":1,\"width\":3,\"height\":3}],\"products\":[{\"type\":\"product\",\"subtype\":0,\"resources\":[36,3,0,0,0,0,0,0],\"points\":10}],\"turns\":14,\"time\":40}";
    inputText += "\n\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(
        inputText.getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    IoSystem ioSystem = new IoSystem(inputStream, outputStream, controller);
    ioSystem.start();

    String outputText = outputStream.toString(StandardCharsets.UTF_8);
    String[] outputLines = outputText.split("\n");

    Assertions.assertTrue(outputLines.length != 0);
    Assertions.assertFalse(outputLines[0].contains("Could not read field."));
  }

  @Test
  @Timeout(value = 30)
  public void test5() {

    String inputText = "{\"width\":29,\"height\":23,\"objects\":[{\"type\":\"obstacle\",\"x\":8,\"y\":0,\"width\":4,\"height\":11},{\"type\":\"obstacle\",\"x\":8,\"y\":12,\"width\":4,\"height\":11},{\"type\":\"deposit\",\"x\":0,\"y\":0,\"subtype\":0,\"width\":8,\"height\":9},{\"type\":\"deposit\",\"x\":0,\"y\":14,\"subtype\":1,\"width\":8,\"height\":9},{\"type\":\"deposit\",\"x\":21,\"y\":0,\"subtype\":2,\"width\":8,\"height\":9},{\"type\":\"deposit\",\"x\":21,\"y\":14,\"subtype\":3,\"width\":8,\"height\":9},{\"type\":\"obstacle\",\"x\":17,\"y\":0,\"width\":4,\"height\":11},{\"type\":\"obstacle\",\"x\":17,\"y\":12,\"width\":4,\"height\":11},{\"type\":\"obstacle\",\"x\":14,\"y\":10,\"width\":1,\"height\":3},{\"type\":\"obstacle\",\"x\":12,\"y\":0,\"width\":5,\"height\":1},{\"type\":\"obstacle\",\"x\":12,\"y\":22,\"width\":5,\"height\":1}],\"products\":[{\"type\":\"product\",\"subtype\":0,\"resources\":[10,10,0,0,0,0,0,0],\"points\":10},{\"type\":\"product\",\"subtype\":1,\"resources\":[0,0,10,10,0,0,0,0],\"points\":10}],\"turns\":50,\"time\":30}";
    inputText += "\n\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(
        inputText.getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    IoSystem ioSystem = new IoSystem(inputStream, outputStream, controller);
    ioSystem.start();

    String outputText = outputStream.toString(StandardCharsets.UTF_8);
    String[] outputLines = outputText.split("\n");

    Assertions.assertTrue(outputLines.length != 0);
    Assertions.assertFalse(outputLines[0].contains("Could not read field."));
  }


}