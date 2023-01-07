package de.unimarburg.profit;

import de.unimarburg.profit.algorithm.Algorithm;
import de.unimarburg.profit.algorithm.factoryplacing.combination.CombinationFinderImpl;
import de.unimarburg.profit.algorithm.factoryplacing.connector.ConnectorImpl;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryChooserRandom;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryPlaceFinderImpl;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryPlacerImpl;
import de.unimarburg.profit.algorithm.mineplacing.MinePlaceChooserImpl;
import de.unimarburg.profit.algorithm.mineplacing.MinePlaceFinderImpl;
import de.unimarburg.profit.algorithm.mineplacing.MinePlacerImpl;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.FixedObject;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import de.unimarburg.profit.service.Input;
import de.unimarburg.profit.service.InputOutputException;
import de.unimarburg.profit.service.InputOutputHandle;
import de.unimarburg.profit.service.InputOutputHandle.FileType;
import de.unimarburg.profit.service.IoSystem;
import de.unimarburg.profit.service.Settings;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

/**
 * Second temporary main method to test the {@link IoSystem}.
 *
 * @author Yannick Kraml.
 */
public class Main {


  public static void main(String[] args) throws InputOutputException {

    //testMain(args);

    startIo();

  }

  private static void startIo() {
    InputStream inputStream = System.in;
    OutputStream outputStream = System.out;

    IoSystem ioSystem = new IoSystem(inputStream, outputStream);

    ioSystem.start();
  }

  private static void testMain(String[] args) throws InputOutputException {
    var settings = Settings.getInstance();
    settings.updateImportTarget(false);
    settings.updateImportFileType(FileType.JSON);

    for (int i = 3; i < args.length; i++) {
      String file = args[i];
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
