package de.unimarburg.profit.algorithm.mineplacing;

import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Implementation of {@link MinePlacer}. It works with evolutionary algorithms.
 *
 * @author Yannick Kraml.
 */
public class MinePlacerImpl implements MinePlacer {


  @Override
  public Map<Mine, Deposit> placeMines(Field field, Map<Mine, Deposit> possibleMines) {

    Map<Mine, Deposit> placedMines = new HashMap<>();

    for (Entry<Mine, Deposit> entry : possibleMines.entrySet()) {
      Mine mine = entry.getKey();
      Deposit deposit = entry.getValue();
      try {
        field.addBaseObject(mine);
        placedMines.put(mine, deposit);
      } catch (CouldNotPlaceObjectException ignored) {
        //If a single Mines can not be placed. It will just be ignored.
      }
    }

    return placedMines;
  }


}
