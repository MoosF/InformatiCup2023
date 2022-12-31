package de.unimarburg.profit.algorithm;

import de.unimarburg.profit.algorithm.mineplacer.MinePlaceFinder;
import de.unimarburg.profit.algorithm.mineplacer.MinePlaceFinderImpl;
import de.unimarburg.profit.algorithm.mineplacer.MinePlacer;
import de.unimarburg.profit.algorithm.mineplacer.MinePlacerImpl;
import de.unimarburg.profit.algorithm.mineplacer.MineWithResource;
import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.MovableObject;
import java.util.Collection;
import java.util.Map;

public class AlgorithmImpl implements Algorithm {

  @Override
  public Collection<MovableObject> runAlgorithm(Field field, int time, int turns){

    MinePlaceFinder minePlaceFinder = new MinePlaceFinderImpl();
    Map<Mine, Deposit> possibleMines = minePlaceFinder.calculatePossibleMines(field);

    MinePlacer minePlacer = new MinePlacerImpl();
    Map<Mine, Deposit> placedMines = minePlacer.placeMines(field, possibleMines);


    Collection<MineWithResource> mineWithResources = minePlaceFinder.calcResourcesPerMine(placedMines);



    return field.getMovableObjects();

  }


}
