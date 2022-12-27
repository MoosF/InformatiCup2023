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

  public Collection<Factory> calculatePossibleFactories(Input input) {
    // benötigte Typen von Factories bestimmen
    Collection<Product> products = new LinkedList<>(input.getProducts());

    // mögliche Plätze für Fabriken bestimmen
    Collection<Factory> result = new LinkedList<>();
    Tile[][] tiles = field.getTiles();
    for (int i = 0; i < field.getWidth(); i++)
      for (int j = 0; j < field.getWidth(); j++) {
        boolean possiblePlace = true;
        for (int x = 0; x < 5; x++) {
          for (int y = 0; y < 5; y++) {
            if (i + x >= field.getWidth() || j + y >= field.getHeight()
                || !tiles[i + x][j + y].getType().equals(TileType.EMPTY)) {
              possiblePlace = false;
              break;
            }
          }
          if (!possiblePlace)
            break;
        }

        if (possiblePlace) {
          int finalJ = j;
          int finalI = i;
          products.forEach(product -> result.add(Factory.createFactoryWithProduct(finalI + 2, finalJ + 2, product)));
        }
      }

    return result;
  }

  protected Collection<Factory> getPossibleFactories() {
    return possibleFactories;
  }
}
