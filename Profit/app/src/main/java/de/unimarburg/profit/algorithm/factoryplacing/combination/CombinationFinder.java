package de.unimarburg.profit.algorithm.factoryplacing.combination;

import de.unimarburg.profit.algorithm.mineplacing.MineWithResources;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Product;
import java.util.Collection;

/**
 * This interface is responsible to calculate which {@link Mine}s need to be connected to a
 * {@link de.unimarburg.profit.model.Factory} in order to produce a specific {@link Product}.
 *
 * @author Yannick Kraml
 */
public interface CombinationFinder {

  /**
   * Finds possible combinations of {@link Mine}s, that should be connected to a {@link Factory}, in
   * order to produce points.
   *
   * @param connectableMines  {@link Mine}s, that can be connected to the {@link Factory}.
   * @param mineWithResources All {@link Mine}s, that exists on the
   *                          {@link de.unimarburg.profit.model.Field} with their resources.
   * @param products {@link Product}s, that can be produced by the {@link Factory}.
   * @param factory Factory, to which the {@link Mine}s should be connected.
   * @return Possible combinations of {@link Mine}s. 
   */
  Collection<TypeAndMinesCombination> findCombinations(Collection<Mine> connectableMines,
      Collection<MineWithResources> mineWithResources, Collection<Product> products,
      Factory factory);
}
