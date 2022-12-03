package mineplacer;

import model.Field;
import model.Mine;
import model.Tile;
import model.enums.TileType;
import model.exceptions.CouldNotPlaceObjectException;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;
import simulation.SimulateException;
import simulation.Simulator;

public class MinePlacingProblem extends AbstractProblem {

  private final Mine[] possiblePlacements;
  private final Field field;
  private final int turns;

  public MinePlacingProblem(Field field, Mine[] possibleMines, int turns) {
    super(1, 2, 0);
    this.possiblePlacements = possibleMines;
    this.field = field;
    this.turns = turns;
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

    try {
      int points = Simulator.getInstance().simulate(copy, turns);
      solution.setObjective(0, -points);
    } catch (SimulateException e) {
      solution.setObjective(0, 0);
    }

    int uselessCount = 0;
    for (Mine mine : copy.getObjectsOfClass(Mine.class)) {
      for (Tile tile : mine.getTiles()) {

        int horPos = mine.getX() + tile.getRelHorPos();
        int verPos = mine.getY() + tile.getRelVerPos();

        if (tile.getType().equals(TileType.OUTPUT) && hasNeighborToOutputTo(copy, horPos, verPos)) {
          uselessCount++;
        }
      }
    }

    solution.setObjective(1, uselessCount);
  }

  private boolean hasNeighborToOutputTo(Field copy, int horPos, int verPos) {
    boolean hasNoOutput = true;
    hasNoOutput = hasOutPutToNeighbor(copy, horPos + 1, verPos, hasNoOutput);
    hasNoOutput = hasOutPutToNeighbor(copy, horPos, verPos + 1, hasNoOutput);
    hasNoOutput = hasOutPutToNeighbor(copy, horPos - 1, verPos, hasNoOutput);
    hasNoOutput = hasOutPutToNeighbor(copy, horPos, verPos - 1, hasNoOutput);
    return hasNoOutput;
  }


  private boolean hasOutPutToNeighbor(Field copy, int horPos, int verPos, boolean hasNoOutput) {
    try {
      if (copy.getTiles()[horPos][verPos].getType().equals(TileType.INPUT)) {
        hasNoOutput = false;
      }
    } catch (Exception e) {

    }
    return hasNoOutput;
  }

  @Override
  public Solution newSolution() {
    Solution solution = new Solution(numberOfVariables, numberOfObjectives, numberOfConstraints);
    solution.setVariable(0, EncodingUtils.newBinary(possiblePlacements.length));
    return solution;
  }
}
