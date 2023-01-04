package de.unimarburg.profit.algorithm.mineplacing;

import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import java.util.HashMap;
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

  private static final Object SEARCH_DEPTH = 10;

  @Override
  public Map<Mine, Deposit> choosePlaces(Field field, Map<Mine, Deposit> possibleMines) {
    Mine[] mines = possibleMines.keySet().toArray(new Mine[0]);

    NondominatedPopulation population = new Executor()
        .withProblemClass(MinePlacingProblem.class, field, mines, SEARCH_DEPTH)
        .withAlgorithm("PESA2")
        .withMaxTime(5 * 1000)
        .run();

    Solution solution = population.iterator().next();

    boolean[] binary = EncodingUtils.getBinary(solution.getVariable(0));

    Map<Mine, Deposit> minesToBePlaced = new HashMap<>();
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
