package de.unimarburg.profit.controller;

import de.unimarburg.profit.algorithm.Algorithm;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.FixedObject;
import de.unimarburg.profit.model.MovableObject;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import de.unimarburg.profit.service.Input;
import java.io.IOException;
import java.util.Collection;

/**
 * Implementation of {@link Controller}.
 *
 * @author Yannick Kraml
 */
public class ControllerImpl implements Controller {

  private final Algorithm algorithm;

  public ControllerImpl(Algorithm algorithm) {
    this.algorithm = algorithm;
  }

  /**
   * Starts the algorithm with the given Input.
   *
   * @param input Input, with that the algorithm should run.
   * @return Collection of {@link MovableObject}s, that resembles the best solution.
   * @throws IOException gets thrown, if the Input was not well-formed.
   */
  @Override
  public Collection<MovableObject> startAlgorithm(Input input) throws IOException {
    Field field = new Field(input.getWidth(), input.getHeight());

    for (FixedObject obj : input.getInputObjects()) {
      try {
        field.addBaseObject(obj);
      } catch (CouldNotPlaceObjectException e) {
        throw new IOException("Could not place all Objects");
      }
    }

    return algorithm.runAlgorithm(field, input.getTime(), input.getTurns(), input.getProducts());
  }

  @Override
  public void stopEverything() {
    //Exit the programm instantly
    System.exit(0);
  }
}
