package de.unimarburg.profit.algorithm.factoryplacing.factory;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;

/**
 * This interface is responsible for placing and removing {@link Factory}s from {@link Field}.
 *
 * @author Yannick
 */
public interface FactoryPlacer {

  boolean placeFactory(Field field, Factory factory);

  boolean removeFactory(Field field, Factory factory);
}
