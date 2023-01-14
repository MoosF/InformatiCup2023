package de.unimarburg.profit.service;

import de.unimarburg.profit.controller.Controller;
import de.unimarburg.profit.model.MovableObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Scanner;

/**
 * System, that is responsible for the input and output. You can specify the {@link InputStream} and
 * {@link OutputStream}.
 *
 * @author Yannick Kraml.
 */
public class IoSystem {

  private final PrintStream printStream;
  private final Scanner scanner;
  private boolean isReading;
  private final Controller controller;

  /**
   * Constructor of the {@link IoSystem}.
   *
   * @param inputStream  {@link InputStream}, from which the input will be read. Should support a
   *                     scanner.
   * @param outputStream {@link OutputStream}, to which the output will be written. Should support a
   *                     printStream.
   * @param controller
   */
  public IoSystem(InputStream inputStream, OutputStream outputStream, Controller controller) {
    this.controller = controller;
    this.isReading = false;
    scanner = new Scanner(inputStream);
    printStream = new PrintStream(outputStream);
  }

  /**
   * Starts the {@link IoSystem}. If already running, an {@link RuntimeException} will be thrown.
   */
  public void start() {

    if (isReading) {
      throw new RuntimeException("Already Reading");
    }

    isReading = true;

    while (isReading) {

      String line = scanner.nextLine();
      if (line.isEmpty()) {
        isReading = false;
        break;
      }

      try {
        readField(line);
      } catch (Exception e) {
        printStream.printf("Could not read field. Reason: %s%n", e.getMessage());
      }
    }


  }


  private void readField(String line) throws IOException {
    Input input = InputOutputHandle.readInputFrom(line);

    Collection<MovableObject> movableObjects = controller.startAlgorithm(input);

    String outputString = InputOutputHandle.generateOutput(movableObjects);
    printStream.println(outputString);

  }

}
