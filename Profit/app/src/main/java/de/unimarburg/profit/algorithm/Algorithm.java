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
    } catch (InterruptedException | ExecutionException | TimeoutException ignored) {
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
        Collection<TypeAndMinesCombination> combinations = combinationFinder.findCombinations(
            reachableMines, minesWithResources, products, factory);

        boolean connectedAll = false;
        for (TypeAndMinesCombination combination : combinations) {
          connectedAll = connector.connectMines(factory, combination.getMines());
          if (connectedAll) {
            break;
          }
        }

        if (!connectedAll) {
          factoryPlacer.removeFactory(field, factory);
        }

      }

      optionalFactory = factoryChooser.chooseFactory(field, factories);
    }
    return field;
  }


}
