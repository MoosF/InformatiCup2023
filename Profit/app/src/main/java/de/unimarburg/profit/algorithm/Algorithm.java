package de.unimarburg.profit.algorithm;

import de.unimarburg.profit.algorithm.connector.Connector;
import de.unimarburg.profit.algorithm.factoryplacer.FactoryChooser;
import de.unimarburg.profit.algorithm.factoryplacer.FactoryPlaceFinder;
import de.unimarburg.profit.algorithm.factoryplacer.FactoryPlacer;
import de.unimarburg.profit.algorithm.mineplacer.MinePlaceChooser;
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
import de.unimarburg.profit.simulation.SimulateException;
import de.unimarburg.profit.simulation.Simulator;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Implementation of the Algorithm.
 *
 * @author Yannick Kraml
 */
public class Algorithm {

  private final MinePlaceFinder minePlaceFinder;
  private final MinePlaceChooser minePlaceChooser;
  private final MinePlacer minePlacer;
  private final FactoryPlaceFinder factoryPlaceFinder;
  private final FactoryChooser factoryChooser;
  private final FactoryPlacer factoryPlacer;
  private final CombinationFinder combinationFinder;
  private final Connector connector;


  /**
   * Constructor of {@link Algorithm}.
   *
   * @param minePlaceFinder    {@link MinePlaceFinder}
   * @param minePlaceChooser   {@link MinePlaceChooser}
   * @param minePlacer         {@link MinePlacer}
   * @param factoryPlaceFinder {@link FactoryPlaceFinder}
   * @param factoryChooser     {@link FactoryChooser}
   * @param factoryPlacer      {@link FactoryPlacer}
   * @param combinationFinder  {@link CombinationFinder}
   * @param connector          {@link Connector}
   */
  public Algorithm(MinePlaceFinder minePlaceFinder, MinePlaceChooser minePlaceChooser,
      MinePlacer minePlacer,
      FactoryPlaceFinder factoryPlaceFinder, FactoryChooser factoryChooser,
      FactoryPlacer factoryPlacer, CombinationFinder combinationFinder, Connector connector) {
    this.minePlaceFinder = minePlaceFinder;
    this.minePlaceChooser = minePlaceChooser;
    this.minePlacer = minePlacer;
    this.factoryPlaceFinder = factoryPlaceFinder;
    this.factoryChooser = factoryChooser;
    this.factoryPlacer = factoryPlacer;
    this.combinationFinder = combinationFinder;
    this.connector = connector;
  }


  /**
   * Starts the algorithm.
   *
   * @param field    Field
   * @param time     The maximal time in seconds, that the algorithm can take.
   * @param turns    Turns, that the {@link Field} will be simulated.
   * @param products {@link Product}s, that exist in this instance of the problem.
   * @return Collection of {@link MovableObject}s, that should be placed for the best solution.
   */
  public Collection<MovableObject> runAlgorithm(Field field, int time, int turns,
      Collection<Product> products) {

    Map<Integer, Field> solutions = new HashMap<>();

    CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
      //At the moment the algorithm will start completely from scratch everytime it has finished.
      while (true) {
        Field solution = createNewSolution(field, products);
        try {
          solutions.put(Simulator.getInstance().simulate(solution, turns), solution);
        } catch (SimulateException ignored) {
          throw new RuntimeException("Exception while simulated");
        }
      }
    });

    try {
      //The future will be canceled five seconds before the time limit.
      future.get(time - 5, TimeUnit.SECONDS);
    } catch (InterruptedException | TimeoutException ignored) {
      //Just ignore. These exceptions will be thrown, if the time ends.
    } catch (ExecutionException e) {
      //If this is caught, an exception occurred in the method get from the supplier in the future.
      throw new RuntimeException(e);
    }

    Optional<Integer> maxPoints = solutions.keySet().stream().max(Comparator.naturalOrder());
    if (maxPoints.isPresent()) {
      return solutions.get(maxPoints.get()).getMovableObjects();
    } else {
      return new LinkedList<>();
    }

  }

  private Field createNewSolution(Field field, Collection<Product> products) {
    field = field.copy();
    field.show();

    Collection<MineWithResources> minesWithResources = placeMines(field);
    placeFactories(field, products, minesWithResources);

    return field;
  }

  private void placeFactories(Field field, Collection<Product> products,
      Collection<MineWithResources> minesWithResources) {
    Collection<Factory> factories = factoryPlaceFinder.calculatePossibleFactories(field, products);

    Optional<Factory> optionalFactory = factoryChooser.chooseFactory(field, factories);
    while (optionalFactory.isPresent()) {

      Factory factory = optionalFactory.get();
      factories.remove(factory);

      System.out.println(factory);

      boolean placed = factoryPlacer.placeFactory(field, factory);
      if (placed) {

        Collection<Mine> reachableMines = connector.getReachableMines(factory);
        Collection<TypeAndMinesCombination> combinations = combinationFinder.findCombinations(
            reachableMines, minesWithResources, products, factory);

        boolean connectedAll = false;
        for (TypeAndMinesCombination combination : combinations) {

          System.out.println(combination);

          connectedAll = connector.connectMines(combination.getMines());
          if (connectedAll) {
            break;
          }
        }

        if (!connectedAll) {
          factoryPlacer.removeFactory(field, factory);
          //connector.removeAllPlacedObjects();
        }

      }

      optionalFactory = factoryChooser.chooseFactory(field, factories);
    }
  }

  private Collection<MineWithResources> placeMines(Field field) {
    Map<Mine, Deposit> possibleMines = minePlaceFinder.calculatePossibleMines(field);
    Map<Mine, Deposit> minesToBePlaced = minePlaceChooser.choosePlaces(field, possibleMines);
    Map<Mine, Deposit> placedMines = minePlacer.placeMines(field, minesToBePlaced);
    Collection<MineWithResources> minesWithResources = minePlaceFinder.calcResourcesPerMine(
        placedMines);
    return minesWithResources;
  }


}
