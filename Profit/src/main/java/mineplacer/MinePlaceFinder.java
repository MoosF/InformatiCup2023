package mineplacer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import model.Deposit;
import model.Field;
import model.Mine;
import model.enums.MineSubType;

public class MinePlaceFinder {

  private final Placement[] allPlacements;

  public MinePlaceFinder(Field field) {

    List<Placement> placements = new ArrayList<>();
    Collection<Deposit> deposits = field.getObjectsOfClass(Deposit.class);
    for (Deposit deposit : deposits) {

      int horPos = deposit.getX();
      int verPos = deposit.getY();
      int width = deposit.getWidth();
      int height = deposit.getHeight();

      //From left to right.
      for (int i = horPos; i < horPos + width; i++) {
        placements.add(new Placement(i - 1, verPos - 3, MineSubType.OUTPUT_NORTH));
        placements.add(new Placement(i + 1, verPos - 2, MineSubType.OUTPUT_EAST));
        placements.add(new Placement(i, verPos + height + 1, MineSubType.OUTPUT_SOUTH));
        placements.add(new Placement(i - 2, verPos + height, MineSubType.OUTPUT_WEST));
      }

      //From top to bottom
      for (int i = verPos; i < verPos + height; i++) {
        placements.add(new Placement(horPos - 2, i - 2, MineSubType.OUTPUT_NORTH));
        placements.add(new Placement(horPos + width + 1, i - 1, MineSubType.OUTPUT_EAST));
        placements.add(new Placement(horPos + width, i + 1, MineSubType.OUTPUT_SOUTH));
        placements.add(new Placement(horPos - 3, i, MineSubType.OUTPUT_WEST));
      }

    }

    Predicate<Placement> filter = placement -> {
      int horPos = placement.getHorPos();
      int verPos = placement.getVerPos();
      MineSubType mineSubType = placement.getMineSubType();
      Mine mine = Mine.createMine(horPos, verPos, mineSubType);
      return field.baseObjectCanBePlaced(mine);
    };

    this.allPlacements = placements.stream().filter(filter).toArray(Placement[]::new);


  }


  public Placement[] getAllPlacements() {
    return allPlacements;
  }
}
