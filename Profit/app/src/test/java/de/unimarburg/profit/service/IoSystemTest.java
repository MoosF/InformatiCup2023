package de.unimarburg.profit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class IoSystemTest {


  @Test
  public void test1() {

    String inputText = "ShouldThrowError\nShouldThrowError\n\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(inputText.getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    IoSystem ioSystem = new IoSystem(inputStream, outputStream);
    ioSystem.start();

    String outputText = outputStream.toString(StandardCharsets.UTF_8);
    String[] outputLines = outputText.split("\n");

    assertEquals(2, outputLines.length);
    Assertions.assertTrue(outputLines[0].startsWith("Could not read field."));
    Assertions.assertTrue(outputLines[1].startsWith("Could not read field."));

  }

  //@Test
  public void test2() throws IOException {

    String inputText = Files.readString(Path.of("001.task-with-time.json")) + "\n\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(inputText.getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    IoSystem ioSystem = new IoSystem(inputStream, outputStream);
    ioSystem.start();

    String outputText = outputStream.toString(StandardCharsets.UTF_8);
    String[] outputLines = outputText.split("\n");

    Assertions.assertFalse(outputLines[0].contains("Could not"));

  }

  @Test
  public void testAlreadyStarted(){

    String inputText = "\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(inputText.getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    IoSystem ioSystem = new IoSystem(inputStream, outputStream);
    ioSystem.start();

    Assertions.assertThrows(RuntimeException.class, ioSystem::start);

  }




}