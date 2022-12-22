package de.unimarburg.profit.algorithm.productchooser;

import de.unimarburg.profit.algorithm.mineplacer.MineResourcePair;
import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Product;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Predicate;
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
      Map<Mine, Deposit> connectableMines, Collection<MineResourcePair> mineResourcePairs,
      Collection<Product> products) {

    MineResourcePair[] connectableMinesArray = mineResourcePairs.stream()
        .filter(mineResourceAmount -> connectableMines.containsKey(mineResourceAmount.getMine()))
        .distinct().toArray(MineResourcePair[]::new);

    Collection<TypeAndMinesCombination> combinations = new HashSet<>();
    for (Product product : products) {

      NondominatedPopulation population = new Executor()
          .withProblemClass(ProductChoosingProblem.class, connectableMinesArray, product)
          .withAlgorithm("NSGAII")
          .withMaxEvaluations(500)
          .distributeOnAllCores()
          .run();

      for (Solution solution : population) {
        combinations.add(
            ProductChoosingProblem.convertSolutionToCombination(solution, connectableMinesArray,
                product));
      }
    }

    combinations.removeIf(
        typeAndMinesCombination -> typeAndMinesCombination.getMineResourcePairs().isEmpty());

    return combinations;
  }
}
