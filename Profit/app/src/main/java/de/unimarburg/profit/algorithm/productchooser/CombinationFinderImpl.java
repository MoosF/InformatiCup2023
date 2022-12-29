package de.unimarburg.profit.algorithm.productchooser;

import de.unimarburg.profit.algorithm.mineplacer.MineWithResource;
import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Product;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
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
      Map<Mine, Deposit> connectableMines, Collection<MineWithResource> mineWithResources,
      Collection<Product> products, Factory factory) {

    MineWithResource[] connectableMinesArray = mineWithResources.stream()
        .filter(mineResourceAmount -> connectableMines.containsKey(mineResourceAmount.getMine()))
        .distinct().toArray(MineWithResource[]::new);

    Collection<TypeAndMinesCombination> combinations = new HashSet<>();
    for (Product product : products) {

      NondominatedPopulation population = new Executor()
          .withProblemClass(MineConnectionsChoosingProblem.class, factory, connectableMinesArray, product)
          .withAlgorithm("NSGAII")
          .withMaxEvaluations(500)
          .distributeOnAllCores()
          .run();

      for (Solution solution : population) {
        combinations.add(
            MineConnectionsChoosingProblem.convertSolutionToCombination(solution, factory,
                connectableMinesArray,
                product));
      }
    }

    combinations.removeIf(
        typeAndMinesCombination -> typeAndMinesCombination.getMinesWithResources().isEmpty());

    return combinations;
  }
}
