package de.unimarburg.profit;

import de.unimarburg.profit.algorithm.Algorithm;
import de.unimarburg.profit.algorithm.factoryplacing.connector.ConnectorImpl;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryChooserRandom;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryPlaceFinderImpl;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryPlacerImpl;
import de.unimarburg.profit.algorithm.mineplacing.MinePlaceChooserImpl;
import de.unimarburg.profit.algorithm.mineplacing.MinePlaceFinderImpl;
import de.unimarburg.profit.algorithm.mineplacing.MinePlacerImpl;
import de.unimarburg.profit.algorithm.factoryplacing.combination.CombinationFinderImpl;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.FixedObject;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import de.unimarburg.profit.service.Input;
import de.unimarburg.profit.service.InputOutputException;
import de.unimarburg.profit.service.InputOutputHandle;
import de.unimarburg.profit.service.InputOutputHandle.FileType;
import de.unimarburg.profit.service.Settings;

/**
 * This class only contains the main method.
 *
 * @author Yannick Kraml
 */
public class MainTest {

  /**
   * Main method.
   *
   * @param args Arguments. Should be an empty array.
   */
  public static void main(String[] args) throws InputOutputException {

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

      Algorithm algorithm = new Algorithm(
          new MinePlaceFinderImpl(),
          new MinePlaceChooserImpl(),
          new MinePlacerImpl(),
          new FactoryPlaceFinderImpl(),
          new FactoryChooserRandom(),
          new FactoryPlacerImpl(),
          new CombinationFinderImpl(),
          new ConnectorImpl(field)
      );

      algorithm.runAlgorithm(field, input.getTime(), input.getTurns(), input.getProducts());
    }

  }


}