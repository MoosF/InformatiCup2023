package de.unimarburg.profit.algorithm;

import de.unimarburg.profit.algorithm.connector.Connector;
import de.unimarburg.profit.algorithm.factoryplacer.FactoryChooser;
import de.unimarburg.profit.algorithm.factoryplacer.FactoryPlaceFinder;
import de.unimarburg.profit.algorithm.factoryplacer.FactoryPlacer;
import de.unimarburg.profit.algorithm.mineplacer.MinePlaceFinder;
import de.unimarburg.profit.algorithm.mineplacer.MinePlacer;
import de.unimarburg.profit.algorithm.mineplacer.MineWithResources;
import de.unimarburg.profit.algorithm.productchooser.CombinationFinder;
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
 * Implementation of the Algorithm.
 *
 * @author Yannick Kraml
 */
public class Algorithm {

  private final MinePlaceFinder minePlaceFinder;
  private final MinePlacer minePlacer;
  private final FactoryPlaceFinder factoryPlaceFinder;
  private final FactoryChooser factoryChooser;
  private final FactoryPlacer factoryPlacer;
  private final CombinationFinder combinationFinder;
  private final Connector connector;

  public Algorithm(MinePlaceFinder minePlaceFinder, MinePlacer minePlacer,
      FactoryPlaceFinder factoryPlaceFinder, FactoryChooser factoryChooser,
      FactoryPlacer factoryPlacer, CombinationFinder combinationFinder, Connector connector) {
    this.minePlaceFinder = minePlaceFinder;
    this.minePlacer = minePlacer;
    this.factoryPlaceFinder = factoryPlaceFinder;
    this.factoryChooser = factoryChooser;
    this.factoryPlacer = factoryPlacer;
    this.combinationFinder = combinationFinder;
    this.connector = connector;
  }


  public Collection<MovableObject> runAlgorithm(Field field, int time, int turns,
      Collection<Product> products) {

    Map<Mine, Deposit> possibleMines = minePlaceFinder.calculatePossibleMines(field);
    Map<Mine, Deposit> placedMines = minePlacer.placeMines(field, possibleMines);
    Collection<MineWithResources> minesWithResources = minePlaceFinder.calcResourcesPerMine(
        placedMines);

    Collection<Factory> factories = factoryPlaceFinder.calculatePossibleFactories(field, products);

    Optional<Factory> optionalFactory = factoryChooser.chooseFactory(field, factories);
    while (optionalFactory.isPresent()) {

      Factory factory = optionalFactory.get();
      factories.remove(factory);

      boolean placed = factoryPlacer.placeFactory(field, factory);
      if (placed) {

        Collection<Mine> reachableMines = connector.getReachableMines(factory);
        Collection<TypeAndMinesCombination> combination = combinationFinder.findCombinations(
            reachableMines, minesWithResources, products, factory);

        boolean connectedAll = false;
        for (TypeAndMinesCombination typeAndMinesCombination : combination) {
          connectedAll = connector.connectMines(factory, typeAndMinesCombination.getMines());
          if (connectedAll) {
            break;
          }
        }

        if(!connectedAll){
          factoryPlacer.removeFactory(field, factory);
        }

      }

      optionalFactory = factoryChooser.chooseFactory(field, factories);
    }

    return field.getMovableObjects();

  }


}
