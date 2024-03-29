package de.unimarburg.profit.algorithm.mineplacing;

import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import java.util.Map;

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
   * @param possibleMines {@link Mine}s, that should be placed.
   * @return Collection of placed {@link Mine}s.
   */
  Map<Mine, Deposit> placeMines(Field field, Map<Mine, Deposit> possibleMines);

  void removeUselessMines(Field field);
}
