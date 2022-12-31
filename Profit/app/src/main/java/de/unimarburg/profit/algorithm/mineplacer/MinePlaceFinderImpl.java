package de.unimarburg.profit.algorithm.mineplacer;

import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.enums.MineSubType;
import de.unimarburg.profit.model.enums.ResourceType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This class is able to find all possible {@link Mine}s, that can be placed on a given Field.
 * Attention: it will not be possible to place all {@link Mine}s at the same time, because there
 * will almost always be overlapping between two or more {@link Mine}s.
 *
 * @author Yannick Kraml
 */
public class MinePlaceFinderImpl implements MinePlaceFinder {


  @Override
  public Map<Mine, Deposit> calculatePossibleMines(Field field) {
    Map<Mine, Deposit> placements = new HashMap<>();
    Collection<Deposit> deposits = field.getObjectsOfClass(Deposit.class);
    for (Deposit deposit : deposits) {

      int horPos = deposit.getX();
      int verPos = deposit.getY();
      int width = deposit.getWidth();
      int height = deposit.getHeight();

      //From left to right.
      ResourceType resource = deposit.getResourceType();
      for (int i = horPos; i < horPos + width; i++) {
        placements.put(Mine.createMine(i - 1, verPos - 3, MineSubType.OUTPUT_NORTH), deposit);
        placements.put(Mine.createMine(i + 1, verPos - 2, MineSubType.OUTPUT_EAST), deposit);
        placements.put(Mine.createMine(i, verPos + height + 1, MineSubType.OUTPUT_SOUTH), deposit);
        placements.put(Mine.createMine(i - 2, verPos + height, MineSubType.OUTPUT_WEST), deposit);
      }

      //From top to bottom
      for (int i = verPos; i < verPos + height; i++) {
        placements.put(Mine.createMine(horPos - 2, i - 2, MineSubType.OUTPUT_NORTH), deposit);
        placements.put(Mine.createMine(horPos + width + 1, i - 1, MineSubType.OUTPUT_EAST),
            deposit);
        placements.put(Mine.createMine(horPos + width, i + 1, MineSubType.OUTPUT_SOUTH), deposit);
        placements.put(Mine.createMine(horPos - 3, i, MineSubType.OUTPUT_WEST), deposit);
      }

    }

    placements.keySet().removeIf(o -> !field.baseObjectCanBePlaced(o));

    return placements;
  }
}
