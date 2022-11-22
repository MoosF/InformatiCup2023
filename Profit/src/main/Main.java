package main;

import model.CouldNotPlaceObjectException;
import model.Field;
import model.FixedObject;
import service.InputFile;
import service.JsonInput;
import simulation.SimulateException;


public class Main {

  public static void main(String[] args) throws CouldNotPlaceObjectException {
    String[] inputFiles = {"001.task.json", "002.task.json", "003.task.json", "004.task.json"};

    for (String file : inputFiles) {
      InputFile input = JsonInput.createInputFromFile(file);

      Field field = new Field(input.getWidth(), input.getHeight());

      for (FixedObject obj : input.getInputObjects()) {
        field.addBaseObject(obj);
      }

      field.show();
    }

  }

}
