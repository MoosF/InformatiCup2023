package de.uni_marburg.profit.simulation;

/**
 * Gets thrown, if an exception happens during the simulation.
 */
public class SimulateException extends Exception {

  public SimulateException(String message) {
    super(message);
  }
}
