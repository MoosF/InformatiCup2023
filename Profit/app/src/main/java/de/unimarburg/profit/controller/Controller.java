package de.unimarburg.profit.controller;

import de.unimarburg.profit.algorithm.Algorithm;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.FixedObject;
import de.unimarburg.profit.model.MovableObject;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import de.unimarburg.profit.service.Input;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * The Controller handles the communication between the IO and the rest of the program.
 *
 * @author Yannick Kraml
 */
public class Controller {

  private final Algorithm algorithm;

  public Controller(Algorithm algorithm) {
    this.algorithm = algorithm;
  }

  /**
   * Starts the algorithm with the given Input.
   *
   * @param input Input, with that the algorithm should run.
   * @return Collection of {@link MovableObject}s, that resembles the best solution.
   * @throws IOException gets thrown, if the Input was not well-formed.
   */
  public Collection<MovableObject> startAlgorithm(Input input) throws IOException {
    Field field = new Field(input.getWidth(), input.getHeight());

    for (FixedObject obj : input.getInputObjects()) {
      try {
        field.addBaseObject(obj);
      } catch (CouldNotPlaceObjectException e) {
        throw new IOException("Could not place all Objects");
      }
    }

    Collection<MovableObject> movableObjects = algorithm.runAlgorithm(field, input.getTime(),
        input.getTurns(), input.getProducts());

    for (var obj : movableObjects) {
      try {
        field.addBaseObject(obj);
      } catch (CouldNotPlaceObjectException e) {
        System.err.println(e.getMessage());
        System.err.println(Arrays.toString(e.getStackTrace()));
      }
    }
    return movableObjects;
  }
}
