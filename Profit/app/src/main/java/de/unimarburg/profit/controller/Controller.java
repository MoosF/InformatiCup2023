package de.unimarburg.profit.controller;

import de.unimarburg.profit.model.MovableObject;
import de.unimarburg.profit.service.Input;
import java.io.IOException;
import java.util.Collection;

public interface Controller {

  Collection<MovableObject> startAlgorithm(Input input) throws IOException;

  default void stopEverything(){
    //doNothing
  }
}
