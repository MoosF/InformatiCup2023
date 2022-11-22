package service;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import model.Deposit;
import model.Field;
import model.FixedObject;
import model.Obstacle;
import model.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import model.ProductType;
import model.ResourceType;

/**
 * @author Fabian Moos
 */
public class JsonInput extends AbstractInput {

  /**
   * Constructor is private because the static function {@link JsonInput#createInputFromFile} should
   * be used to create objects of type {@link JsonInput}.
   */
  private JsonInput() {
    super(new Vector<>(), new Vector<>());
    this.width = -1;
    this.height = -1;
    this.turns = -1;
    this.time = -1;
  }

  /**
   * This function can't parse any input files. It is made specifically for parsing the JSON-input
   * for a {@link Field} as defined by the exercise for the InformatiCup2023.
   *
   * @param jsonFile An absolute or relative path to the JSON file that holds the
   *                 {@link FixedObject} definitions.
   * @return An {@link Object} that implements the interface {@link InputFile} and holds all
   * relevant information about the {@link Field} defined by the input file.
   */
  public static InputFile createInputFromFile(String jsonFile) throws InputException {
    var inputString = JsonInput.readInputFile(jsonFile);
    var tokens = JsonInput.tokenize(inputString);
    var it = Arrays.stream(tokens).iterator();
    InputFile finalInput = parse(it);
    if (it.hasNext()) {
      throw new InputException("Input partly ignored!");
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
  private static InputFile parse(Iterator<String> it) throws InputException {
    JsonInput input = new JsonInput();
    if (!it.next().equals("{")) {
      throw new InputException("Input is not a JSON-object!");
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
   * @throws InputException When a number is encountered that does not correspond to a valid
   *                        {@link ProductType}.
   */
  private static ProductType getProductTypeFor(int num) throws InputException {
    return switch (num) {
      case 0 -> ProductType.ZERO;
      case 1 -> ProductType.ONE;
      case 2 -> ProductType.TWO;
      case 3 -> ProductType.THREE;
      case 4 -> ProductType.FOUR;
      case 5 -> ProductType.FIVE;
      case 6 -> ProductType.SIX;
      case 7 -> ProductType.SEVEN;
      default -> throw new InputException("Given product subtype does not exist!");
    };
  }

  /**
   * @param num The number of the resource. Must be in the interval [0,7].
   * @return The {@link ResourceType} corresponding to the given number.
   * @throws InputException when the number input is outside the interval [0,7].
   */
  private static ResourceType getResourceTypeFor(int num) throws InputException {
    return switch (num) {
      case 0 -> ResourceType.ZERO;
      case 1 -> ResourceType.ONE;
      case 2 -> ResourceType.TWO;
      case 3 -> ResourceType.THREE;
      case 4 -> ResourceType.FOUR;
      case 5 -> ResourceType.FIVE;
      case 6 -> ResourceType.SIX;
      case 7 -> ResourceType.SEVEN;
      default -> throw new InputException("Given resource subtype does not exist!");
    };
  }

  /**
   * @param input The {@link InputFile} object that will be filled with the parsed information.
   * @param it    An {@link Iterator} over the remaining {@link String} elements.
   * @param token The last JSON token that has been retrieved from the iterator.
   */
  private static void parseObjects(JsonInput input, Iterator<String> it, String token)
      throws InputException {
    if (!it.next().equals("[")) {
      throw new InputException("Fixed object array is missing in input file!");
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
        case "x" -> currentObject.x = Integer.parseInt(it.next());
        case "y" -> currentObject.y = Integer.parseInt(it.next());
        case "subtype" -> currentObject.subtype = getResourceTypeFor(Integer.parseInt(it.next()));
        case "width" -> currentObject.width = Integer.parseInt(it.next());
        case "height" -> currentObject.height = Integer.parseInt(it.next());
        default -> setDepositOrObstacle(input, token, currentObject);
      }
      token = it.next();
    }
  }

  /**
   * @param input The {@link InputFile} object that will be filled with the parsed information.
   * @param it    An {@link Iterator} over the remaining {@link String} elements.
   * @param token The last JSON token that has been retrieved from the iterator.
   */
  private static void parseProducts(JsonInput input, Iterator<String> it, String token) {
    if (!it.next().equals("[")) {
      throw new InputException("Products array is missing in input file!");
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
      throw new InputException("Array for a Product's resources is missing!");
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
   * the objects of the {@link JsonInput} object. The type of the {@link TempFixedObject} decides
   * whether it will be saved as a {@link Deposit} or as an {@link Obstacle}.
   *
   * @param input         The {@link InputFile} object that will be filled with the parsed
   *                      information.
   * @param token         The last JSON token that has been retrieved from the input tokens
   *                      iterator.
   * @param currentObject The current object that will be saved in the given {@link JsonInput}
   *                      object. This is either a {@link Deposit} or an {@link Obstacle}.
   */
  private static void setDepositOrObstacle(JsonInput input, String token,
      TempFixedObject currentObject) {
    if (token.equals("}")) {
      if (currentObject.type.equals("deposit")) {
        Deposit deposit = Deposit.createDeposit(currentObject.subtype,
            currentObject.x, currentObject.y,
            currentObject.width, currentObject.height);
        input.objects.add(deposit);
      } else {
        Obstacle obstacle = Obstacle.createObstacle(currentObject.x, currentObject.y,
            currentObject.width, currentObject.height);
        input.objects.add(obstacle);
      }
    }
  }

  /**
   * Temporary class for holding information related to a {@link FixedObject}.
   */
  private static class TempFixedObject {

    int x;
    int y;
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
