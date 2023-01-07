package de.unimarburg.profit.algorithm.factoryplacing.factory;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import java.util.Collection;
import java.util.Optional;

public class FactoryChooserDistance implements FactoryChooser {

  @Override
  public Optional<Factory> chooseFactory(Field field, Collection<Factory> possibleFactories) {

    Collection<Mine> mines = field.getObjectsOfClass(Mine.class);

    int minDistance = Integer.MAX_VALUE;
    Factory bestFactory = null;
    for (Factory factory : possibleFactories) {
      int distance = mines.stream().mapToInt(mine -> calcDistance(factory, mine)).sum();
      if (distance < minDistance) {
        minDistance = distance;
        bestFactory = factory;
      }

    }

    return Optional.ofNullable(bestFactory);
  }

  private static int calcDistance(Factory factory, Mine mine) {
    return (int) Math.sqrt(
        Math.pow(factory.getX() - mine.getX(), 2) + Math.pow(factory.getY() - mine.getY(), 2));
  }
}
