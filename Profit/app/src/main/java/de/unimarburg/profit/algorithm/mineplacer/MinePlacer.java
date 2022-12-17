package de.unimarburg.profit.algorithm.mineplacer;

import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import java.util.Collection;

/**
 * This interface is responsible to place {@link Mine}s on a given {@link Field}.
 *
 * @author Yannick Kraml.
 */
public interface MinePlacer {

  /**
   * Places {@link Mine}s on the given Field.
   *
   * @param field         Field, on which the {@link Mine}s should be placed.
   * @param possibleMines All possible {@link Mine}s, that can be placed.
   */
  void placeMines(Field field, Collection<Mine> possibleMines);

}
