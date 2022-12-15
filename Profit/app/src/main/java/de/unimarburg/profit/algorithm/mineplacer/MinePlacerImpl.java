package de.unimarburg.profit.algorithm.mineplacer;

import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import java.util.Collection;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

public class MinePlacerImpl implements MinePlacer {

  private static final int SEARCH_DEPTH = 20;

  @Override
  public void placeMines(Field field, Collection<Mine> possibleMines) {

    Mine[] mines = possibleMines.toArray(new Mine[0]);

    NondominatedPopulation population = new Executor()
        .withProblemClass(MinePlacingProblem.class, field, mines, SEARCH_DEPTH)
        .withAlgorithm("NSGAII")
        .withMaxTime(5 * 1000)
        .distributeOnAllCores()
        .run();

    Solution solution = population.iterator().next();

    boolean[] binary = EncodingUtils.getBinary(solution.getVariable(0));

    for (int i = 0; i < binary.length; i++) {
      boolean shouldBePlaced = binary[i];
      Mine mine = mines[i];

      if (shouldBePlaced) {
        try {
          field.addBaseObject(mine);
        } catch (CouldNotPlaceObjectException ignore) {
          //Ignore
        }
      }
    }

  }


}
