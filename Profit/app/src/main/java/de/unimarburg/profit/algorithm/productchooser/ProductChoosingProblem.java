package de.unimarburg.profit.algorithm.productchooser;

import de.unimarburg.profit.algorithm.mineplacer.MineResourceAmount;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.enums.ProductType;
import java.util.Collection;
import java.util.HashSet;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

public class ProductChoosingProblem extends AbstractProblem {

  private final MineResourceAmount[] connectableMines;
  private final Product[] products;

  public ProductChoosingProblem(MineResourceAmount[] connectableMines, Product[] products) {
    super(2, 1);
    this.connectableMines = connectableMines;
    this.products = products;
  }

  @Override
  public void evaluate(Solution solution) {

    TypeAndMinesCombination combination = convertSolutionToCombination(solution,
        connectableMines, products);

    solution.setObjective(0, -combination.getValue());

  }

  public static TypeAndMinesCombination convertSolutionToCombination(Solution solution,
      MineResourceAmount[] connectableMines,
      Product[] products) {
    boolean[] minesBinary = EncodingUtils.getBinary(solution.getVariable(0));
    int productIndex = EncodingUtils.getInt(solution.getVariable(1));

    Product product = products[productIndex];
    Collection<MineResourceAmount> mines = new HashSet<>();

    for (int i = 0; i < minesBinary.length; i++) {
      if (minesBinary[i]) {
        mines.add(connectableMines[i]);
      }
    }

    return new TypeAndMinesCombination(product, mines);
  }

  @Override
  public Solution newSolution() {
    Solution solution = new Solution(numberOfVariables, numberOfObjectives, numberOfConstraints);
    solution.setVariable(0, EncodingUtils.newBinary(connectableMines.length));
    solution.setVariable(1, EncodingUtils.newBinaryInt(0, products.length - 1));
    return solution;
  }
}
