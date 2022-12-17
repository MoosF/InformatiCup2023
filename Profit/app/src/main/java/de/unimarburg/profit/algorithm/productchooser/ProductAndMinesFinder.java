package de.unimarburg.profit.algorithm.productchooser;

import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.enums.ResourceType;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This interface is responsible to calculate which {@link Mine}s need to be connected to a
 * {@link de.unimarburg.profit.model.Factory} in order to produce a specific {@link Product}.
 *
 * @author Yannick Kraml
 */
public interface ProductAndMinesFinder {


  /**
   * Calculates for each {@link Product}, which {@link Mine}s produces resources, that are needed
   * for the production of the given {@link Product}.
   *
   * @param connectableMines A {@link Map} containing as kays all {@link Mine}s, that should be
   *                         taken into consideration and as values the resources each {@link Mine}
   *                         produces.
   * @param products         All {@link Product}s, that can be produced.
   * @return List of {@link TypeAndMinesCombination}.
   */
  List<TypeAndMinesCombination> findProductMinesCombination(
      Map<Mine, ResourceType> connectableMines, Collection<Product> products);


}
