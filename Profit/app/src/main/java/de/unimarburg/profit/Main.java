package de.unimarburg.profit;

import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import de.unimarburg.profit.mineplacer.MinePlaceFinder;
import de.unimarburg.profit.mineplacer.MinePlacingProblemReaching;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.FixedObject;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.service.Input;
import de.unimarburg.profit.service.InputOutputHandle;
import de.unimarburg.profit.service.InputOutputHandle.FileType;
import de.unimarburg.profit.service.Settings;
import de.unimarburg.profit.simulation.SimulateException;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;


/**
 * This class only contains the main method.
 *
 * @author Yannick Kraml
 */
public class Main {

  /**
   * Main method.
   *
   * @param args Arguments. Should be an empty array.
   */
  public static void main(String[] args) throws SimulateException {
    var settings = Settings.getInstance();
    settings.updateImportTarget(false);
    settings.updateImportFileType(FileType.JSON);

    for (String file : args) {
      Input input = InputOutputHandle.readInputFrom(file);

      Field field = new Field(input.getWidth(), input.getHeight());

      for (FixedObject obj : input.getInputObjects()) {
        try {
          field.addBaseObject(obj);
        } catch (CouldNotPlaceObjectException e) {
          throw new RuntimeException(e);
        }
      }

      MinePlaceFinder minePlaceFinder = new MinePlaceFinder(field);
      Mine[] possibleMines = minePlaceFinder.getAllPossibleMines();

      NondominatedPopulation population = new Executor().withProblemClass(
              MinePlacingProblemReaching.class, field, possibleMines, input.getTurns())
          .withAlgorithm("NSGAII").withMaxTime(3 * 1000).distributeOnAllCores().run();

      Solution solution = population.iterator().next();
      placeMines(field, possibleMines, solution);

      System.out.println("Violates contraint: " + solution.violatesConstraints());
      System.out.println("Objective: " + solution.getObjective(1));
      System.out.println();

      field.show();

    }

  }

  private static void placeMines(Field field, Mine[] possibleMines, Solution solution) {

    boolean[] binary = EncodingUtils.getBinary(solution.getVariable(0));

    for (int i = 0; i < binary.length; i++) {
      boolean shouldBePlaced = binary[i];
      Mine mine = possibleMines[i];

      if (shouldBePlaced) {
        try {
          field.addBaseObject(mine);
        } catch (CouldNotPlaceObjectException ignore) {
        }
      }
    }
  }

}
