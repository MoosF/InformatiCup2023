package de.unimarburg.profit;

import de.unimarburg.profit.algorithm.mineplacer.MinePlaceFinder;
import de.unimarburg.profit.algorithm.mineplacer.MinePlaceFinderImpl;
import de.unimarburg.profit.algorithm.mineplacer.MinePlacer;
import de.unimarburg.profit.algorithm.mineplacer.MinePlacerImpl;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.FixedObject;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import de.unimarburg.profit.service.Input;
import de.unimarburg.profit.service.InputOutputHandle;
import de.unimarburg.profit.service.InputOutputHandle.FileType;
import de.unimarburg.profit.service.Settings;
import de.unimarburg.profit.simulation.SimulateException;
import java.util.Collection;

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

      MinePlaceFinder minePlaceFinder = new MinePlaceFinderImpl();
      Collection<Mine> possibleMines = minePlaceFinder.calculatePossibleMines(field);

      MinePlacer minePlacer = new MinePlacerImpl();
      minePlacer.placeMines(field, possibleMines);


      field.show();

    }

  }


}