package de.unimarburg.profit.algorithm.factoryplacer;

import de.unimarburg.profit.model.Field;
import org.moeaframework.core.Solution;

public class FactoryPlacingSolution extends Solution {

  public Field getField() {
    return field;
  }

  private Field field;

  public FactoryPlacingSolution(int numberOfVariables, int numberOfObjectives, Field field) {
    super(numberOfVariables, numberOfObjectives);
    this.field = field;
  }
}
