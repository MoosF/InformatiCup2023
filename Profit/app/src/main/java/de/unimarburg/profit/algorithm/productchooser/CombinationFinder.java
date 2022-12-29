package de.unimarburg.profit.algorithm.productchooser;

import de.unimarburg.profit.algorithm.mineplacer.MineWithResource;
import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Product;
import java.util.Collection;
import java.util.Map;

/**
 * This interface is responsible to calculate which {@link Mine}s need to be connected to a
 * {@link de.unimarburg.profit.model.Factory} in order to produce a specific {@link Product}.
 *
 * @author Yannick Kraml
 */
public interface CombinationFinder {

  Collection<TypeAndMinesCombination> findProductMinesCombination(
      Map<Mine, Deposit> connectableMines, Collection<MineWithResource> mineWithResources,
      Collection<Product> products,
      Factory factory);
}
