package de.unimarburg.profit.algorithm.productchooser;

import de.unimarburg.profit.algorithm.mineplacer.MineResourceAmount;
import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Product;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

/**
 * Implementation of {@link CombinationFinder}.
 *
 * @author Yannick Kraml
 */
public class CombinationFinderImpl implements CombinationFinder {


  @Override
  public Collection<TypeAndMinesCombination> findProductMinesCombination(
      Map<Mine, Deposit> connectableMines, Collection<MineResourceAmount> mineResourceAmounts,
      Collection<Product> products) {

    MineResourceAmount[] connectableMinesArray = mineResourceAmounts.stream()
        .filter(mineResourceAmount -> connectableMines.containsKey(mineResourceAmount.getMine()))
        .distinct().toArray(MineResourceAmount[]::new);

    Product[] productArray = products.toArray(new Product[0]);

    NondominatedPopulation population = new Executor().withProblemClass(
            ProductChoosingProblem.class, connectableMinesArray, productArray).withAlgorithm("NSGAII")
        .withMaxEvaluations(1000).distributeOnAllCores().run();

    Collection<TypeAndMinesCombination> collection = new HashSet<>();

    for (Solution solution : population) {
      collection.add(
          ProductChoosingProblem.convertSolutionToCombination(solution, connectableMinesArray,
              productArray));
    }

    return collection;
  }
}
