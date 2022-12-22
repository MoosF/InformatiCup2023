package de.unimarburg.profit.algorithm.productchooser;

import de.unimarburg.profit.algorithm.mineplacer.MineResourcePair;
import de.unimarburg.profit.model.Product;
import java.util.Collection;
import java.util.HashSet;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

public class ProductChoosingProblem extends AbstractProblem {

  private final MineResourcePair[] connectableMines;
  private final Product product;

  public ProductChoosingProblem(MineResourcePair[] connectableMines, Product product) {
    super(1, 2);
    this.connectableMines = connectableMines;
    this.product = product;
  }

  @Override
  public void evaluate(Solution solution) {

    TypeAndMinesCombination combination = convertSolutionToCombination(solution, connectableMines,
        product);

    solution.setObjective(0, -combination.getValue());
    solution.setObjective(1, combination.getMineResourcePairs().size());

  }

  @Override
  public Solution newSolution() {
    Solution solution = new Solution(numberOfVariables, numberOfObjectives, numberOfConstraints);
    solution.setVariable(0, EncodingUtils.newBinary(connectableMines.length));
    return solution;
  }

  public static TypeAndMinesCombination convertSolutionToCombination(Solution solution,
      MineResourcePair[] connectableMines, Product product) {
    boolean[] minesBinary = EncodingUtils.getBinary(solution.getVariable(0));

    Collection<MineResourcePair> mines = new HashSet<>();

    for (int i = 0; i < minesBinary.length; i++) {
      if (minesBinary[i]) {
        mines.add(connectableMines[i]);
      }
    }

    return new TypeAndMinesCombination(product, mines);
  }
}
