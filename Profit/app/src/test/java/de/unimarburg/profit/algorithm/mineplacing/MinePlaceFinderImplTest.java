package de.unimarburg.profit.algorithm.mineplacing;

import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.enums.MineSubType;
import de.unimarburg.profit.model.enums.ResourceType;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MinePlaceFinderImplTest {

  private final MinePlaceFinder minePlaceFinder = new MinePlaceFinderImpl();
  private final Field field = new Field(100, 100);

  @BeforeEach
  public void initField() throws CouldNotPlaceObjectException {
    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 0, 0, 10, 10));
    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 30, 20, 5, 5));
  }

  @Test
  public void testAllMinesArePlaceable() {
    Map<Mine, Deposit> mines = minePlaceFinder.calculatePossibleMines(field);
    mines.forEach((mine, deposit) -> Assertions.assertTrue(field.baseObjectCanBePlaced(mine)));
  }

  @Test
  public void testMinesCount() {
    Map<Mine, Deposit> mines = minePlaceFinder.calculatePossibleMines(field);
    Assertions.assertEquals(36 + 40, mines.size());
  }

  @Test
  public void test() {

    Deposit d1 = Deposit.createDeposit(ResourceType.ZERO, 0, 0, 5, 5);
    Deposit d2 = Deposit.createDeposit(ResourceType.ONE, 0, 0, 3, 3);

    Map<Mine, Deposit> placedMines = new HashMap<>();
    placedMines.put(Mine.createMine(0, 0, MineSubType.OUTPUT_EAST), d1);
    placedMines.put(Mine.createMine(0, 0, MineSubType.OUTPUT_EAST), d1);
    placedMines.put(Mine.createMine(0, 0, MineSubType.OUTPUT_EAST), d1);
    placedMines.put(Mine.createMine(0, 0, MineSubType.OUTPUT_EAST), d2);
    placedMines.put(Mine.createMine(0, 0, MineSubType.OUTPUT_EAST), d2);

    Collection<MineWithResources> minesWithResources = minePlaceFinder.calcResourcesPerMine(
        placedMines);

    Assertions.assertEquals(5, minesWithResources.size());

    List<MineWithResources> ordered = new LinkedList<>(minesWithResources);
    ordered.sort(Comparator.comparingInt(MineWithResources::getAmount));

    Assertions.assertEquals(9 * 5 / 2, ordered.get(0).getAmount());
    Assertions.assertEquals(9 * 5 / 2, ordered.get(1).getAmount());
    Assertions.assertEquals(25 * 5 / 3, ordered.get(2).getAmount());
    Assertions.assertEquals(25 * 5 / 3, ordered.get(3).getAmount());
    Assertions.assertEquals(25 * 5 / 3, ordered.get(4).getAmount());

    Assertions.assertEquals(ResourceType.ONE, ordered.get(0).getResourceType());
    Assertions.assertEquals(ResourceType.ONE, ordered.get(1).getResourceType());
    Assertions.assertEquals(ResourceType.ZERO, ordered.get(2).getResourceType());
    Assertions.assertEquals(ResourceType.ZERO, ordered.get(3).getResourceType());
    Assertions.assertEquals(ResourceType.ZERO, ordered.get(4).getResourceType());
  }

}