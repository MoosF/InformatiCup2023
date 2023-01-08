package de.unimarburg.profit.algorithm.mineplacing;

import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import java.util.Collection;
import java.util.Map;

/**
 * This interface is responsible for choosing which {@link Mine}s should be placed from a given Map
 * of {@link Mine}s, that can be placed theoretically.
 *
 * @author Yannick Kraml
 */
public interface MinePlaceChooser {

  /**
   * Chooses which {@link Mine}s should be placed from a Map of {@link Mine}s, that can
   * theoretically be placed.
   *
   * @param field         {@link Field}, on which the {@link Mine}s should be placed.
   * @param possibleMines {@link Map}, that contains {@link Mine}s as keys and the {@link Deposit},
   *                      that are connected to the {@link Mine}s as values.
   * @return Collection, that contains possible placements of {@link Mine}s.
   */
  Collection<Map<Mine, Deposit>> choosePlacements(Field field, Map<Mine, Deposit> possibleMines);

}
