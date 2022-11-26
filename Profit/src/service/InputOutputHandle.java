package service;

import java.util.List;
import model.Combiner;
import model.Conveyor;
import model.Factory;
import model.Field;
import model.FixedObject;
import model.Mine;
import model.MovableObject;
import model.Product;
import model.Tile;

import static service.InputOutputHandle.FileType.*;

/**
 * Class for handling input of {@link Field} parameters and output of calculated {@link Mine}-,
 * {@link Conveyor}-, {@link Combiner}- and {@link Factory}-objects.
 *
 * @author Fabian Moos
 * @see Input
 * @see InputOutputHandle#getInputFrom(String)
 * @see InputOutputHandle#generateOutput(List)
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
   * @return An class that implements the interface {@link Input} and holds all relevant information
   * about the {@link Field} defined by the input file.
   * @throws InputOutputException when an error has occurred while parsing the input.
   */
  public static Input getInputFrom(String input) throws InputOutputException {
    var settings = Settings.getInstance();
    return switch (settings.getImportFileType()) {
      case JSON -> Json.getInputFrom(input);
      case XML -> throw new InputOutputException("Input file type XML is not yet implemented!");
      default -> throw new InputOutputException("Input file type is not yet implemented!");
    };
  }

  /**
   * Writes the {@code output} to standard output or to a file. What actually happens is determined
   * by the values set in the {@link Settings}-class.
   *
   * @param output A {@link List} of {@link MovableObject}s.
   * @return the generated {@link String} containing the output in the format determined by
   * {@link Settings}.
   * @throws InputOutputException when a {@link FileType} is set in the {@link Settings} that is not
   *                              yet implemented.
   */
  public static String generateOutput(List<MovableObject> output) throws InputOutputException {
    String outputString;
    var settings = Settings.getInstance();
    switch (settings.getExportFileType()) {
      case JSON -> outputString = Json.generateOutput(output);
      case XML -> throw new InputOutputException("Output file type XML is not yet implemented!");
      default -> throw new InputOutputException("Output file type is not yet implemented!");
    }
    return outputString;
  }

  /**
   * @return the required width of the field in {@link Tile}s.
   */
  @Override
  public int getWidth() {
    return width;
  }

  /**
   * @return the required height of the field in {@link Tile}s.
   */
  @Override
  public int getHeight() {
    return height;
  }

  /**
   * @return the required {@link FixedObject}s.
   */
  @Override
  public List<FixedObject> getInputObjects() {
    return objects;
  }

  /**
   * @return the requested {@link Product}s.
   */
  @Override
  public List<Product> getProducts() {
    return products;
  }

  /**
   * @return the maximum number of turns.
   */
  @Override
  public int getTurns() {
    return turns;
  }

  /**
   * @return the available times.
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
