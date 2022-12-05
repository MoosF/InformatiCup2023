package de.uni_marburg.profit.mineplacer;

import java.util.Map;
import de.uni_marburg.profit.model.*;
import de.uni_marburg.profit.model.enums.*;
import de.uni_marburg.profit.model.exceptions.*;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import de.uni_marburg.profit.service.Input;
import de.uni_marburg.profit.service.InputOutputHandle;
import de.uni_marburg.profit.simulation.SimulateException;
import de.uni_marburg.profit.simulation.Simulator;

public class MinePlacerMain {


  public static void main(String[] args) throws CouldNotPlaceObjectException, SimulateException {

    Product product0 = new Product(10, ProductType.ZERO,
        Map.of(ResourceType.ZERO, 10, ResourceType.ONE, 10));
    Product product1 = new Product(10, ProductType.ONE,
        Map.of(ResourceType.TWO, 10, ResourceType.THREE, 10));

    Field field = new Field(40, 20);

    field.addBaseObject(Deposit.createDeposit(ResourceType.ZERO, 0, 0, 5, 5));
    field.addBaseObject(Combiner.createCombiner(10, 2, CombinerSubType.OUTPUT_EAST));
    field.addBaseObject(Conveyer.createConveyor(13, 2, ConveyerSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Conveyer.createConveyor(14, 4, ConveyerSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyer.createConveyor(16, 5, ConveyerSubType.SHORT_OUTPUT_EAST));

    field.addBaseObject(Deposit.createDeposit(ResourceType.ONE, 35, 0, 5, 5));
    field.addBaseObject(Combiner.createCombiner(29, 2, CombinerSubType.OUTPUT_WEST));
    field.addBaseObject(Conveyer.createConveyor(26, 2, ConveyerSubType.SHORT_OUTPUT_WEST));
    field.addBaseObject(Conveyer.createConveyor(25, 4, ConveyerSubType.SHORT_OUTPUT_SOUTH));
    field.addBaseObject(Conveyer.createConveyor(24, 6, ConveyerSubType.SHORT_OUTPUT_WEST));

    field.addBaseObject(Deposit.createDeposit(ResourceType.TWO, 0, 15, 5, 5));
    field.addBaseObject(Combiner.createCombiner(10, 17, CombinerSubType.OUTPUT_EAST));
    field.addBaseObject(Conveyer.createConveyor(13, 17, ConveyerSubType.SHORT_OUTPUT_EAST));
    field.addBaseObject(Conveyer.createConveyor(14, 15, ConveyerSubType.SHORT_OUTPUT_NORTH));
    field.addBaseObject(Conveyer.createConveyor(16, 14, ConveyerSubType.SHORT_OUTPUT_EAST));

    field.addBaseObject(Deposit.createDeposit(ResourceType.THREE, 35, 15, 5, 5));
    field.addBaseObject(Combiner.createCombiner(29, 17, CombinerSubType.OUTPUT_WEST));
    field.addBaseObject(Conveyer.createConveyor(26, 17, ConveyerSubType.SHORT_OUTPUT_WEST));
    field.addBaseObject(Conveyer.createConveyor(25, 15, ConveyerSubType.SHORT_OUTPUT_NORTH));
    field.addBaseObject(Conveyer.createConveyor(24, 13, ConveyerSubType.SHORT_OUTPUT_WEST));

    field.addBaseObject(Factory.createFactory(20, 7, product0));
    field.addBaseObject(Factory.createFactory(20, 12, product1));

    int turns = 16;

    field.show();

    MinePlaceFinder minePlaceFinder = new MinePlaceFinder(field);
    Mine[] possibleMines = minePlaceFinder.getAllPossibleMines();

    NondominatedPopulation population = new Executor()
        .withProblemClass(MinePlacingProblem.class, field, possibleMines, turns)
        .withAlgorithm("NSGAII")
        .withMaxTime(3 * 1000)
        .distributeOnAllCores()
        .run();



    Solution solution = population.iterator().next();
    placeMines(field, possibleMines, solution);


    int points = Simulator.getInstance().simulate(field, turns);
    System.out.println(points);

    System.out.println("Violates contraint: " + solution.violatesConstraints());

    System.out.println(solution.getObjective(1));

    field.show();
  }

  private static Field createFieldFromJson(String file) throws CouldNotPlaceObjectException {
    Input input = InputOutputHandle.getInputFrom(file);

    Field field = new Field(input.getWidth(), input.getHeight());

    for (FixedObject obj : input.getInputObjects()) {
      field.addBaseObject(obj);
    }
    return field;
  }

  private static void placeMines(Field field, Mine[] possibleMines, Solution solution) {

    boolean[] binary = EncodingUtils.getBinary(solution.getVariable(0));

    for (int i = 0; i < binary.length; i++) {
      boolean shouldBePlaced = binary[i];
      Mine mine = possibleMines[i];

      if (shouldBePlaced) {
        try {
          field.addBaseObject(mine);
        } catch (CouldNotPlaceObjectException ignored) {

        }
      }
    }


  }

}
