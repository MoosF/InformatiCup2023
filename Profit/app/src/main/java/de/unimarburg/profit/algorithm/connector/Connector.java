package de.unimarburg.profit.algorithm.connector;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import java.util.Collection;

/**
 * @author Fabian Moos
 */
public interface Connector {

  /**
   *
   * @param mines
   * @param field
   */
  void connect(Collection<Mine> mines, Field field);

  /**
   *
   * @param field
   * @param factory
   * @param minesToConnect
   * @return
   */
  boolean connectMines(Factory factory, Collection<Mine> minesToConnect);

  /**
   *
   * @param field
   * @param factory
   * @return
   */
  Collection<Mine> getReachableMines(Factory factory);
}
