package de.unimarburg.profit.algorithm.factoryplacing.connector;

import de.unimarburg.profit.model.Conveyer;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import java.util.Collection;

/**
 * Functionality to find connectable {@link Mine}s for a {@link Factory} and to connect a
 * {@link Factory} to a set of {@link Mine}s.
 *
 * @author Fabian Moos
 */
public interface Connector {

  /**
   * TODO
   *
   * @param mines TODO
   * @param field TODO
   */
  void connect(Collection<Mine> mines, Field field);

  /**
   * Connect the given collection of {@link Mine}s to the factory used by
   * {@code getReachableMines(Factory factory)}..
   *
   * @param minesToConnect A set of {@link Mine}s that must be connected to the given
   *                       {@link Factory}.
   * @return {@code true} if all mines of {@code minesToConnect} can be connected, {@code false}
   * otherwise.
   */
  boolean connectMines(Collection<Mine> minesToConnect);

  /**
   * Gather all {@link Mine}s that are reachable from a {@link Factory}.
   *
   * @param factory The factory that is reachable from the returned {@link Mine}s.
   * @return a {@link Collection} of {@link Mine}s that can be connected to the given
   * {@link Factory} by using {@link Conveyer}s.
   */
  Collection<Mine> getReachableMines(Factory factory);

  /**
   * Removes all objects that have been placed by
   * {@code connectMines(Collection<Mine> minesToConnect)}.
   */
  void removeAllPlacedObjects();
}
