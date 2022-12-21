package de.unimarburg.profit.algorithm.mineplacer;

import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.enums.ResourceType;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

/**
 * Interface that is responsible to find all {@link Mine}s, that can be placed on a given
 * {@link Field}.
 *
 * @author Yannick Kraml
 */
public interface MinePlaceFinder {

  /**
   * Calculates all {@link Mine}s, that can theoretically be placed on the given {@link Field}.
   * Attention: there is no guarantee, that all {@link Mine}s can be placed at the same time.
   *
   * @param field Field, on which the {@link Mine}s will be placed.
   * @return Map, that contains as keys {@link Mine}s and as values the connected {@link Deposit}.
   */
  Map<Mine, Deposit> calculatePossibleMines(Field field);

  /**
   * Calculates which {@link Deposit}s are connected to which {@link Mine}s.
   *
   * @param possibleMines Map, that contains placed Mines as keys and the connected {@link Deposit}
   *                      as value.
   * @return Map, that contains {@link Deposit}s as keys and Collections of {@link Mine}s as values.
   */
  default Map<Deposit, Collection<Mine>> getMinesFromDeposits(Map<Mine, Deposit> possibleMines) {
    Map<Deposit, Collection<Mine>> depositsWithMines = new HashMap<>();
    possibleMines.values().forEach(deposit -> depositsWithMines.put(deposit, new LinkedList<>()));

    possibleMines.forEach((key, value) -> depositsWithMines.get(value).add(key));

    return depositsWithMines;
  }

  /**
   * Calculates how many resources each {@link Mine} can produce in its life cycle.
   *
   * @param placedMines Map, that contains the placed {@link Mine}s as keys and the connected
   *                    {@link Deposit}s as values.
   * @return Collection of {@link MineResourceAmount}.
   */
  default Collection<MineResourceAmount> calcResourcesPerMine(Map<Mine, Deposit> placedMines) {
    Map<Deposit, Collection<Mine>> minesFromDeposits = getMinesFromDeposits(placedMines);

    Collection<MineResourceAmount> amounts = new HashSet<>();

    minesFromDeposits.forEach((deposit, mines) -> {

      Map<ResourceType, Integer> startResources = deposit.getStartResources();
      ResourceType resourceType = deposit.getResourceType();
      Integer amount = startResources.get(resourceType);

      int amountPerMine = amount / mines.size();

      mines.forEach(mine -> amounts.add(new MineResourceAmount(mine, resourceType, amountPerMine)));

    });

    return amounts;
  }
}
