package de.unimarburg.profit.algorithm;

import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.MovableObject;
import de.unimarburg.profit.model.Product;
import java.util.Collection;

/**
 * Interface responsible for the algorithm.
 *
 * @author Yannick
 */
public interface Algorithm {

  /**
   * Starts the algorithm.
   *
   * @param field    Field
   * @param time     The maximal time in seconds, that the algorithm can take.
   * @param turns    Turns, that the {@link Field} will be simulated.
   * @param products {@link Product}s, that exist in this instance of the problem.
   * @return Collection of {@link MovableObject}s, that should be placed for the best solution.
   */
  Collection<MovableObject> runAlgorithm(Field field, int time, int turns,
      Collection<Product> products);
}
