package de.unimarburg.profit.algorithm.mineplacer;

import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import java.util.Collection;

public interface MinePlacer {

  void placeMines(Field field, Collection<Mine> possibleMines);

}
