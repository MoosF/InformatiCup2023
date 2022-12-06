package de.unimarburg.profit.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import de.unimarburg.profit.model.Combiner;
import de.unimarburg.profit.model.Conveyer;
import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.FixedObject;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.MovableObject;
import de.unimarburg.profit.model.Obstacle;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.enums.ProductType;
import de.unimarburg.profit.model.enums.ResourceType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * Class for handling inputs of {@link FileType} JSON.
 *
 * @author Fabian Moos
 */
final class Json extends InputOutputHandle {

  private static final String KEY_HEIGHT = "height";
  private static final String KEY_OBJECTS = "objects";
  private static final String KEY_POINTS = "points";
  private static final String KEY_PRODUCTS = "products";
  private static final String KEY_RESOURCES = "resources";
  private static final String KEY_SUBTYPE = "subtype";
  private static final String KEY_TIME = "time";
  private static final String KEY_TURNS = "turns";
  private static final String KEY_TYPE = "type";
  private static final String KEY_WIDTH = "width";
  private static final String KEY_X = "x";
  private static final String KEY_Y = "y";

  private static final String TYPE_VALUE_DEPOSIT = "deposit";
  private static final String TYPE_VALUE_OBSTACLE = "obstacle";
  private static final String TYPE_VALUE_PRODUCT = "product";

  private static final String VALUE_COMBINER = "combiner";
  private static final String VALUE_CONVEYER = "conveyor";
  private static final String VALUE_FACTORY = "factory";
  private static final String VALUE_MINE = "mine";


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
   * actual behavior is determined by the values set in the {@link Settings}-class.
   *
   * @param output A {@link List} of {@link MovableObject}s.
   */
  public static void writeOutObjects(List<MovableObject> output) {
    var settings = Settings.getInstance();
    if (settings.exportTargetIsStdOut()) {
      System.out.println(generateOutputString(output));
    } else {
      writeOutputToFile(output);
    }
  }

  /**
   * This function can parse any JSON input file, but throws exceptions if the JSON-file does not
   * contain a {@link JsonObject} containing all required input parameters for a {@link Field} as
   * defined by the exercise for the InformatiCup2023.
   *
   * @param input The relative or absolute path to a file containing a {@link JsonObject} or a
   *              {@link String} containing a {@link JsonObject}.
   * @return An instance of a class that implements the interface {@link Input} and holds all
   * relevant information about the {@link Field} defined by the input file.
   * @throws InputOutputException when an error has occurred while parsing the input or if
   *                              {@code input} has been {@code null}.
   */
  public static Input getInputFrom(String input) throws InputOutputException {
    if (null == input) {
      throw new InputOutputException("The input string has been empty!");
    }
    var settings = Settings.getInstance();
    if (settings.importTargetIsStdIn()) {
      return getInputFromString(input);
    } else {
      return getInputFromFile(input);
    }
  }

  /**
   * Generates a JSON-Array from the list of {@link MovableObject}s given as a parameter.
   *
   * @param output the {@link MovableObject}s that will be turned into a JSON-Array.
   * @return a {@link String} containing the {@link MovableObject}s from {@code output} as a
   * JSON-Array.
   */
  static String generateOutputString(List<MovableObject> output) {
    JsonArray outputArray = new JsonArray();

    for (MovableObject movableObject : output) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.add(KEY_TYPE, new JsonPrimitive(
          switch (movableObject.getType()) {
            case COMBINER -> VALUE_COMBINER;
            case CONVEYER -> VALUE_CONVEYER;
            case FACTORY -> VALUE_FACTORY;
            case MINE -> VALUE_MINE;
          }
      ));

      jsonObject.add(KEY_X, new JsonPrimitive(movableObject.getX()));
      jsonObject.add(KEY_Y, new JsonPrimitive(movableObject.getY()));

      jsonObject.add(KEY_SUBTYPE, new JsonPrimitive(
          switch (movableObject.getType()) {
            case COMBINER -> ((Combiner) movableObject).getSubType().ordinal();
            case CONVEYER -> ((Conveyer) movableObject).getSubType().ordinal();
            case FACTORY -> ((Factory) movableObject).getSubType().ordinal();
            case MINE -> ((Mine) movableObject).getSubType().ordinal();
          }
      ));

      outputArray.add(jsonObject);
    }

    return outputArray.toString();
  }

  /**
   * Writes the {@link MovableObject}s in output into the file that is set in {@link Settings}.
   *
   * @param output A list of {@link MovableObject}s.
   */
  private static void writeOutputToFile(List<MovableObject> output) throws InputOutputException {
    var settings = Settings.getInstance();
    String outputString = generateOutputString(output);
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(settings.getExportFileName()));
      writer.write(outputString);
      writer.close();
    } catch (IOException e) {
      throw new InputOutputException("Failed to write output to file " +
          settings.getExportFileName() + "!");
    }
  }

  /**
   * @param inputFileName The absolute path or the relative path to the current working directory of
   *                      the input file.
   * @return The file inputFileName as one String. An empty String if an Exception has been caused.
   */
  private static String readInputFile(String inputFileName) throws InputOutputException {
    try {
      BufferedReader inputReader = new BufferedReader(new FileReader(inputFileName));
      String inputString = inputReader.lines().collect(Collectors.joining());
      inputReader.close();
      return inputString;
    } catch (IOException e) {
      throw new InputOutputException("Failed to read JSON-file!");
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
    var jsonReader = new Gson();
    JsonObject jsonObject;
    try {
      jsonObject = jsonReader.fromJson(inputString, JsonObject.class);
    } catch (JsonSyntaxException e) {
      throw new InputOutputException("Not a valid JSON-object!");
    }

    Json jsonInput = new Json();

    readPrimitives(jsonInput, jsonObject);
    readObjects(jsonInput, jsonObject);
    readProducts(jsonInput, jsonObject);

    return jsonInput;
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
      default -> throw new InputOutputException("Product subtype does not exist!");
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
      default -> throw new InputOutputException("Resource subtype does not exist!");
    };
  }

  /**
   * Reads an {@code int} from a {@link JsonObject}. The integer must be {@code >= 0}, because there
   * is no attribute or object in the {@link Field} that can make any sense of a negative
   * {@code int}.
   *
   * @param jsonObject The {@code int} will be retrieved from this {@link JsonObject}.
   * @param KEY        The key in the given {@link JsonObject} that maps to the requested
   *                   {@code int} value.
   * @return the requested integer.
   * @throws InputOutputException if the JSON-primitive found under {@code KEY} is not a valid
   *                              integer or the acquired integer is smaller than 0.
   */
  private static int readIntFromJsonObject(JsonObject jsonObject, final String KEY)
      throws InputOutputException {
    var maybeValue = readPrimitiveValueFromJsonObject(jsonObject, KEY);
    int value;
    try {
      value = maybeValue.getAsInt();
    } catch (NumberFormatException e) {
      throw new InputOutputException("Value for key " + KEY + "must be convertible to int!");
    }
    if (value < 0) {
      throw new InputOutputException("Integer is smaller than 0!");
    }
    return value;
  }

  /**
   * Reads the objects field of the input {@link JsonObject}.
   *
   * @param json       The {@link Json} object that will be filled with the acquired objects.
   * @param jsonObject The {@link JsonObject} the objects will be retrieved from.
   * @throws InputOutputException if there is no {@link JsonArray} in the {@code jsonObject}, or if
   *                              there is a non {@link JsonObject} in the {@link JsonArray} or if
   *                              an object-type is encountered that is not known to the program.
   */
  private static void readObjects(Json json, JsonObject jsonObject) throws InputOutputException {
    var jsonArray = jsonObject.getAsJsonArray(KEY_OBJECTS);
    if (null == jsonArray) {
      throw new InputOutputException("No objects supplied!");
    }
    for (JsonElement elem : jsonArray) {
      if (!elem.isJsonObject()) {
        throw new InputOutputException("\"objects\"-array contained non JSON-object element!");
      }
      var object = elem.getAsJsonObject();
      var type = readStringFromJsonObject(object, KEY_TYPE);
      var width = readIntFromJsonObject(object, KEY_WIDTH);
      var height = readIntFromJsonObject(object, KEY_HEIGHT);
      var x = readIntFromJsonObject(object, KEY_X);
      var y = readIntFromJsonObject(object, KEY_Y);

      switch (type) {
        case TYPE_VALUE_DEPOSIT -> {
          var subtype_number = object.get(KEY_SUBTYPE).getAsInt();
          var subtype = getResourceTypeFor(subtype_number);
          json.objects.add(Deposit.createDeposit(subtype, x, y, width, height));
        }
        case TYPE_VALUE_OBSTACLE -> json.objects.add(Obstacle.createObstacle(x, y, width, height));
        default -> throw new InputOutputException("Encountered \"object\"= with unsupported type(="
            + type + "!");
      }
    }
  }

  /**
   * Reads all primitive {@link JsonElement}s that are required for a {@link Field}.
   *
   * @param json       The {@link Json} object the values will be stored in.
   * @param jsonObject The {@link JsonObject} the values will be retrieved from.
   * @throws InputOutputException if reading a value from the {@link JsonObject} fails.
   */
  private static void readPrimitives(Json json, JsonObject jsonObject) throws InputOutputException {
    json.height = readIntFromJsonObject(jsonObject, KEY_HEIGHT);
    json.width = readIntFromJsonObject(jsonObject, KEY_WIDTH);
    json.time = readIntFromJsonObject(jsonObject, KEY_TIME);
    json.turns = readIntFromJsonObject(jsonObject, KEY_TURNS);
  }

  /**
   * Reads a primitive value from a {@link JsonObject}.
   *
   * @param jsonObject The {@link JsonObject} the primitive value will be retrieved from.
   * @param KEY        The key that maps to the desired value in the {@link JsonObject}.
   * @return a {@code JsonElement} containing a value of a primitive Json-type.
   * @throws InputOutputException if there is no mapping for {@code KEY}, or if the requested value
   *                              the key maps to does not have a primitive Json-type.
   */
  private static JsonElement readPrimitiveValueFromJsonObject(JsonObject jsonObject,
      final String KEY) throws InputOutputException {
    var maybeValue = jsonObject.get(KEY);
    if (maybeValue == null) {
      throw new InputOutputException(KEY + " value has not been supplied!");
    }
    if (!maybeValue.isJsonPrimitive()) {
      throw new InputOutputException(KEY + " must be a primitive JSON-value!");
    }
    return maybeValue;
  }

  /**
   * Reads all requested {@link Product}s from a {@link JsonObject}.
   *
   * @param json       A {@link Json} object the Products will be stored in.
   * @param jsonObject The {@link JsonObject} the {@link Product}s will be retrieved from.
   * @throws InputOutputException if there is no mapping for the key "products" in the given
   *                              {@link JsonObject}, or if there is a non {@link JsonObject}
   *                              element in the products-array or if there is a {@link JsonObject}
   *                              of an unsupported type or if the resources {@link JsonArray} of a
   *                              product object is not present or if the resources array contained
   *                              a non-integer value or if the required amount for a resource for
   *                              the {@link Product} is missing.
   */
  private static void readProducts(Json json, JsonObject jsonObject) {
    var jsonArray = jsonObject.getAsJsonArray(KEY_PRODUCTS);
    if (null == jsonArray) {
      throw new InputOutputException("No products supplied!");
    }
    for (JsonElement elem : jsonArray) {
      if (!elem.isJsonObject()) {
        throw new InputOutputException("\"products\"-array contained non JSON-object element!");
      }
      var object = elem.getAsJsonObject();
      var type = readStringFromJsonObject(object, KEY_TYPE);
      if (!TYPE_VALUE_PRODUCT.equals(type)) {
        throw new InputOutputException("Unsupported products type \"" + type + "\" encountered!");
      }
      var subtype_number = readIntFromJsonObject(object, KEY_SUBTYPE);
      var subtype = getProductTypeFor(subtype_number);
      var points = readIntFromJsonObject(object, KEY_POINTS);

      var resourcesArray = object.getAsJsonArray(KEY_RESOURCES);
      if (resourcesArray == null) {
        throw new InputOutputException("No resources for product supplied!");
      }

      HashMap<ResourceType, Integer> resourcesList = new HashMap<>();

      for (int i = 0; i < resourcesArray.size(); ++i) {
        try {
          resourcesList.put(getResourceTypeFor(i), resourcesArray.get(i).getAsInt());
        } catch (UnsupportedOperationException e) {
          throw new InputOutputException("Resources requirement for product was not of a " +
              "primitive Json type!");
        } catch (NumberFormatException | IllegalStateException e) {
          throw new InputOutputException("Resources requirement for product was not an int!");
        }
      }

      // Greater than 8 is already checked when the resource type is queried in the for-loop
      if (resourcesList.size() < 8) {
        throw new InputOutputException("Not enough resource requirements for product!");
      }

      json.products.add(new Product(points, subtype, resourcesList));
    }
  }

  /**
   * Reads a {@link String} from a {@link JsonObject}.
   *
   * @param jsonObject The {@link JsonObject} the {@code String} will be retrieved from.
   * @param KEY        The key that maps to the desired value in the json object.
   * @return the string the {@code KEY} maps to in the given json object.
   * @throws InputOutputException if there is an exception while reading the primitive value from
   *                              the {@link JsonObject}.
   * @see Json#readPrimitiveValueFromJsonObject(JsonObject, String)
   */
  private static String readStringFromJsonObject(JsonObject jsonObject, final String KEY)
      throws InputOutputException {
    var maybeValue = readPrimitiveValueFromJsonObject(jsonObject, KEY);
    // All Exceptions that could be thrown by getAsString are already handled in
    // readPrimitiveValueFromJsonObject!
    return maybeValue.getAsString();
  }
}
