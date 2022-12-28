package de.unimarburg.profit.algorithm.factoryplacer;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.Tile;
import de.unimarburg.profit.model.enums.TileType;
import de.unimarburg.profit.service.Input;
import java.util.Collection;
import java.util.LinkedList;

/**
 * This clas is responsible to find all possible {@link Factory} for a given {@link Field}.
 *
 * @author Yevheniia Makara
 */
public class FactoryPlaceFinder {

  private final Field field;

  protected FactoryPlaceFinder(Field field) {
    this.field = field;
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

}