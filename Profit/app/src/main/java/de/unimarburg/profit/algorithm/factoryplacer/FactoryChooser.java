package de.unimarburg.profit.algorithm.factoryplacer;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.Tile;
import de.unimarburg.profit.model.enums.ProductType;
import de.unimarburg.profit.model.enums.TileType;
import de.unimarburg.profit.service.Input;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

/**
 * This clas is responsible to calculate which {@link Factory} should be placed next on a given
 * {@link Field}.
 *
 * @author Yannick Kraml
 */
public abstract class FactoryChooser {

  private final Field field;
  private final Collection<Factory> possibleFactories;

  protected FactoryChooser(Field field, Collection<Factory> possibleFactories) {
    this.field = field;
    this.possibleFactories = possibleFactories;
  }

  /**
   * Calculates which {@link Factory} should be placed next.
   *
   * @return Optional of {@link Factory}. Empty, if no {@link Factory} could be found.
   */
  public abstract Optional<Factory> chooseFactory();

  protected Field getField() {
    return field;
  }

  protected Collection<Factory> getPossibleFactories() {
    return possibleFactories;
  }
}
