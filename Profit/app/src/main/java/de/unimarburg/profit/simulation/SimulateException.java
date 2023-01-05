package de.unimarburg.profit.simulation;

/**
 * Gets thrown, if an exception happens during the simulation.
 *
 * @author Yannick Kraml
 */
public class SimulateException extends Exception {

  public SimulateException(String message) {
    super(message);
  }
}
