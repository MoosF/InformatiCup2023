package de.unimarburg.profit;

import de.unimarburg.profit.model.Conveyer;
import de.unimarburg.profit.model.enums.ConveyerSubType;
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
  public static void main(String[] args) throws SimulateException, CouldNotPlaceObjectException {

    Field field = new Field(20, 20);

    field.addBaseObject(Conveyer.createConveyor(1,1, ConveyerSubType.SHORT_OUTPUT_NORTH));
    field.addBaseObject(Conveyer.createConveyor(1,1, ConveyerSubType.SHORT_OUTPUT_WEST));



    field.show();

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
