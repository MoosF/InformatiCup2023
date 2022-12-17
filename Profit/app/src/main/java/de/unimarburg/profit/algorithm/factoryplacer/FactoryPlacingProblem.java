package de.unimarburg.profit.algorithm.factoryplacer;

import de.unimarburg.profit.model.Combiner;
import de.unimarburg.profit.model.Conveyer;
import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Tile;
import de.unimarburg.profit.model.enums.CombinerSubType;
import de.unimarburg.profit.model.enums.ConveyerSubType;
import de.unimarburg.profit.model.enums.TileType;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import de.unimarburg.profit.simulation.SimulateException;
import de.unimarburg.profit.simulation.Simulator;
import java.util.List;
import java.util.Random;
import org.moeaframework.core.Solution;
import org.moeaframework.problem.AbstractProblem;

public class FactoryPlacingProblem extends AbstractProblem {

  private List<Factory> factories;
  private List<Conveyer> conveyers;
  private List<Combiner> combiners;
  private final Field field;
  private final int turns;

  public FactoryPlacingProblem(Field field, List<Factory> factories, List<Conveyer> conveyers,
      List<Combiner> combiners, int turns) {
    super(0, 2, 0);
    this.factories = factories;
    this.conveyers = conveyers;
    this.combiners = combiners;
    this.field = field;
    this.turns = turns;
  }

  @Override
  public void evaluate(Solution solution) {
    FactoryPlacingSolution solution1 = (FactoryPlacingSolution) solution;
    try {
      int points = Simulator.getInstance().simulate(solution1.getField(), turns);
      solution.setObjective(0, -points);
    } catch (SimulateException e) {
      solution.setObjective(0, 0);
    }

    // TODO als zweites Ziel Anzahl der Runden minimieren
  }

  @Override
  public Solution newSolution() {
    FactoryPlacingSolution solution = new FactoryPlacingSolution(numberOfVariables,
        numberOfObjectives, field.copy());

    Random random = new Random();

    // zufällig Fabrik, Combiner oder Conveyer an allen Ausgängen hinzufügen
    // in einer Schleife, weil durch hinzufügen neue Elemente mit Ausgängen entstehen, wo man etwas hinzufügen kann
    for (int a = 0; a < 10; a++) {
      Tile[][] tiles = solution.getField().getTiles();
      for (int i = 0; i < tiles.length; i++) {
        for (int j = 0; j < tiles[0].length; j++) {
          if (tiles[i][j].getType().equals(TileType.OUTPUT)) {
            int object = random.nextInt(
                3); // wenn 0: versuchen Fabrik zu platzieren, wenn 1: conveyer, wenn 2: combiner
            switch (object) {
              case 0 -> {
                int subtype = random.nextInt(8);
                tryToPlaceFactory(subtype, solution.getField(), i, j);
              }
              case 1 -> {
                int subtype = random.nextInt(8);
                tryToPlaceConveyor(subtype, solution.getField(), i, j);
              }
              default -> {
                int subtype = random.nextInt(4);
                tryToPlaceCombiner(subtype, solution.getField(), i, j);
              }
            }
          }
        }
      }
    }

    return solution;
  }

  private boolean tryToPlaceFactory(int subtype, Field field, int x, int y) {
    Factory factory;
    // versuchen rechts vom Ausgang
    for (int i = 0; i < 5; i++) {
      factory = Factory.createFactoryWithSubtype(x + 1, y + i, subtype);
      if (field.baseObjectCanBePlaced(factory)) {
        try {
          field.addBaseObject(factory);
          return true;
        } catch (CouldNotPlaceObjectException e) {
          e.printStackTrace();
        }
      }
    }

    // versuchen links vom Ausgang
    for (int i = 0; i < 5; i++) {
      factory = Factory.createFactoryWithSubtype(x - 5, y + i, subtype);
      if (field.baseObjectCanBePlaced(factory)) {
        try {
          field.addBaseObject(factory);
          return true;
        } catch (CouldNotPlaceObjectException e) {
          e.printStackTrace();
        }
      }
    }

    // versuchen über dem Ausgang
    for (int i = 0; i < 5; i++) {
      factory = Factory.createFactoryWithSubtype(x - i, y - 5, subtype);
      if (field.baseObjectCanBePlaced(factory)) {
        try {
          field.addBaseObject(factory);
          return true;
        } catch (CouldNotPlaceObjectException e) {
          e.printStackTrace();
        }
      }
    }

    // versuchen unter dem Ausgang
    for (int i = 0; i < 5; i++) {
      factory = Factory.createFactoryWithSubtype(x - i, y + 1, subtype);
      if (field.baseObjectCanBePlaced(factory)) {
        try {
          field.addBaseObject(factory);
          return true;
        } catch (CouldNotPlaceObjectException e) {
          e.printStackTrace();
        }
      }
    }

    return false;
  }

  private boolean tryToPlaceConveyor(int subtype, Field field, int x, int y) {
    Conveyer conveyor;
    ConveyerSubType conveyorSubType;
    switch (subtype) {
      case 0 -> conveyorSubType = ConveyerSubType.SHORT_OUTPUT_EAST;
      case 1 -> conveyorSubType = ConveyerSubType.SHORT_OUTPUT_SOUTH;
      case 2 -> conveyorSubType = ConveyerSubType.SHORT_OUTPUT_WEST;
      case 3 -> conveyorSubType = ConveyerSubType.SHORT_OUTPUT_NORTH;
      case 4 -> conveyorSubType = ConveyerSubType.LONG_OUTPUT_EAST;
      case 5 -> conveyorSubType = ConveyerSubType.LONG_OUTPUT_SOUTH;
      case 6 -> conveyorSubType = ConveyerSubType.LONG_OUTPUT_WEST;
      default -> conveyorSubType = ConveyerSubType.LONG_OUTPUT_NORTH;
    }
    conveyor = Conveyer.createConveyor(x, y + 1, conveyorSubType);
    if (field.baseObjectCanBePlaced(conveyor)) {
      try {
        field.addBaseObject(conveyor);
        return true;
      } catch (CouldNotPlaceObjectException e) {
        e.printStackTrace();
      }
    }

    conveyor = Conveyer.createConveyor(x, y - 1, conveyorSubType);
    if (field.baseObjectCanBePlaced(conveyor)) {
      try {
        field.addBaseObject(conveyor);
        return true;
      } catch (CouldNotPlaceObjectException e) {
        e.printStackTrace();
      }
    }

    conveyor = Conveyer.createConveyor(x + 1, y, conveyorSubType);
    if (field.baseObjectCanBePlaced(conveyor)) {
      try {
        field.addBaseObject(conveyor);
        return true;
      } catch (CouldNotPlaceObjectException e) {
        e.printStackTrace();
      }
    }

    conveyor = Conveyer.createConveyor(x - 1, y, conveyorSubType);
    if (field.baseObjectCanBePlaced(conveyor)) {
      try {
        field.addBaseObject(conveyor);
        return true;
      } catch (CouldNotPlaceObjectException e) {
        e.printStackTrace();
      }
    }

    return false;
  }

  private boolean tryToPlaceCombiner(int subtype, Field field, int x, int y) {
    Combiner combiner;
    CombinerSubType combinerSubType;
    switch (subtype) {
      case 0 -> combinerSubType = CombinerSubType.OUTPUT_EAST;
      case 1 -> combinerSubType = CombinerSubType.OUTPUT_SOUTH;
      case 2 -> combinerSubType = CombinerSubType.OUTPUT_WEST;
      default -> combinerSubType = CombinerSubType.OUTPUT_NORTH;
    }

    switch (subtype) {
      case 0 -> {
        if (placeCombiner(combinerSubType, field, x + 1, y - 2)) {
          return true;
        }
        if (placeCombiner(combinerSubType, field, x + 2, y - 1)) {
          return true;
        }
        if (placeCombiner(combinerSubType, field, x + 2, y)) {
          return true;
        }
        if (placeCombiner(combinerSubType, field, x + 2, y + 1)) {
          return true;
        }
        if (placeCombiner(combinerSubType, field, x + 1, y + 2)) {
          return true;
        }
      }
      case 1 -> {
        if (placeCombiner(combinerSubType, field, x - 2, y + 1)) {
          return true;
        }
        if (placeCombiner(combinerSubType, field, x - 1, y + 2)) {
          return true;
        }
        if (placeCombiner(combinerSubType, field, x, y + 2)) {
          return true;
        }
        if (placeCombiner(combinerSubType, field, x + 1, y + 2)) {
          return true;
        }
        if (placeCombiner(combinerSubType, field, x + 2, y + 1)) {
          return true;
        }
      }
      case 2 -> {
        if (placeCombiner(combinerSubType, field, x - 1, y - 2)) {
          return true;
        }
        if (placeCombiner(combinerSubType, field, x - 2, y - 1)) {
          return true;
        }
        if (placeCombiner(combinerSubType, field, x - 2, y)) {
          return true;
        }
        if (placeCombiner(combinerSubType, field, x - 2, y + 1)) {
          return true;
        }
        if (placeCombiner(combinerSubType, field, x - 1, y + 2)) {
          return true;
        }
      }
      default -> {
        if (placeCombiner(combinerSubType, field, x - 2, y - 1)) {
          return true;
        }
        if (placeCombiner(combinerSubType, field, x - 1, y - 2)) {
          return true;
        }
        if (placeCombiner(combinerSubType, field, x, y - 2)) {
          return true;
        }
        if (placeCombiner(combinerSubType, field, x + 1, y - 2)) {
          return true;
        }
        if (placeCombiner(combinerSubType, field, x + 2, y - 1)) {
          return true;
        }
      }
    }

    return false;
  }

  private boolean placeCombiner(CombinerSubType combinerSubType, Field field, int x, int y) {
    Combiner combiner = Combiner.createCombiner(x, y, combinerSubType);
    if (field.baseObjectCanBePlaced(combiner)) {
      try {
        field.addBaseObject(combiner);
        return true;
      } catch (CouldNotPlaceObjectException e) {
        e.printStackTrace();
      }
    }
    return false;
  }
}
