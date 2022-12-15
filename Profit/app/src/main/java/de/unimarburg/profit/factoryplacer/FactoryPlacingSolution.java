package de.uni.marburg.profit.factoryplacer;

import de.uni.marburg.profit.model.Factory;
import de.uni.marburg.profit.model.Field;
import de.uni.marburg.profit.model.MovableObject;
import java.util.List;
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
