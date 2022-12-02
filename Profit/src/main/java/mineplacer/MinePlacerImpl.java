package mineplacer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import model.Deposit;
import model.Field;
import model.Mine;
import model.enums.MineSubType;
import model.exceptions.CouldNotPlaceObjectException;
import model.exceptions.CouldNotRemoveObjectException;

public final class MinePlacerImpl extends MinePlacer {

  private final Placement[] allPossibilities;


  public MinePlacerImpl(Field field) {
    super(field);
    List<Placement> allPossibilities = new ArrayList<>();

    Collection<Deposit> deposits = field.getObjectsOfClass(Deposit.class);
    for (Deposit deposit : deposits) {

      int horPos = deposit.getX();
      int verPos = deposit.getY();
      int width = deposit.getWidth();
      int height = deposit.getHeight();

      //From left to right.
      for (int i = horPos; i < horPos + width; i++) {
        allPossibilities.add(new Placement(i - 1, verPos - 3, MineSubType.OUTPUT_NORTH));
        allPossibilities.add(new Placement(i + 1, verPos - 2, MineSubType.OUTPUT_EAST));
        allPossibilities.add(new Placement(i, verPos + height + 1, MineSubType.OUTPUT_SOUTH));
        allPossibilities.add(new Placement(i - 2, verPos + height, MineSubType.OUTPUT_WEST));
      }

      //From top to bottom
      for (int i = verPos; i < verPos + height; i++) {
        allPossibilities.add(new Placement(horPos - 2, i - 2, MineSubType.OUTPUT_NORTH));
        allPossibilities.add(new Placement(horPos + width + 1, i - 1, MineSubType.OUTPUT_EAST));
        allPossibilities.add(new Placement(horPos + width, i + 1, MineSubType.OUTPUT_SOUTH));
        allPossibilities.add(new Placement(horPos - 3, i, MineSubType.OUTPUT_WEST));
      }

    }

    this.allPossibilities = allPossibilities.toArray(new Placement[0]);


  }

  @Override
  public boolean placeRandomMine() throws CouldNotPlaceObjectException {

    Field field = getField();
    List<Placement> filteredPlacements = new ArrayList<>();
    for (Placement placement : allPossibilities) {
      int horPos = placement.getHorPos();
      int verPos = placement.getVerPos();
      MineSubType mineSubType = placement.getMineSubType();
      if (field.baseObjectCanBePlaced(Mine.createMine(horPos, verPos, mineSubType))) {
        filteredPlacements.add(placement);
      }
    }

    if (filteredPlacements.isEmpty()) {
      return false;
    }

    int index = (int) (Math.random() * filteredPlacements.size());
    Placement randomPlacement = filteredPlacements.get(index);

    int horPos = randomPlacement.getHorPos();
    int verPos = randomPlacement.getVerPos();
    MineSubType subType = randomPlacement.getMineSubType();
    field.addBaseObject(Mine.createMine(horPos, verPos, subType));

    return true;
  }

  @Override
  public boolean removeRandomMine() throws CouldNotRemoveObjectException {
    Field field = getField();
    List<Mine> mines = new ArrayList<>(field.getObjectsOfClass(Mine.class));

    if (mines.isEmpty()) {
      return false;
    }

    Collections.shuffle(mines);
    Mine randomMine = mines.iterator().next();
    field.removeBaseObject(randomMine);

    return true;
  }

  public Placement[] getAllPossibilities() {
    return allPossibilities;
  }
}
