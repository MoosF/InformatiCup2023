package de.unimarburg.profit.algorithm.mineplacer;

import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.enums.ResourceType;
import java.util.Map;

/**
 * Interface that is responsible to find all {@link Mine}s, that can be placed on a given
 * {@link Field}.
 *
 * @author Yannick Kraml
 */
public interface MinePlaceFinder {

  /**
   * Calculates all {@link Mine}s, that can theoretically be placed on the given {@link Field}.
   * Attention: there is no guarantee, that all {@link Mine}s can be placed at the same time.
   *
   * @param field Field, on which the {@link Mine}s will be placed.
   * @return Map, that contains as keys {@link Mine}s and as values the resources they produce.
   */
  Map<Mine, ResourceType> calculatePossibleMines(Field field);

}
