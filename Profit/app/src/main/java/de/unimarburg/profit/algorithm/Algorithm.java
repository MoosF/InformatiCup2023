package de.unimarburg.profit.algorithm;

import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.MovableObject;
import java.util.Collection;

public interface Algorithm {
  Collection<MovableObject> runAlgorithm(Field field, int time, int turns);
}
