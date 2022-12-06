package de.uni.marburg.profit.service;

import de.uni.marburg.profit.model.Combiner;
import de.uni.marburg.profit.model.Conveyer;
import de.uni.marburg.profit.model.Deposit;
import de.uni.marburg.profit.model.Factory;
import de.uni.marburg.profit.model.Field;
import de.uni.marburg.profit.model.FixedObject;
import de.uni.marburg.profit.model.Mine;
import de.uni.marburg.profit.model.MovableObject;
import de.uni.marburg.profit.model.Obstacle;
import de.uni.marburg.profit.model.Product;
import de.uni.marburg.profit.model.enums.ProductType;
import de.uni.marburg.profit.model.enums.ResourceType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import de.uni_marburg.profit.model.*;

/**
 * Class for handling inputs of {@link FileType} JSON.
 *
 * @author Fabian Moos
 */
final class Json extends InputOutputHandle {

  /**
   * Use the static function {@link Json#getInputFrom} to create objects of type {@link Json}.
   */
  private Json() {
    super(new Vector<>(), new Vector<>());
    this.width = -1;
    this.height = -1;
    this.turns = -1;
    this.time = -1;
  }

  /**
   * Writes the {@link MovableObject}s in the given list into a file or to Standard Output. The
   * actual behavior is determined by the values et in the {@link Settings}-class.
   *
   * @param output A {@link List} of {@link MovableObject}s.
   * @return the generated {@link String} containing the output in the format determined by
   * {@link Settings}.
   */
  public static String generateOutput(List<MovableObject> output) {
    var settings = Settings.getInstance();
    if (settings.exportTargetIsStdOut()) {
      return generateOutputString(output);
    } else {
      return writeOutputToFile(output);
    }
  }

  /**
   * Generates a JSON-Array from the list of {@link MovableObject}s given as a parameter.
   *
   * @param output the {@link MovableObject}s that will be turned into a JSON-Array.
   * @return a {@link String} containing the {@link MovableObject}s from {@code output} as a
   * JSON-Array.
   */
  private static String generateOutputString(List<MovableObject> output) {
    StringBuilder stringBuilder = new StringBuilder("[");
    for (int i = 0; i < output.size(); ++i) {
      stringBuilder.append("{\"type\":");

      MovableObject object = output.get(i);
      switch (object.getType()) {
        case COMBINER -> stringBuilder.append("\"combiner\"");
        case CONVEYER -> stringBuilder.append("\"conveyor\"");
        case FACTORY -> stringBuilder.append("\"factory\"");
        case MINE -> stringBuilder.append("\"mine\"");
        default -> throw new IllegalStateException("Unexpected value: " + object.getType());
      }

      stringBuilder.append(",\"x\":");
      stringBuilder.append(object.getX());
      stringBuilder.append(",\"y\":");
      stringBuilder.append(object.getY());

      stringBuilder.append(",");
      stringBuilder.append("\"subtype\":");
      switch (object.getType()) {
        case COMBINER -> stringBuilder.append(((Combiner) object).getSubType().ordinal());
        case CONVEYER -> stringBuilder.append(((Conveyer) object).getSubType().ordinal());
        case FACTORY -> stringBuilder.append(((Factory) object).getSubType().ordinal());
        case MINE -> stringBuilder.append(((Mine) object).getSubType().ordinal());
        default -> throw new IllegalStateException("Unexpected value: " + object.getType());
      }

      stringBuilder.append("}");
      if (i != output.size() - 1) {
        stringBuilder.append(",");
      }
    }

    stringBuilder.append("]");
    return stringBuilder.toString();
  }

  /**
   * Writes the {@link MovableObject}s in output into the file that is set in {@link Settings}.
   *
   * @param output A list of {@link MovableObject}s.
   * @return An empty {@link String}.
   */
  private static String writeOutputToFile(List<MovableObject> output) {
    var settings = Settings.getInstance();
    String outputString = generateOutputString(output);
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(settings.getExportFileName()));
      writer.write(outputString);
      writer.close();
    } catch (IOException e) {
      System.err.println(e.getMessage());
      System.err.println(Arrays.toString(e.getStackTrace()));
    }
    return "";
  }

  /**
   * This function can't parse any JSON input file. It is made specifically for parsing the
   * JSON-input for a {@link Field} as defined by the exercise for the InformatiCup2023.
   *
   * @param input The relative or absolute path to file containing a JSON-object.
   * @return An {@link Object} that implements the interface {@link Input} and holds all relevant
   * information about the {@link Field} defined by the input file.
   * @throws InputOutputException when an error has occurred while parsing the input.
   */
  public static Input getInputFrom(String input) throws InputOutputException {
    var settings = Settings.getInstance();
    if (settings.importTargetIsStdIn()) {
      return getInputFromString(input);
    } else {
      return getInputFromFile(input);
    }
  }

  /**
   * Takes a {@link String} that contains an absolute or relative file name containing a valid
   * JSON-object with input information for a {@link Field}.
   *
   * @param jsonFile An absolute or relative path to the JSON file that holds the
   *                 {@link FixedObject} definitions.
   * @return An {@link Object} that implements the interface {@link Input} and holds all relevant
   * information about the {@link Field} defined by the input file.
   * @throws InputOutputException when an error has occurred while parsing the input.
   */
  private static Input getInputFromFile(String jsonFile) throws InputOutputException {
    var inputString = Json.readInputFile(jsonFile);
    return getInputFromString(inputString);
  }

  /**
   * Takes a {@link String} that contains a valid JSON-object with input information for a
   * {@link Field}.
   *
   * @param inputString Contains the JSON-object.
   * @return an {@link Input} object containing all information required to build a valid
   * {@link Field}. The returned object is never {@code null}. When an error occurs, an Exception is
   * thrown.
   * @throws InputOutputException when an error has occurred while parsing the input.
   */
  private static Input getInputFromString(String inputString) throws InputOutputException {
    var tokens = Json.tokenize(inputString);
    var it = Arrays.stream(tokens).iterator();
    Input finalInput = parse(it);
    if (it.hasNext()) {
      throw new InputOutputException("Input partly ignored!");
    }
    return finalInput;
  }

  /**
   * @param inputFileName The absolute path or the relative path to the current working directory of
   *                      the input file.
   * @return The file inputFileName as one String. An empty String if an Exception has been caused.
   */
  private static String readInputFile(String inputFileName) {
    try {
      BufferedReader inputReader = new BufferedReader(new FileReader(inputFileName));
      String inputString = inputReader.lines().collect(Collectors.joining());
      inputReader.close();
      return inputString;
    } catch (IOException e) {
      System.err.println(e.getMessage());
      System.err.println(Arrays.toString(e.getStackTrace()));
    }
    return "";
  }

  /**
   * @param fileString Contains all input from the input file.
   * @return a {@link String} array holding all JSON tokens, each in a single {@code String}.
   */
  private static String[] tokenize(String fileString) {
    return Arrays.stream(fileString.replace("{", ":{:")
        .replace("}", ":}:")
        .replace("[", ":[:")
        .replace("]", ":]:")
        .split(",|:|\"|\"")).filter((str) -> !str.isEmpty()).toArray(String[]::new);
  }

  /**
   * @param it An {@link Iterator} pointing to the first element of the input file.
   * @return an object containing all relevant information about the {@link Field} described in the
   * input file.
   */
  private static Input parse(Iterator<String> it) throws InputOutputException {
    Json input = new Json();
    if (!it.next().equals("{")) {
      throw new InputOutputException("Input is not a JSON-object!");
    }
    while (it.hasNext()) {
      var token = it.next();
      switch (token) {
        case "width" -> input.width = Integer.parseInt(it.next());
        case "height" -> input.height = Integer.parseInt(it.next());
        case "turns" -> input.turns = Integer.parseInt(it.next());
        case "time" -> input.time = Integer.parseInt(it.next());
        case "objects" -> parseObjects(input, it, token);
        case "products" -> parseProducts(input, it, token);
        default -> {
          if (token.equals("}")) {
            return input;
          } else {
            System.err.println(">>> UNEXPECTED TOKEN IN JSON FILE!");
          }
        }
      }
    }
    return null;
  }

  /**
   * @param num The number of the required requested resource.
   * @return the {@link ProductType} variant corresponding to the given number.
   * @throws InputOutputException When a number is encountered that does not correspond to a valid
   *                              {@link ProductType}.
   */
  private static ProductType getProductTypeFor(int num) throws InputOutputException {
    return switch (num) {
      case 0 -> ProductType.ZERO;
      case 1 -> ProductType.ONE;
      case 2 -> ProductType.TWO;
      case 3 -> ProductType.THREE;
      case 4 -> ProductType.FOUR;
      case 5 -> ProductType.FIVE;
      case 6 -> ProductType.SIX;
      case 7 -> ProductType.SEVEN;
      default -> throw new InputOutputException("Given product subtype does not exist!");
    };
  }

  /**
   * @param num The number of the resource. Must be in the interval [0,7].
   * @return The {@link ResourceType} corresponding to the given number.
   * @throws InputOutputException when the number input is outside the interval [0,7].
   */
  private static ResourceType getResourceTypeFor(int num) throws InputOutputException {
    return switch (num) {
      case 0 -> ResourceType.ZERO;
      case 1 -> ResourceType.ONE;
      case 2 -> ResourceType.TWO;
      case 3 -> ResourceType.THREE;
      case 4 -> ResourceType.FOUR;
      case 5 -> ResourceType.FIVE;
      case 6 -> ResourceType.SIX;
      case 7 -> ResourceType.SEVEN;
      default -> throw new InputOutputException("Given resource subtype does not exist!");
    };
  }

  /**
   * @param input The {@link Input} object that will be filled with the parsed information.
   * @param it    An {@link Iterator} over the remaining {@link String} elements.
   * @param token The last JSON token that has been retrieved from the iterator.
   */
  private static void parseObjects(Json input, Iterator<String> it, String token)
      throws InputOutputException {
    if (!it.next().equals("[")) {
      throw new InputOutputException("Fixed object array is missing in input file!");
    }
    TempFixedObject currentObject = null;
    while (it.hasNext() && !token.equals("]")) {
      if (token.equals("{")) {
        currentObject = new TempFixedObject();
      }
      switch (token) {
        // Current object can't be null, IntelliJ is lying here. The input must always have
        // something in the JSON-file's object-array.
        case "type" -> currentObject.type = it.next();
        case "x" -> currentObject.horPos = Integer.parseInt(it.next());
        case "y" -> currentObject.verPos = Integer.parseInt(it.next());
        case "subtype" -> currentObject.subtype = getResourceTypeFor(Integer.parseInt(it.next()));
        case "width" -> currentObject.width = Integer.parseInt(it.next());
        case "height" -> currentObject.height = Integer.parseInt(it.next());
        default -> setDepositOrObstacle(input, token, currentObject);
      }
      token = it.next();
    }
  }

  /**
   * @param input The {@link Input} object that will be filled with the parsed information.
   * @param it    An {@link Iterator} over the remaining {@link String} elements.
   * @param token The last JSON token that has been retrieved from the iterator.
   */
  private static void parseProducts(Json input, Iterator<String> it, String token) {
    if (!it.next().equals("[")) {
      throw new InputOutputException("Products array is missing in input file!");
    }
    TempProduct currentProduct = null;

    while (it.hasNext() && !token.equals("]")) {
      if (token.equals("{")) {
        currentProduct = new TempProduct();
      }

      switch (token) {
        case "type" -> it.next();
        case "subtype" -> currentProduct.subtype = getProductTypeFor(Integer.parseInt(it.next()));
        case "resources" -> parseResources(it, currentProduct);
        case "points" -> currentProduct.points = Integer.parseInt(it.next());
      }

      if (token.equals("}")) {
        input.products.add(new Product(currentProduct.points, currentProduct.subtype,
            currentProduct.requiredResources));
      }
      token = it.next();
    }
  }

  /**
   * Extracts all resources that are required to produce the {@code currentProduct} and directly
   * saves them in a {@link HashMap} in the current product.
   *
   * @param it             An {@link Iterator} over the remaining {@link String} elements.
   * @param currentProduct The current {@link Product} that is currently being extracted from the
   *                       iterator.
   */
  private static void parseResources(Iterator<String> it, TempProduct currentProduct) {
    if (!it.next().equals("[")) {
      throw new InputOutputException("Array for a Product's resources is missing!");
    }
    String token = it.next();
    currentProduct.requiredResources = new HashMap<>();
    int i = 0;
    while (it.hasNext() && !token.equals("]")) {
      currentProduct.requiredResources
          .put(getResourceTypeFor(i), Integer.parseInt(token));
      ++i;
      token = it.next();
    }
  }

  /**
   * When the current token is a closing curly bracket the current temporary object will be added to
   * the objects of the {@link Json} object. The type of the {@link TempFixedObject} decides whether
   * it will be saved as a {@link Deposit} or as an {@link Obstacle}.
   *
   * @param input         The {@link Input} object that will be filled with the parsed information.
   * @param token         The last JSON token that has been retrieved from the input tokens
   *                      iterator.
   * @param currentObject The current object that will be saved in the given {@link Json} object.
   *                      This is either a {@link Deposit} or an {@link Obstacle}.
   */
  private static void setDepositOrObstacle(Json input, String token,
      TempFixedObject currentObject) {
    if (token.equals("}")) {
      if (currentObject.type.equals("deposit")) {
        Deposit deposit = Deposit.createDeposit(currentObject.subtype,
            currentObject.horPos, currentObject.verPos,
            currentObject.width, currentObject.height);
        input.objects.add(deposit);
      } else {
        Obstacle obstacle = Obstacle.createObstacle(currentObject.horPos, currentObject.verPos,
            currentObject.width, currentObject.height);
        input.objects.add(obstacle);
      }
    }
  }

  /**
   * Temporary class for holding information related to a {@link FixedObject}. This class is
   * supposed to hold the gathered information only until all information for one specific fixed
   * object is extracted from the JSON file.
   */
  private static class TempFixedObject {

    int horPos;
    int verPos;
    int width;
    int height;
    String type;
    ResourceType subtype;
  }

  /**
   * Temporary class for holding information related to a {@link Product}.
   */
  private static class TempProduct {

    ProductType subtype;
    int points;
    HashMap<ResourceType, Integer> requiredResources;
  }
}
