package mineplacer;

import java.util.Collection;
import model.Field;
import model.Mine;
import model.enums.MineSubType;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

public class MinePlacingProblem extends AbstractProblem {

  private final Placement[] possiblePlacements;
  private final Field field;

  public MinePlacingProblem(Field field, Placement[] possiblePlacements) {
    super(1, 1);
    this.possiblePlacements = possiblePlacements;
    this.field = field;
  }

  @Override
  public void evaluate(Solution solution) {
    boolean[] binary = EncodingUtils.getBinary(solution.getVariable(0));
    int mineCount = 0;

    Field copy = field.copy();

    for (int i = 0; i < binary.length; i++) {
      boolean shouldBePlaced = binary[i];
      Placement possiblePlacement = possiblePlacements[i];

      int horPos = possiblePlacement.getHorPos();
      int verPos = possiblePlacement.getVerPos();
      MineSubType mineSubType = possiblePlacement.getMineSubType();
      Mine mine = Mine.createMine(horPos, verPos, mineSubType);
      if (shouldBePlaced && copy.baseObjectCanBePlaced(mine)) {
        mineCount++;
      }

    }

    solution.setObjective(0, -mineCount);

  }

  @Override
  public Solution newSolution() {
    Solution solution = new Solution(numberOfVariables, numberOfObjectives);
    solution.setVariable(0, EncodingUtils.newBinary(possiblePlacements.length));
    return solution;
  }
}
