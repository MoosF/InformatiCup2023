package de.unimarburg.profit.service;

import de.unimarburg.profit.algorithm.Algorithm;
import de.unimarburg.profit.algorithm.factoryplacing.connector.ConnectorImpl;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryChooserRandom;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryPlaceFinderImpl;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryPlacerImpl;
import de.unimarburg.profit.algorithm.mineplacing.MinePlaceChooserImpl;
import de.unimarburg.profit.algorithm.mineplacing.MinePlaceFinderImpl;
import de.unimarburg.profit.algorithm.mineplacing.MinePlacerImpl;
import de.unimarburg.profit.algorithm.factoryplacing.combination.CombinationFinderImpl;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.FixedObject;
import de.unimarburg.profit.model.MovableObject;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
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

  /**
   * Constructor of the {@link IoSystem}.
   *
   * @param inputStream  {@link InputStream}, from which the input will be read. Should support a
   *                     scanner.
   * @param outputStream {@link OutputStream}, to which the output will be written. Should support a
   *                     printStream.
   */
  public IoSystem(InputStream inputStream, OutputStream outputStream) {
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

    Field field = new Field(input.getWidth(), input.getHeight());

    for (FixedObject obj : input.getInputObjects()) {
      try {
        field.addBaseObject(obj);
      } catch (CouldNotPlaceObjectException e) {
        throw new IOException("Could not place all Objects");
      }
    }

    Algorithm algorithm = new Algorithm(
        new MinePlaceFinderImpl(),
        new MinePlaceChooserImpl(),
        new MinePlacerImpl(),
        new FactoryPlaceFinderImpl(),
        new FactoryChooserRandom(),
        new FactoryPlacerImpl(),
        new CombinationFinderImpl(),
        new ConnectorImpl(field)
    );

    Collection<MovableObject> movableObjects = algorithm.runAlgorithm(field, input.getTime(),
        input.getTurns(), input.getProducts());

    String outputString = InputOutputHandle.generateOutput(movableObjects);
    printStream.println(outputString);

  }
}
