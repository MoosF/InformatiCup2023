package de.unimarburg.profit.service;

import de.unimarburg.profit.model.Combiner;
import de.unimarburg.profit.model.Conveyer;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.FixedObject;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.MovableObject;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.Tile;
import java.util.Collection;
import java.util.List;

/**
 * Class for handling input of {@link Field} parameters and output of calculated {@link Mine}-,
 * {@link Conveyer}-, {@link Combiner}- and {@link Factory}-objects.
 *
 * @author Fabian Moos
 * @see Input
 * @see InputOutputHandle#readInputFrom(String)
 * @see InputOutputHandle#writeOutObjects(Collection) (List)
 */
public abstract class InputOutputHandle implements Input {

  protected int width;
  protected int height;
  protected int turns;
  protected int time;
  protected final List<FixedObject> objects;
  protected final List<Product> products;

  /**
   * This constructor is used to give all implementing classes the possibility to decide with
   * {@link List}-implementing class is used for {@link InputOutputHandle#objects} and
   * {@link InputOutputHandle#products}.
   *
   * @param objects  A list of {@link FixedObject}s.
   * @param products A list of {@link Product}s.
   */
  protected InputOutputHandle(List<FixedObject> objects, List<Product> products) {
    this.objects = objects;
    this.products = products;
  }

  /**
   * Parses the input in the given {@link String}. The way the input is interpreted is determined by
   * the values set in the {@link Settings}-class.
   *
   * @param input A {@link String} containing a valid JSON-object or the relative or absolute path
   *              to a file containing the JSON-object.
   * @return A class that implements the interface {@link Input} and holds all relevant information
   *         about the {@link Field} defined by the input file.
   * @throws InputOutputException when an error has occurred while parsing the input.
   */
  public static Input readInputFrom(String input) throws InputOutputException {
    var settings = Settings.getInstance();
    return switch (settings.getImportFileType()) {
      case JSON -> Json.readInputFrom(input);
      case XML -> throw new InputOutputException("Input file type XML is not yet implemented!");
      default -> throw new InputOutputException("Input file type is not yet implemented!");
    };
  }

  /**
   * Writes the {@code output} to standard output or to a file. What actually happens is determined
   * by the values set in the {@link Settings}-class.
   *
   * @param output A {@link List} of {@link MovableObject}s.
   * @throws InputOutputException when a {@link FileType} is set in the {@link Settings} that is not
   *                              yet implemented.
   */
  public static void writeOutObjects(Collection<MovableObject> output) throws InputOutputException {
    var settings = Settings.getInstance();
    switch (settings.getExportFileType()) {
      case JSON -> Json.writeOutObjects(output);
      case XML -> throw new InputOutputException("Output file type XML is not yet implemented!");
      default -> throw new InputOutputException("Output file type is not yet implemented!");
    }
  }

  /**
   * Generates the output how specified.
   *
   * @param movableObjects {@link Collection} of {@link MovableObject}, that should be included in
   *                       the output
   * @return String, that represents the output.
   */
  public static String generateOutput(Collection<MovableObject> movableObjects) {
    String returnString;
    switch (Settings.getInstance().getExportFileType()) {
      case JSON -> returnString = Json.generateOutputString(movableObjects);
      case XML -> throw new IllegalStateException("Output file type XML is not yet implemented!");
      default -> throw new IllegalStateException(
          "Unexpected value: " + Settings.getInstance().getExportFileType());
    }

    return returnString;
  }

  /**
   * Returns the width of the field in {@link Tile}s. Returns -1 if no width was supplied.
   *
   * @return the required width of the field in {@link Tile}s.
   */
  @Override
  public int getWidth() {
    return width;
  }

  /**
   * Returns the height of the field in {@link Tile}s. Returns -1 if no height was supplied.
   *
   * @return the required height of the field in {@link Tile}s.
   */
  @Override
  public int getHeight() {
    return height;
  }

  /**
   * Returns a list of {@link FixedObject}s. Returns an empy list if no objects were supplied.
   *
   * @return the required {@link FixedObject}s.
   */
  @Override
  public List<FixedObject> getInputObjects() {
    return objects;
  }

  /**
   * Returns a list of requested {@link Product}s. Returns an empty list if no products were
   * supplied.
   *
   * @return the requested {@link Product}s.
   */
  @Override
  public List<Product> getProducts() {
    return products;
  }

  /**
   * Returns the number of turns for the field described by this {@link Input}. Returns -1 if the
   * number of turns has not been supplied.
   *
   * @return the maximum number of turns.
   */
  @Override
  public int getTurns() {
    return turns;
  }

  /**
   * Returns the time limit for this {@link Field}. The value is -1 if no time limit has been
   * supplied.
   *
   * @return the time limit for this {@link Field}.
   */
  @Override
  public int getTime() {
    return time;
  }

  /**
   * This class defines supported file types for input and output files. The file type that is
   * currently used can be set in the {@link Settings} of the program.
   *
   * @author Fabian Moos
   * @see Input
   * @see InputOutputHandle
   * @see Settings
   */
  public enum FileType {
    JSON,
    XML,
    TOML,
  }
}
