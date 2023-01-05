package de.unimarburg.profit;

import de.unimarburg.profit.service.IoSystem;
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


  public static void main(String[] args) {

    InputStream inputStream = System.in;
    OutputStream outputStream = System.out;

    IoSystem ioSystem = new IoSystem(inputStream, outputStream);

    ioSystem.start();

  }


}
