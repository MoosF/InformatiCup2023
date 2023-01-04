package de.unimarburg.profit.algorithm.mineplacing;

import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link MinePlacer}. It works with evolutionary algorithms.
 *
 * @author Yannick Kraml.
 */
public class MinePlacerImpl implements MinePlacer {


  @Override
  public Map<Mine, Deposit> placeMines(Field field, Map<Mine, Deposit> possibleMines) {

    Map<Mine, Deposit> placedMines = new HashMap<>();

    possibleMines.forEach((mine, deposit) -> {
      try {
        field.addBaseObject(mine);
      } catch (CouldNotPlaceObjectException ignored) {
        //If a single Mines can not be placed. It will just be ignored.
      }
    });

    return placedMines;
  }


}
