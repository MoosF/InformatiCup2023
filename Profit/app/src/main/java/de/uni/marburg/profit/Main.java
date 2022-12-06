package de.uni.marburg.profit;


import de.uni.marburg.profit.mineplacer.MinePlaceFinder;
import de.uni.marburg.profit.mineplacer.MinePlacingProblemReaching;
import de.uni.marburg.profit.model.Field;
import de.uni.marburg.profit.model.FixedObject;
import de.uni.marburg.profit.model.Mine;
import de.uni.marburg.profit.model.exceptions.CouldNotPlaceObjectException;
import de.uni.marburg.profit.service.Input;
import de.uni.marburg.profit.service.InputOutputHandle;
import de.uni.marburg.profit.service.InputOutputHandle.FileType;
import de.uni.marburg.profit.service.Settings;
import de.uni.marburg.profit.simulation.SimulateException;
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
      Input input = InputOutputHandle.getInputFrom(file);

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

      Field copy = field.copy();
      placeMines(copy, possibleMines, solution);
      System.out.println("Violates contraint: " + solution.violatesConstraints());
      System.out.println("Objective: " + solution.getObjective(1));
      System.out.println();
      copy.show();

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
          //Ignore
        }
      }
    }
  }

}
