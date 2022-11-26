package service;

import java.util.List;
import model.Field;
import model.FixedObject;
import model.Product;
import model.Tile;

/**
 * There can only be {@code width}-, {@code height}-, {@code turns}-, {@code time}, {@code object}-
 * and {@code product} values in the input file. These can be queried by calling the functions that
 * are defined by this interface.
 *
 * @author Fabian Moos
 * @see Json
 */
public interface Input {

  /**
   * @return the width of {@link Field} in {@link Tile}s. This value can be at maximum {@code 100}.
   */
  int getWidth();

  /**
   * @return the height of {@link Field} in {@link Tile}s. This value can be at maximum {@code 100}.
   */
  int getHeight();

  /**
   * @return the list of {@link FixedObject}s for this {@link Field}.
   */
  List<FixedObject> getInputObjects();

  /**
   * @return the List of {@link Product}s for this {@link Field}.
   */
  List<Product> getProducts();

  /**
   * @return the number of maximum turns for this {@link Field}.
   */
  int getTurns();

  /**
   * @return the time limit for this {@link Field} in seconds.
   */
  int getTime();
}
