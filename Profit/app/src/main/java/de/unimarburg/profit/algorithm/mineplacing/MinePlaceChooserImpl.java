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
 * Implementierung von {@link MinePlaceChooser}.
 *
 * @author Yannick Kraml
 */
public class MinePlaceChooserImpl implements MinePlaceChooser {

  private static final Object SEARCH_DEPTH = 20;

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
