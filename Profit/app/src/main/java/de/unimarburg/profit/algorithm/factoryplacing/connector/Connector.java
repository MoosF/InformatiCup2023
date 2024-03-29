package de.unimarburg.profit.algorithm.factoryplacing.connector;

import de.unimarburg.profit.model.Conveyor;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.exceptions.CouldNotRemoveObjectException;
import java.util.Collection;

/**
 * Functionality to find connectable {@link Mine}s for a {@link Factory} and to connect a
 * {@link Factory} to a set of {@link Mine}s.
 *
 * @author Fabian Moos
 */
public interface Connector {

  /**
   * Connect the given collection of {@link Mine}s to the factory used by
   * {@code getReachableMines(Factory factory)}..
   *
   * @param minesToConnect A set of {@link Mine}s that must be connected to the given
   *                       {@link Factory}.
   * @return {@code true} if all mines of {@code minesToConnect} can be connected, {@code false}
   *         otherwise.
   */
  boolean connectMines(Collection<Mine> minesToConnect);

  /**
   * Gather all {@link Mine}s that are reachable from a {@link Factory}.
   *
   * @param factory The factory that is reachable from the returned {@link Mine}s.
   * @return a {@link Collection} of {@link Mine}s that can be connected to the given
   *         {@link Factory} by using {@link Conveyor}s.
   */
  Collection<Mine> getReachableMines(Factory factory);

  /**
   * Removes all {@link Conveyor}s from a {@link Field}, that are placed since a specified time.
   * The time is indirectly given by the beforeConveyers.
   *
   * @param field {@link Field}, from which the {@link Conveyor}s should be removed.
   * @param beforeConveyors {@link Conveyor}s, that are placed before the specified time.
   */
  default void removePlacedConveyers(Field field, Collection<Conveyor> beforeConveyors) {
    Collection<Conveyor> afterConveyors = field.getObjectsOfClass(Conveyor.class);
    for (Conveyor conveyor : afterConveyors) {
      if (!beforeConveyors.contains(conveyor)) {
        try {
          field.removeBaseObject(conveyor);
        } catch (CouldNotRemoveObjectException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}
