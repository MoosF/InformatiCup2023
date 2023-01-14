package de.unimarburg.profit;

import de.unimarburg.profit.algorithm.Algorithm;
import de.unimarburg.profit.algorithm.AlgorithmImpl;
import de.unimarburg.profit.algorithm.factoryplacing.combination.CombinationFinderImpl;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryChooserRandom;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryPlaceFinderImpl;
import de.unimarburg.profit.algorithm.factoryplacing.factory.FactoryPlacerImpl;
import de.unimarburg.profit.algorithm.mineplacing.MinePlaceChooserImpl;
import de.unimarburg.profit.algorithm.mineplacing.MinePlaceFinderImpl;
import de.unimarburg.profit.algorithm.mineplacing.MinePlacerImpl;
import de.unimarburg.profit.controller.Controller;
import de.unimarburg.profit.service.IoSystem;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Class that contains main method.
 *
 * @author Yannick Kraml.
 */
public class Main {


  /**
   * Main method.
   *
   * @param args Arguments. Should be empty.
   */
  public static void main(String[] args) {

    Algorithm algorithm = new AlgorithmImpl(
        new MinePlaceFinderImpl(),
        new MinePlaceChooserImpl(),
        new MinePlacerImpl(),
        new FactoryPlaceFinderImpl(),
        new FactoryChooserRandom(),
        new FactoryPlacerImpl(),
        new CombinationFinderImpl()
    );

    Controller controller = new Controller(algorithm);

    InputStream inputStream = System.in;
    OutputStream outputStream = System.out;

    IoSystem ioSystem = new IoSystem(inputStream, outputStream, controller);
    ioSystem.start();

  }


}
