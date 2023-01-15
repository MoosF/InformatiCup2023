package de.unimarburg.profit.controller;

import de.unimarburg.profit.model.MovableObject;
import de.unimarburg.profit.service.Input;
import java.io.IOException;
import java.util.Collection;

/**
 * Interface for a Controller. The Controller handles the communication between the IO and the rest
 * of the program.
 *
 * @author Yannick Kraml
 */
public interface Controller {

  Collection<MovableObject> startAlgorithm(Input input) throws IOException;

  default void stopEverything() {
    //doNothing. Threads should normally close.
  }
}
