package de.unimarburg.profit.service;

import de.unimarburg.profit.algorithm.Algorithm;
import de.unimarburg.profit.algorithm.AlgorithmImpl;
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

public class IoSystem {

  private final PrintStream printStream;
  private final Scanner scanner;

  private boolean isReading;

  public IoSystem(InputStream inputStream, OutputStream outputStream) {
    this.isReading = false;
    scanner = new Scanner(inputStream);
    printStream = new PrintStream(outputStream);
  }

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
    field.show();

    for (FixedObject obj : input.getInputObjects()) {
      try {
        field.addBaseObject(obj);
      } catch (CouldNotPlaceObjectException e) {
        throw new IOException("Could not place all Objects");
      }
    }

    Algorithm algorithm = new AlgorithmImpl();
    Collection<MovableObject> movableObjects = algorithm.runAlgorithm(field, input.getTime(),
        input.getTurns());

    String outputString = Json.generateOutputString(movableObjects);
    printStream.println(outputString);

  }
}
