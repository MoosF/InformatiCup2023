package de.unimarburg.profit.algorithm.factoryplacing.factory;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Tile;
import de.unimarburg.profit.model.enums.TileType;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Implementation of {@link FactoryPlaceFinder}.
 *
 * @author Yevheniia Makara
 */
public class FactoryPlaceFinderImpl implements FactoryPlaceFinder {

  @Override
  public Collection<Factory> calculatePossibleFactories(Field field) {
    // benötigte Typen von Factories bestimmen

    // mögliche Plätze für Fabriken bestimmen
    Collection<Factory> factories = new LinkedList<>();
    Tile[][] tiles = field.getTiles();
    for (int i = 0; i < field.getWidth(); i++) {
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
          if (!possiblePlace) {
            break;
          }
        }

        if (possiblePlace) {
          factories.add(Factory.createFactoryWithoutProduct(i, j));
        }
      }
    }

    return factories;
  }

}
