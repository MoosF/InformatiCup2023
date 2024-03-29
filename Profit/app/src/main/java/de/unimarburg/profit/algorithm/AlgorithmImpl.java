package de.unimarburg.profit.algorithm;

import de.unimarburg.profit.algorithm.factoryplacing.combination.CombinationFinder;
import de.unimarburg.profit.algorithm.factoryplacing.combination.TypeAndMinesCombination;
import de.unimarburg.profit.algorithm.factoryplacing.connector.Connector;
import de.unimarburg.profit.algorithm.factoryplacing.connector.ConnectorImpl;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryChooser;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryPlaceFinder;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryPlaceFinderImpl;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryPlacerImpl;
import de.unimarburg.profit.algorithm.mineplacing.MinePlaceChooser;
import de.unimarburg.profit.algorithm.mineplacing.MinePlaceFinder;
import de.unimarburg.profit.algorithm.mineplacing.MinePlacer;
import de.unimarburg.profit.algorithm.mineplacing.MineWithResources;
import de.unimarburg.profit.model.Conveyor;
import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.MovableObject;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import de.unimarburg.profit.model.exceptions.CouldNotRemoveObjectException;
import de.unimarburg.profit.simulation.SimulateException;
import de.unimarburg.profit.simulation.Simulator;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Implementation of the interface {@link Algorithm}.
 *
 * @author Yannick Kraml
 */
public class AlgorithmImpl implements Algorithm {

  private static final int NUMBER_OF_FACTORY_PLACEMENT_TRIES = 10;
  private static final int MAX_NUMBER_OF_WAITING_FUTURES = 20;
  private static final int MAX_PLACED_FACTORIES_TRIES = 100;
  private final MinePlaceFinder minePlaceFinder;
  private final MinePlaceChooser minePlaceChooser;
  private final MinePlacer minePlacer;
  private final FactoryPlaceFinder factoryPlaceFinder;
  private final FactoryChooser factoryChooser;
  private final FactoryPlacerImpl factoryPlacer;
  private final CombinationFinder combinationFinder;

  private final Map<String, Boolean> uuids;


  private final Collection<CompletableFuture<Void>> futures;
  private final ExecutorService executorService;

  /**
   * Constructor of {@link AlgorithmImpl}.
   *
   * @param minePlaceFinder    {@link MinePlaceFinder}
   * @param minePlaceChooser   {@link MinePlaceChooser}
   * @param minePlacer         {@link MinePlacer}
   * @param factoryPlaceFinder {@link FactoryPlaceFinderImpl}
   * @param factoryChooser     {@link FactoryChooser}
   * @param factoryPlacer      {@link FactoryPlacerImpl}
   * @param combinationFinder  {@link CombinationFinder}
   */
  public AlgorithmImpl(MinePlaceFinder minePlaceFinder, MinePlaceChooser minePlaceChooser,
      MinePlacer minePlacer, FactoryPlaceFinder factoryPlaceFinder, FactoryChooser factoryChooser,
      FactoryPlacerImpl factoryPlacer, CombinationFinder combinationFinder) {
    this.minePlaceFinder = minePlaceFinder;
    this.minePlaceChooser = minePlaceChooser;
    this.minePlacer = minePlacer;
    this.factoryPlaceFinder = factoryPlaceFinder;
    this.factoryChooser = factoryChooser;
    this.factoryPlacer = factoryPlacer;
    this.combinationFinder = combinationFinder;

    uuids = new HashMap<>();
    futures = new HashSet<>();
    executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
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
  @Override
  public Collection<MovableObject> runAlgorithm(Field field, int time, int turns,
      Collection<Product> products) {

    Map<Integer, Field> solutions = new HashMap<>();

    String uuid = UUID.randomUUID().toString();
    uuids.put(uuid, true);
    CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
      while (true) {
        createAndAddNewSolutions(solutions, turns, field, products, uuid);
        if (!uuids.get(uuid)) {
          return null;
        }
      }
    });

    try {
      //The future will be canceled five seconds before the time limit.
      future.get(time - 10, TimeUnit.SECONDS);
    } catch (InterruptedException | TimeoutException | ExecutionException ignored) {
      //Just ignore. These exceptions will be thrown, if the time ends.
    }

    uuids.put(uuid, false);

    try {
      // Try to combine solutions.
      return buildSolution(solutions);
    } catch (CouldNotPlaceObjectException | CouldNotRemoveObjectException e) {
      // If an error occurred, just return best solution.
      Optional<Integer> maxPoints = solutions.keySet().stream().max(Comparator.naturalOrder());
      if (maxPoints.isPresent()) {
        Integer bestPoints = maxPoints.get();
        Field bestField = solutions.get(bestPoints);
        return bestField.getMovableObjects();
      } else {
        return new LinkedList<>();
      }
    }
  }

  /**
   * Tries to combine all solutions into one. Only none-overlapping solutions can be combined.
   *
   * @param solutions All solutions that have been found by the {@link Algorithm}.
   * @return All {@link MovableObject}s that are part of the best solution found.
   * @throws CouldNotPlaceObjectException If an object could not be placed on a {@link Field}.
   * @throws CouldNotRemoveObjectException If an object could not be removed from a {@link Field}.
   */
  private Collection<MovableObject> buildSolution(Map<Integer, Field> solutions)
      throws CouldNotPlaceObjectException, CouldNotRemoveObjectException {
    // No solution found.
    if (solutions.isEmpty()) {
      return new LinkedList<>();
    }

    // Build the best possible solution.
    Queue<Entry<Integer, Field>> solQueue = new PriorityQueue<>(
        (e1, e2) -> e2.getKey() - e1.getKey());
    solQueue.addAll(solutions.entrySet());
    if (solQueue.isEmpty()) {
      return new LinkedList<>();
    }
    var bestSingleSolution = solQueue.poll().getValue();
    LinkedList<MovableObject> perfectSolution = new LinkedList<>(
        bestSingleSolution.getMovableObjects());

    while (!solQueue.isEmpty()) {
      var nextBestSolution = solQueue.poll();
      if (nextBestSolution.getKey() == 0) {
        break;
      }
      var nextBestField = nextBestSolution.getValue();
      var movableObjs = nextBestField.getMovableObjects();
      var addedMovableObjects = new LinkedList<MovableObject>();

      for (MovableObject obj : movableObjs) {
        if (bestSingleSolution.baseObjectCanBePlaced(obj)) {
          bestSingleSolution.addBaseObject(obj);
        } else {
          for (var placedObjects : addedMovableObjects) {
            bestSingleSolution.removeBaseObject(placedObjects);
          }
          addedMovableObjects.clear();
          break;
        }

        addedMovableObjects.add(obj);
      }
      perfectSolution.addAll(addedMovableObjects);
    }

    return perfectSolution;
  }

  private void createAndAddNewSolutions(Map<Integer, Field> solutions, int turns, Field field,
      Collection<Product> products, String uuid) {

    List<Map<Mine, Deposit>> placements = findMinePlacements(field);
    for (Map<Mine, Deposit> placement : placements) {
      Field copy1 = field.copy();

      Collection<MineWithResources> minesWithResources = placeMines(copy1, placement);

      for (int i = 0; i < NUMBER_OF_FACTORY_PLACEMENT_TRIES; i++) {

        waitHere();

        //Checks if time is up
        if (!uuids.get(uuid)) {
          return;
        }

        Runnable runnable = () -> {
          Field copy2 = copy1.copy();
          AlgorithmImpl.this.placeFactories(copy2, products, minesWithResources);
          minePlacer.removeUselessMines(copy2);
          evaluateAndAddSolution(solutions, turns, copy2);
        };

        CompletableFuture<Void> future = CompletableFuture.runAsync(runnable, executorService);
        futures.add(future);
        future.whenComplete((unused, throwable) -> futures.remove(future));
      }

    }

  }

  private void waitHere() {
    while (futures.size() > MAX_NUMBER_OF_WAITING_FUTURES) {
      try {
        CompletableFuture.anyOf(futures.toArray(new CompletableFuture[0])).get();
      } catch (InterruptedException | ExecutionException | NullPointerException e) {
        //Ignore
      }
    }
  }

  private static void evaluateAndAddSolution(Map<Integer, Field> solutions, int turns,
      Field field) {
    try {
      int points = Simulator.getInstance().simulate(field, turns);
      solutions.put(points, field);
    } catch (SimulateException ignored) {
      //Ignore. The Solution just won't be put in the map.
    }
  }

  private Collection<MineWithResources> placeMines(Field field, Map<Mine, Deposit> placement) {
    Map<Mine, Deposit> placedMines = minePlacer.placeMines(field, placement);
    return minePlaceFinder.calcResourcesFromMines(placedMines);
  }

  private List<Map<Mine, Deposit>> findMinePlacements(Field field) {
    Map<Mine, Deposit> possibleMines = minePlaceFinder.calculatePossibleMines(field);
    Comparator<Map<Mine, Deposit>> comp = Comparator.comparingInt(mines -> -mines.size());
    return minePlaceChooser.choosePlacements(field, possibleMines).stream().sorted(comp).toList();
  }

  private void placeFactories(Field field, Collection<Product> products,
      Collection<MineWithResources> minesWithResources) {
    Collection<Factory> factories = factoryPlaceFinder.calculatePossibleFactories(field);

    Optional<Factory> optionalFactory = factoryChooser.chooseFactory(field, factories);
    int i = 0;
    while (optionalFactory.isPresent()) {
      i++;
      if (i > MAX_PLACED_FACTORIES_TRIES) {
        return;
      }

      Factory factory = optionalFactory.get();
      factories.remove(factory);

      boolean placed = factoryPlacer.placeFactory(field, factory);
      if (placed) {

        Connector connector = new ConnectorImpl(field);
        Collection<Mine> reachableMines = connector.getReachableMines(factory);

        Collection<TypeAndMinesCombination> combinations = combinationFinder.findCombinations(
                reachableMines, minesWithResources, products, factory).stream()
            .sorted((o1, o2) -> -(int) (o1.getValue() - o2.getValue())).toList();

        boolean connectedAll = false;
        Collection<Conveyor> beforeConveyors = field.getObjectsOfClass(Conveyor.class);
        for (TypeAndMinesCombination combination : combinations) {

          connectedAll = connector.connectMines(combination.getMines());

          if (connectedAll) {
            factory.setProduct(combination.getProduct());
            i = 0;
            break;
          }

          connector.removePlacedConveyers(field, beforeConveyors);
        }

        if (!connectedAll) {
          factoryPlacer.removeFactory(field, factory);
        }

      }

      optionalFactory = factoryChooser.chooseFactory(field, factories);
    }
  }


}
