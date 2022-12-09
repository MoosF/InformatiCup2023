package de.unimarburg.profit.mineplacer;

import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Tile;
import de.unimarburg.profit.model.enums.TileType;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import de.unimarburg.profit.simulation.SimulateException;
import de.unimarburg.profit.simulation.Simulator;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

/**
 * This class represents the mine-placing problem. The problem contains a field and has the
 * objective to place {@link Mine}s as optimal as possible.
 *
 * @author Yannick Kraml
 */
public class MinePlacingProblemPoints extends AbstractProblem {

  private final Mine[] possiblePlacements;
  private final Field field;
  private final int turns;

  /**
   * Constructor of {@link MinePlacingProblemPoints}.
   *
   * @param field         {@link Field}, where the {@link Mine}s should be placed.
   * @param possibleMines An array of {@link Mine}s, containing all possible {@link Mine}s, that can
   *                      be placed.
   * @param turns         The amount of turns, that the field will be simulated in the future.
   */
  public MinePlacingProblemPoints(Field field, Mine[] possibleMines, int turns) {
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
        } catch (CouldNotPlaceObjectException e) {
          throw new RuntimeException(e);
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


  private boolean hasOutPutToNeighbor(Field field, int horPos, int verPos, boolean hasNoOutput) {

    Tile[][] array = field.getTiles();
    if (horPos >= 0 && verPos >= 0 && horPos < array.length && horPos <= array[0].length) {
      if (array[horPos][verPos].getType().equals(TileType.INPUT)) {
        hasNoOutput = false;
      }
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
