package mineplacer;

import model.Field;
import model.Mine;
import model.exceptions.CouldNotPlaceObjectException;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

public class MinePlacingProblem extends AbstractProblem {

  private final Mine[] possiblePlacements;
  private final Field field;

  public MinePlacingProblem(Field field, Mine[] possibleMines) {
    super(1, 1);
    this.possiblePlacements = possibleMines;
    this.field = field;
  }

  @Override
  public void evaluate(Solution solution) {
    boolean[] binary = EncodingUtils.getBinary(solution.getVariable(0));

    Field copy = field.copy();

    for (int i = 0; i < binary.length; i++) {
      boolean shouldBePlaced = binary[i];
      Mine mine = possiblePlacements[i];

      if (shouldBePlaced) {
        try {
          copy.addBaseObject(mine);
        } catch (CouldNotPlaceObjectException ignored) {

        }
      }
    }

    int mineCount = copy.getObjectsOfClass(Mine.class).size();
    solution.setObjective(0, -mineCount);

  }

  @Override
  public Solution newSolution() {
    Solution solution = new Solution(numberOfVariables, numberOfObjectives);
    solution.setVariable(0, EncodingUtils.newBinary(possiblePlacements.length));
    return solution;
  }
}
