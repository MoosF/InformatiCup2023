package de.unimarburg.profit.algorithm.mineplacing;

import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.enums.MineSubType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MinePlacerImplTest {

  private final MinePlacer minePlacer = new MinePlacerImpl();
  private Field field;

  @BeforeEach
  public void init() {
    field = new Field(10, 10);
  }

  @Test
  public void test1() {

    Map<Mine, Deposit> possibleMines = new HashMap<>();
    Mine m1 = Mine.createMine(2, 5, MineSubType.OUTPUT_NORTH);
    Mine m2 = Mine.createMine(5, 5, MineSubType.OUTPUT_NORTH);
    Mine m3 = Mine.createMine(8, 5, MineSubType.OUTPUT_NORTH);
    Mine m4 = Mine.createMine(2, 5, MineSubType.OUTPUT_NORTH);

    possibleMines.put(m1, null);
    possibleMines.put(m2, null);
    possibleMines.put(m3, null);
    possibleMines.put(m4, null);

    Map<Mine, Deposit> placedMines = minePlacer.placeMines(field, possibleMines);

    Assertions.assertEquals(3, field.getObjectsOfClass(Mine.class).size());
    Assertions.assertEquals(3, placedMines.size());

  }

}