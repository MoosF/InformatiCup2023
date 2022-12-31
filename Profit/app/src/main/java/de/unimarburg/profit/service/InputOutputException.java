package de.unimarburg.profit.service;

import java.io.IOException;

/**
 * Exception that can be thrown while parsing the input file. It is not a replacement for an
 * {@link java.io.IOException}. This exception is thrown when there is a mistake with the format of
 * the input file's content.
 *
 * @author Fabian Moos
 */
public class InputOutputException extends IOException {

  public InputOutputException(String message) {
    super(message);
  }
}
