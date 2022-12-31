package de.unimarburg.profit.algorithm;

import de.unimarburg.profit.algorithm.connector.Connector;
import de.unimarburg.profit.algorithm.connector.ConnectorImpl;
import de.unimarburg.profit.algorithm.factoryplacer.FactoryChooser;
import de.unimarburg.profit.algorithm.factoryplacer.FactoryChooserRandom;
import de.unimarburg.profit.algorithm.factoryplacer.FactoryPlaceFinder;
import de.unimarburg.profit.algorithm.factoryplacer.FactoryPlacer;
import de.unimarburg.profit.algorithm.mineplacer.MinePlaceFinder;
import de.unimarburg.profit.algorithm.mineplacer.MinePlaceFinderImpl;
import de.unimarburg.profit.algorithm.mineplacer.MinePlacer;
import de.unimarburg.profit.algorithm.mineplacer.MinePlacerImpl;
import de.unimarburg.profit.algorithm.mineplacer.MineWithResource;
import de.unimarburg.profit.algorithm.productchooser.CombinationFinder;
import de.unimarburg.profit.algorithm.productchooser.CombinationFinderImpl;
import de.unimarburg.profit.algorithm.productchooser.TypeAndMinesCombination;
import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.MovableObject;
import de.unimarburg.profit.model.Product;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of {@link Algorithm}.
 *
 * @author Yannick Kraml
 */
public class AlgorithmImpl implements Algorithm {

  private FactoryChooser factoryChooser;
  private FactoryPlaceFinder factoryPlaceFinder;
  private MinePlacer minePlacer = new MinePlacerImpl();
  private MinePlaceFinder minePlaceFinder = new MinePlaceFinderImpl();
  private CombinationFinder combinationFinder = new CombinationFinderImpl();
  private Connector connector;

  @Override
  public Collection<MovableObject> runAlgorithm(Field field, int time, int turns,
      Collection<Product> products) {

    connector = new ConnectorImpl(field);

    Map<Mine, Deposit> possibleMines = minePlaceFinder.calculatePossibleMines(field);

    Map<Mine, Deposit> placedMines = minePlacer.placeMines(field, possibleMines);
    Collection<MineWithResource> minesWithResources = minePlaceFinder.calcResourcesPerMine(
        placedMines);

    factoryPlaceFinder = new FactoryPlaceFinder(field);
    Collection<Factory> factories = factoryPlaceFinder.calculatePossibleFactories(products);

    factoryChooser = new FactoryChooserRandom(field, factories);
    FactoryPlacer factoryPlacer = new FactoryPlacer();

    Optional<Factory> optionalFactory = factoryChooser.chooseFactory();
    while (optionalFactory.isPresent()) {

      Factory factory = optionalFactory.get();
      boolean placed = factoryPlacer.placeFactory(field, factory);

      if (placed) {

        Collection<Mine> reachableMines = connector.getReachableMines(factory);
        Collection<TypeAndMinesCombination> combination = combinationFinder.findCombinations(
            reachableMines, minesWithResources, products, factory);

        for (TypeAndMinesCombination typeAndMinesCombination : combination) {
          boolean connectedAll = connector.connectMines(factory,
              typeAndMinesCombination.getMines());
          if (connectedAll) {
            break;
          }
        }

      }

      optionalFactory = factoryChooser.chooseFactory();
    }

    return field.getMovableObjects();

  }


}
