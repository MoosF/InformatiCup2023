package de.unimarburg.profit.algorithm.productchooser;

import de.unimarburg.profit.algorithm.mineplacer.MineWithResources;
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

  Collection<TypeAndMinesCombination> findCombinations(
      Collection<Mine> connectableMines, Collection<MineWithResources> mineWithResources,
      Collection<Product> products,
      Factory factory);
}
