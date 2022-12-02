package mineplacer;

import java.util.Collection;
import model.BaseObject;
import model.Field;
import model.Mine;
import model.enums.MineSubType;
import model.exceptions.CouldNotPlaceObjectException;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

public class MinePlacingProblem extends AbstractProblem {

  private final Placement[] possiblePlacements;
  private final Field field;

  public MinePlacingProblem( Field field, Placement[] possiblePlacements) {
    super(1, 1);
    this.possiblePlacements = possiblePlacements;
    this.field = field;
  }

  @Override
  public void evaluate(Solution solution) {
    boolean[] binary = EncodingUtils.getBinary(solution.getVariable(0));

    Field copy = copy(field);

    for (int i = 0; i < binary.length; i++) {
      boolean shouldBePlaced = binary[i];
      Placement possiblePlacement = possiblePlacements[i];

      int horPos = possiblePlacement.getHorPos();
      int verPos = possiblePlacement.getVerPos();
      MineSubType mineSubType = possiblePlacement.getMineSubType();
      Mine mine = Mine.createMine(horPos, verPos, mineSubType);
      if (shouldBePlaced && copy.baseObjectCanBePlaced(mine)) {
        try {
          copy.addBaseObject(mine);
        } catch (CouldNotPlaceObjectException e) {
          throw new RuntimeException(e);
        }
      }

    }

    Collection<Mine> mines = copy.getObjectsOfClass(Mine.class);

    solution.setObjective(0, -mines.size());

  }

  private Field copy(Field field) {
    Field copy = new Field(field.getWidth(), field.getHeight());

    for (BaseObject object : field.getAllObjects()) {
      try {
        copy.addBaseObject(object);
      } catch (CouldNotPlaceObjectException e) {
        throw new RuntimeException(e);
      }
    }

    return copy;
  }

  @Override
  public Solution newSolution() {
    Solution solution = new Solution(numberOfVariables, numberOfObjectives);
    solution.setVariable(0, EncodingUtils.newBinary(possiblePlacements.length));
    return solution;
  }
}
