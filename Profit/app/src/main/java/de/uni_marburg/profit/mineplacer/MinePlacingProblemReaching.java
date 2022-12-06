package de.uni_marburg.profit.mineplacer;

import de.uni_marburg.profit.model.BaseObject;
import de.uni_marburg.profit.model.Conveyer;
import de.uni_marburg.profit.model.Field;
import de.uni_marburg.profit.model.Mine;
import de.uni_marburg.profit.model.MovableObject;
import de.uni_marburg.profit.model.Tile;
import de.uni_marburg.profit.model.enums.ConveyerSubType;
import de.uni_marburg.profit.model.enums.MineSubType;
import de.uni_marburg.profit.model.enums.TileType;
import de.uni_marburg.profit.model.exceptions.CouldNotPlaceObjectException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

public class MinePlacingProblemReaching extends AbstractProblem {

  private final Mine[] possiblePlacements;
  private final Field field;
  private final int turns;

  public MinePlacingProblemReaching(Field field, Mine[] possibleMines, int turns) {
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

    int reachingScore = 0;

    for (Mine mine : copy.getObjectsOfClass(Mine.class)) {
      HashSet<Position> reachablePositions = new HashSet<>();
      reachingScore += calcReachScore(copy.copy(), mine, reachablePositions);
    }

    solution.setObjective(1, -reachingScore);
  }

  /**
   * Calculates the reachability score for a given {@link MovableObject}. The reachability score
   * says, how many tiles can be reached from this {@link MovableObject} with the help of
   * conveyors.
   *
   * @param field
   * @param object
   * @param reachablePositions
   * @return Reachability score
   */
  private int calcReachScore(Field field, MovableObject object, Set<Position> reachablePositions) {

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

    Collection<Position> neighbors = getNeighbors(object, position);

    int reachScore = 1;
    for (Position neighborPosition : neighbors) {
      for (ConveyerSubType subtype : ConveyerSubType.values()) {

        Conveyer conveyer = createConveyerFromPosition(neighborPosition, subtype);
        if (field.baseObjectCanBePlaced(conveyer)) {
          //field.addBaseObject(conveyer);
          reachScore += calcReachScore(field, conveyer, reachablePositions);

        }

      }
    }

    return reachScore;
  }

  private Collection<Position> getNeighbors(BaseObject object, Position position) {
    Collection<Position> neighbors = new LinkedList<>();
    neighbors.add(new Position(position.getHorPos() + 1, position.getVerPos()));
    neighbors.add(new Position(position.getHorPos(), position.getVerPos() + 1));
    neighbors.add(new Position(position.getHorPos() - 1, position.getVerPos()));
    neighbors.add(new Position(position.getHorPos(), position.getVerPos() - 1));

    neighbors.removeIf(position1 -> {
      for (Tile tile : object.getTiles()) {
        int x = object.getX() + tile.getRelHorPos();
        int y = object.getY() + tile.getRelVerPos();
        if (x == position1.getHorPos() && y == position1.getVerPos()) {
          return true;
        }
      }
      return false;
    });
    return neighbors;
  }

  private Conveyer createConveyerFromPosition(Position neighborPosition, ConveyerSubType subtype) {
    int horPos = neighborPosition.getHorPos();
    int verPos = neighborPosition.getVerPos();

    Conveyer conveyer;
    switch (subtype) {
      case SHORT_OUTPUT_EAST, LONG_OUTPUT_EAST ->
          conveyer = Conveyer.createConveyor(horPos + 1, verPos, subtype);
      case SHORT_OUTPUT_SOUTH, LONG_OUTPUT_SOUTH ->
          conveyer = Conveyer.createConveyor(horPos, verPos + 1, subtype);
      case SHORT_OUTPUT_WEST -> conveyer = Conveyer.createConveyor(horPos - 1, verPos, subtype);
      case LONG_OUTPUT_WEST -> conveyer = Conveyer.createConveyor(horPos - 2, verPos, subtype);
      case SHORT_OUTPUT_NORTH -> conveyer = Conveyer.createConveyor(horPos, verPos - 1, subtype);
      case LONG_OUTPUT_NORTH -> conveyer = Conveyer.createConveyor(horPos, verPos - 2, subtype);
      default -> throw new IllegalStateException("Unexpected value: " + subtype);
    }
    return conveyer;
  }


  @Override
  public Solution newSolution() {
    Solution solution = new Solution(numberOfVariables, numberOfObjectives, numberOfConstraints);
    solution.setVariable(0, EncodingUtils.newBinary(possiblePlacements.length));
    return solution;
  }
}
