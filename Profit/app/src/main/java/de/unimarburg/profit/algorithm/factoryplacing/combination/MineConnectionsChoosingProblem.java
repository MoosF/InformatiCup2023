package de.unimarburg.profit.algorithm.factoryplacing.combination;

import de.unimarburg.profit.algorithm.mineplacing.MineWithResources;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Product;
import java.util.Collection;
import java.util.HashSet;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

/**
 * {@link MineConnectionsChoosingProblem} extends {@link AbstractProblem}. It models the
 * optimization problem of choosing which {@link Mine}s, that should be connected to a single
 * {@link Factory}, that produces a specific {@link Product}. The problem optimizes the amount of
 * {@link Mine}s, the generated Points of the {@link Factory} and the distances between the
 * {@link Mine}s and the {@link Factory}.
 *
 * @author Yannick Kraml
 */
public class MineConnectionsChoosingProblem extends AbstractProblem {

  private final Factory factory;
  private final MineWithResources[] connectableMines;
  private final Product product;

  /**
   * Constructor of {@link MineConnectionsChoosingProblem}.
   *
   * @param factory          {@link Factory}, for which the {@link Mine}s should be choosen.
   * @param connectableMines {@link Mine}s, that can be connected to the {@link Factory}.
   * @param product          {@link Product}, that the Factory will produce.
   */
  public MineConnectionsChoosingProblem(Factory factory, MineWithResources[] connectableMines,
      Product product) {
    super(1, 3);
    this.factory = factory;
    this.connectableMines = connectableMines;
    this.product = product;
  }

  @Override
  public void evaluate(Solution solution) {

    TypeAndMinesCombination combination = convertSolutionToCombination(solution, factory,
        connectableMines,
        product);

    solution.setObjective(0, -combination.getValue());
    solution.setObjective(1, combination.getMinesWithResources().size());
    solution.setObjective(2, combination.getDistances());

  }

  @Override
  public Solution newSolution() {
    Solution solution = new Solution(numberOfVariables, numberOfObjectives, numberOfConstraints);
    solution.setVariable(0, EncodingUtils.newBinary(connectableMines.length));
    return solution;
  }

  /**
   * Converts a solution to the {@link MineConnectionsChoosingProblem} to a
   * {@link TypeAndMinesCombination}.
   *
   * @param solution         {@link Solution}, to be converted.
   * @param factory          {@link Factory}, with that the {@link MineConnectionsChoosingProblem}
   *                         was constructed.
   * @param connectableMines Collection of {@link MineWithResources}, with that the
   *                         {@link MineConnectionsChoosingProblem} was constructed.
   * @param product          {@link Product}, with that the {@link MineConnectionsChoosingProblem}
   *                         was constructed.
   * @return {@link TypeAndMinesCombination}, that contains all information to solve the problem.
   */
  public static TypeAndMinesCombination convertSolutionToCombination(Solution solution,
      Factory factory, MineWithResources[] connectableMines, Product product) {
    boolean[] minesBinary = EncodingUtils.getBinary(solution.getVariable(0));

    Collection<MineWithResources> mines = new HashSet<>();

    for (int i = 0; i < minesBinary.length; i++) {
      if (minesBinary[i]) {
        mines.add(connectableMines[i]);
      }
    }

    return new TypeAndMinesCombination(factory, product, mines);
  }
}
