package de.unimarburg.profit.algorithm.factoryplacing.combination;

import de.unimarburg.profit.algorithm.mineplacing.MineWithResources;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Product;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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
  public Collection<TypeAndMinesCombination> findCombinations(
      Collection<Mine> connectableMines, Collection<MineWithResources> mineWithResources,
      Collection<Product> products, Factory factory) {

    MineWithResources[] connectableMinesArray = mineWithResources.stream()
        .filter(mineResourceAmount -> connectableMines.contains(mineResourceAmount.getMine()))
        .distinct().toArray(MineWithResources[]::new);

    Collection<TypeAndMinesCombination> combinations = new HashSet<>();
    for (Product product : products) {

      NondominatedPopulation population = new Executor()
          .withProblemClass(MineConnectionsChoosingProblem.class, factory, connectableMinesArray,
              product)
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
