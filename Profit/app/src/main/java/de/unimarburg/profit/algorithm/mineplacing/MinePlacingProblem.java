package de.unimarburg.profit.algorithm.mineplacing;

import de.unimarburg.profit.model.BaseObject;
import de.unimarburg.profit.model.Conveyer;
import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.MovableObject;
import de.unimarburg.profit.model.Tile;
import de.unimarburg.profit.model.enums.ConveyorSubType;
import de.unimarburg.profit.model.enums.TileType;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
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

  private final Mine[] possibleMinesArray;
  private final Field field;
  private final int turns;
  private final Map<Mine, Deposit> possibleMines;

  /**
   * Constructor of {@link MinePlacingProblem}.
   *
   * @param field         {@link Field}, where the {@link Mine}s should be placed.
   * @param possibleMines An array of {@link Mine}s, containing all possible {@link Mine}s, that can
   *                      be placed.
   * @param turns         The amount of turns, that the field will be simulated in the future.
   */
  public MinePlacingProblem(Field field, int turns, Map<Mine, Deposit> possibleMines,
      Mine[] possibleMinesArray) {
    super(1, 3, 0);
    this.possibleMinesArray = possibleMinesArray;
    this.field = field;
    this.turns = 5;
    this.possibleMines = possibleMines;
  }

  @Override
  public void evaluate(Solution solution) {
    boolean[] shouldBePlaced = EncodingUtils.getBinary(solution.getVariable(0));

    Field copy = field.copy();
    for (int i = 0; i < shouldBePlaced.length; i++) {
      if (shouldBePlaced[i]) {
        try {
          copy.addBaseObject(possibleMinesArray[i]);
        } catch (CouldNotPlaceObjectException ignored) {
          //Ignore
        }
      }
    }

    int reachingScore = 0;
    Map<Mine, Deposit> placedMines = new HashMap<>();
    copy.getObjectsOfClass(Mine.class)
        .forEach(mine -> placedMines.put(mine, possibleMines.get(mine)));
    for (Mine mine : placedMines.keySet()) {
      reachingScore += calcReachScore(copy, new LinkedList<>(), mine, turns);
      //reachingScore += calcReachScore(copy, mine, new HashSet<>(), turns);
    }

    int minedResourcesSum = 0;
    for (MineWithResources minesWithResource : new MinePlaceFinderImpl().calcResourcesFromMines(
        placedMines)) {
      minedResourcesSum += minesWithResource.getAmount();
    }

    solution.setObjective(0, -reachingScore);
    solution.setObjective(1, -minedResourcesSum);
    solution.setObjective(2, placedMines.size());
  }

  private int calcReachScore(Field field, Collection<Conveyer> path, BaseObject lastPlacedObject,
      int turns) {

    if (turns <= 0) {
      return 0;
    }

    Optional<Tile> optionalOutputTile = Arrays.stream(lastPlacedObject.getTiles())
        .filter(tile -> tile.getType().equals(TileType.OUTPUT)).findFirst();

    if (optionalOutputTile.isEmpty()) {
      return 0;
    }

    int outputHorPos = lastPlacedObject.getX() + optionalOutputTile.get().getRelHorPos();
    int outputVerPos = lastPlacedObject.getY() + optionalOutputTile.get().getRelVerPos();
    Position position = new Position(outputHorPos, outputVerPos);

    int reachScore = 1;
    for (Position neighborPosition : getValidNeighboringPositions(lastPlacedObject, position)) {

      for (ConveyorSubType subtype : ConveyorSubType.values()) {

        Conveyer conveyer = createConveyerFromInputPosition(neighborPosition, subtype);
        if (field.baseObjectCanBePlaced(conveyer) && !intersect(path, conveyer)) {
          //Collection<Conveyer> newPath = new HashSet<>(path);
          //newPath.add(conveyer);
          path.add(conveyer);
          reachScore += calcReachScore(field, path, conveyer, turns - 1);
        }

      }
    }

    return reachScore;
  }

  private boolean intersect(Collection<Conveyer> path, Conveyer conveyer) {
    for (Conveyer pathConveyor : path) {
      for (Tile pathConveyerTile : pathConveyor.getTiles()) {
        int x1 = pathConveyor.getX() + pathConveyerTile.getRelHorPos();
        int y1 = pathConveyor.getY() + pathConveyerTile.getRelVerPos();

        for (Tile conveyerTile : conveyer.getTiles()) {

          int x2 = conveyer.getX() + conveyerTile.getRelHorPos();
          int y2 = conveyer.getY() + conveyerTile.getRelVerPos();

          boolean bothAreCrossable =
              pathConveyerTile.getType().equals(TileType.CROSSABLE) && conveyerTile.getType()
                  .equals(TileType.CROSSABLE);
          boolean atTheSameLocation = x1 == x2 && y1 == y2;
          if (atTheSameLocation && !bothAreCrossable) {
            return true;
          }

        }
      }
    }
    return false;
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
    solution.setVariable(0, EncodingUtils.newBinary(possibleMinesArray.length));
    return solution;
  }
}
