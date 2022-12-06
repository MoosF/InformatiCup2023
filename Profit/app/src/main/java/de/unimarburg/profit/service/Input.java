package de.unimarburg.profit.service;

import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.FixedObject;
import de.unimarburg.profit.model.Tile;
import de.unimarburg.profit.model.Product;
import java.util.List;

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
   * Returns a number {@code >= 0} as width of the {@link Field}.
   *
   * @return the width of {@link Field} in {@link Tile}s. This value can be at maximum {@code 100}.
   */
  int getWidth();

  /**
   * Returns a number {@code >= 0} as height of the {@link Field}.
   *
   * @return the height of {@link Field} in {@link Tile}s. This value can be at maximum {@code 100}.
   */
  int getHeight();

  /**
   * Returns a list of all {@link FixedObject}s that must be present on the {@link Field}.
   *
   * @return the list of {@link FixedObject}s for this {@link Field}.
   */
  List<FixedObject> getInputObjects();

  /**
   * Returns a list of all {@link Product}s that are requested for this {@link Field}.
   *
   * @return the List of {@link Product}s for this {@link Field}.
   */
  List<Product> getProducts();

  /**
   * Returns a number {@code >= 0} as number of turns for this {@link Field}.
   *
   * @return the number of maximum turns for this {@link Field}.
   */
  int getTurns();

  /**
   * Returns a number {@code >= 0} as the time limit for this {@link Field}. The number is the time
   * limit in seconds.
   *
   * @return the time limit for this {@link Field} in seconds.
   */
  int getTime();
}
