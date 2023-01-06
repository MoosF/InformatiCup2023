package de.unimarburg.profit.algorithm.mineplacing;

import de.unimarburg.profit.model.BaseObject;
import de.unimarburg.profit.model.Conveyer;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.MovableObject;
import de.unimarburg.profit.model.Tile;
import de.unimarburg.profit.model.enums.ConveyorSubType;
import de.unimarburg.profit.model.enums.TileType;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

/**
 * This class represents the mine-placing problem. The problem contains a field and has the
 * objective to place {@link Mine}s as optimal as possible.
 *
 * @author Yannick Kraml
 */
public class MinePlacingProblem extends AbstractProblem {

  private final Mine[] possibleMines;
  private final Field field;
  private final int turns;

  /**
   * Constructor of {@link MinePlacingProblem}.
   *
   * @param field         {@link Field}, where the {@link Mine}s should be placed.
   * @param possibleMines An array of {@link Mine}s, containing all possible {@link Mine}s, that can
   *                      be placed.
   * @param turns         The amount of turns, that the field will be simulated in the future.
   */
  public MinePlacingProblem(Field field, Mine[] possibleMines, int turns) {
    super(1, 1, 0);
    this.possibleMines = possibleMines;
    this.field = field;
    this.turns = turns;
  }

  @Override
  public void evaluate(Solution solution) {
    boolean[] shouldBePlaced = EncodingUtils.getBinary(solution.getVariable(0));

    Field copy = field.copy();
    for (int i = 0; i < shouldBePlaced.length; i++) {
      if (shouldBePlaced[i]) {
        try {
          copy.addBaseObject(possibleMines[i]);
        } catch (CouldNotPlaceObjectException ignored) {
          //Ignore
        }
      }
    }

    int reachingScore = 0;
    for (Mine mine : copy.getObjectsOfClass(Mine.class)) {
      reachingScore += calcReachScore(copy, mine, new HashSet<>(), turns);
    }

    solution.setObjective(0, -reachingScore);
  }

  /**
   * Calculates the reachability score for a given {@link MovableObject}. The reachability score
   * says, how many tiles can be reached from this {@link MovableObject} with the help of
   * conveyors.
   *
   * @param field              Field, where the {@link BaseObject} will be placed.
   * @param object             {@link BaseObject}, from wich the reach score should be calculated.
   * @param reachablePositions Collection of {@link Position}s, that are already reached by a before
   *                           connected {@link BaseObject}.
   * @param turns              Amount of turns, that the {@link Field} will be calculated later.
   * @return Reachability score
   */
  private int calcReachScore(Field field, MovableObject object, Set<Position> reachablePositions,
      int turns) {

    if (turns <= 0) {
      return 0;
    }

    Optional<Tile> optionalOutputTile = Arrays.stream(object.getTiles())
        .filter(tile -> tile.getType().equals(TileType.OUTPUT)).findFirst();

    if (optionalOutputTile.isEmpty()) {
      return 0;
    }

    int outputHorPos = object.getX() + optionalOutputTile.get().getRelHorPos();
    int outputVerPos = object.getY() + optionalOutputTile.get().getRelVerPos();
    Position position = new Position(outputHorPos, outputVerPos);
    if (reachablePositions.contains(position)) {
      return 0;
    }
    reachablePositions.add(position);

    int reachScore = 1;
    for (Position neighborPosition : getValidNeighboringPositions(object, position)) {
      for (ConveyorSubType subtype : ConveyorSubType.values()) {

        Conveyer conveyer = createConveyerFromInputPosition(neighborPosition, subtype);
        if (field.baseObjectCanBePlaced(conveyer)) {
          reachScore += calcReachScore(field, conveyer, reachablePositions, turns - 1);
        }

      }
    }

    return reachScore;
  }

  private Collection<Position> getValidNeighboringPositions(BaseObject object, Position position) {
    Collection<Position> neighbors = new LinkedList<>();
    neighbors.add(new Position(position.horPos() + 1, position.verPos()));
    neighbors.add(new Position(position.horPos(), position.verPos() + 1));
    neighbors.add(new Position(position.horPos() - 1, position.verPos()));
    neighbors.add(new Position(position.horPos(), position.verPos() - 1));

    neighbors.removeIf(position1 -> {
      for (Tile tile : object.getTiles()) {
        int x = object.getX() + tile.getRelHorPos();
        int y = object.getY() + tile.getRelVerPos();
        if (x == position1.horPos() && y == position1.verPos()) {
          return true;
        }
      }
      return false;
    });
    return neighbors;
  }

  private Conveyer createConveyerFromInputPosition(Position inputPosition, ConveyorSubType type) {
    int horPos = inputPosition.horPos();
    int verPos = inputPosition.verPos();

    Conveyer conveyer;
    switch (type) {
      case SHORT_OUTPUT_EAST, LONG_OUTPUT_EAST ->
          conveyer = Conveyer.createConveyor(horPos + 1, verPos, type);
      case SHORT_OUTPUT_SOUTH, LONG_OUTPUT_SOUTH ->
          conveyer = Conveyer.createConveyor(horPos, verPos + 1, type);
      case SHORT_OUTPUT_WEST -> conveyer = Conveyer.createConveyor(horPos - 1, verPos, type);
      case LONG_OUTPUT_WEST -> conveyer = Conveyer.createConveyor(horPos - 2, verPos, type);
      case SHORT_OUTPUT_NORTH -> conveyer = Conveyer.createConveyor(horPos, verPos - 1, type);
      case LONG_OUTPUT_NORTH -> conveyer = Conveyer.createConveyor(horPos, verPos - 2, type);
      default -> throw new IllegalStateException("Unexpected value: " + type);
    }
    return conveyer;
  }


  @Override
  public Solution newSolution() {
    Solution solution = new Solution(numberOfVariables, numberOfObjectives, numberOfConstraints);
    solution.setVariable(0, EncodingUtils.newBinary(possibleMines.length));
    return solution;
  }
}
