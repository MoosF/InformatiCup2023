package de.unimarburg.profit.algorithm.mineplacing;

import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

/**
 * Implementation of {@link MinePlaceChooser}.
 *
 * @author Yannick Kraml
 */
public class MinePlaceChooserImpl implements MinePlaceChooser {

  private static final Object SEARCH_DEPTH = 20;

  /**
   * Chooses which {@link Mine}s should be placed from a Map of {@link Mine}s, that can
   * theoretically be placed.
   *
   * @param field         {@link Field}, on which the {@link Mine}s should be placed.
   * @param possibleMines {@link Map}, that contains {@link Mine}s as keys and the {@link Deposit},
   *                      that are connected to the {@link Mine}s as values.
   * @return Collection, that contains possible placements of {@link Mine}s.
   */
  public Collection<Map<Mine, Deposit>> choosePlacements(Field field,
      Map<Mine, Deposit> possibleMines) {

    Mine[] possibleMinesArray = possibleMines.keySet().toArray(new Mine[0]);

    NondominatedPopulation population = new Executor()
        .withProblemClass(MinePlacingProblem.class, field, SEARCH_DEPTH, possibleMines,
            possibleMinesArray)
        .withAlgorithm("NSGAII")
        .withMaxTime(5 * 1000)
        .distributeOnAllCores()
        .run();

    Collection<Map<Mine, Deposit>> placements = new LinkedList<>();

    for (Solution solution : population) {
      Map<Mine, Deposit> placement = createPlacementFromSolution(possibleMines, possibleMinesArray,
          solution);
      placements.add(placement);
    }

    return placements;
  }

  private static Map<Mine, Deposit> createPlacementFromSolution(Map<Mine, Deposit> possibleMines,
      Mine[] mines, Solution solution) {
    Map<Mine, Deposit> minesToBePlaced = new HashMap<>();
    boolean[] binary = EncodingUtils.getBinary(solution.getVariable(0));
    for (int i = 0; i < binary.length; i++) {
      boolean shouldBePlaced = binary[i];
      Mine mine = mines[i];

      if (shouldBePlaced) {
        minesToBePlaced.put(mine, possibleMines.get(mine));
      }
    }
    return minesToBePlaced;
  }
}
