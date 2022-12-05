package de.uni_marburg.profit.mineplacer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import de.uni_marburg.profit.model.Deposit;
import de.uni_marburg.profit.model.Field;
import de.uni_marburg.profit.model.Mine;
import de.uni_marburg.profit.model.enums.MineSubType;

/**
 * This class is able to find all possible {@link Mine}s, that can be placed on a given Field.
 * Attention: it will not be possible to place all {@link Mine}s at the same time, because there
 * will almost always be overlapping between two or more {@link Mine}s.
 *
 * @author Yannick Kraml
 */
public class MinePlaceFinder {

  private final Mine[] allPlacements;

  /**
   * Constructor of this class.
   *
   * @param field {@link Field}, where to find all possible {@link Mine}s.
   */
  public MinePlaceFinder(Field field) {

    List<Mine> placements = new ArrayList<>();
    Collection<Deposit> deposits = field.getObjectsOfClass(Deposit.class);
    for (Deposit deposit : deposits) {

      int horPos = deposit.getX();
      int verPos = deposit.getY();
      int width = deposit.getWidth();
      int height = deposit.getHeight();

      //From left to right.
      for (int i = horPos; i < horPos + width; i++) {
        placements.add(Mine.createMine(i - 1, verPos - 3, MineSubType.OUTPUT_NORTH));
        placements.add(Mine.createMine(i + 1, verPos - 2, MineSubType.OUTPUT_EAST));
        placements.add(Mine.createMine(i, verPos + height + 1, MineSubType.OUTPUT_SOUTH));
        placements.add(Mine.createMine(i - 2, verPos + height, MineSubType.OUTPUT_WEST));
      }

      //From top to bottom
      for (int i = verPos; i < verPos + height; i++) {
        placements.add(Mine.createMine(horPos - 2, i - 2, MineSubType.OUTPUT_NORTH));
        placements.add(Mine.createMine(horPos + width + 1, i - 1, MineSubType.OUTPUT_EAST));
        placements.add(Mine.createMine(horPos + width, i + 1, MineSubType.OUTPUT_SOUTH));
        placements.add(Mine.createMine(horPos - 3, i, MineSubType.OUTPUT_WEST));
      }

    }

    Predicate<Mine> filter = field::baseObjectCanBePlaced;
    this.allPlacements = placements.stream().filter(filter).toArray(Mine[]::new);
  }


  public Mine[] getAllPossibleMines() {
    return allPlacements;
  }
}
