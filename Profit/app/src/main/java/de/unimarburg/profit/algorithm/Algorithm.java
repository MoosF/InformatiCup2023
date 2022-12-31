package de.unimarburg.profit.algorithm;

import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.MovableObject;
import de.unimarburg.profit.model.Product;
import java.util.Collection;
import java.util.List;

/**
 * This interfaces specifies with what information the algorithm should work and what it returns.
 *
 * @author Yannick Kraml
 */
public interface Algorithm {

  Collection<MovableObject> runAlgorithm(Field field, int time, int turns,
      Collection<Product> products);
}
