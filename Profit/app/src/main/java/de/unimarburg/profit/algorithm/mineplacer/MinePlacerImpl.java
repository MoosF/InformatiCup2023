package de.unimarburg.profit.algorithm.mineplacer;

import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

/**
 * Implementation of {@link MinePlacer}. It works with evolutionary algorithms.
 *
 * @author Yannick Kraml.
 */
public class MinePlacerImpl implements MinePlacer {

  private static final int SEARCH_DEPTH = 20;

  @Override
  public Map<Mine, Deposit> placeMines(Field field, Map<Mine, Deposit> possibleMines) {

    Mine[] mines = possibleMines.keySet().toArray(new Mine[0]);

    NondominatedPopulation population = new Executor()
        .withProblemClass(MinePlacingProblem.class, field, mines, SEARCH_DEPTH)
        .withAlgorithm("NSGAII")
        .withMaxTime(5 * 1000)
        .distributeOnAllCores()
        .run();

    Solution solution = population.iterator().next();

    boolean[] binary = EncodingUtils.getBinary(solution.getVariable(0));

    Map<Mine, Deposit> placedMines = new HashMap<>();
    for (int i = 0; i < binary.length; i++) {
      boolean shouldBePlaced = binary[i];
      Mine mine = mines[i];

      if (shouldBePlaced) {
        try {
          field.addBaseObject(mine);
          placedMines.put(mine, possibleMines.get(mine));
        } catch (CouldNotPlaceObjectException ignore) {
          //Ignore
        }
      }
    }

    return placedMines;
  }


}
