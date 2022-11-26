package main;

import model.CouldNotPlaceObjectException;
import model.Field;
import model.FixedObject;
import service.InputOutputHandle.FileType;
import service.Input;
import service.InputOutputHandle;
import service.Settings;


public class Main {

  public static void main(String[] args) throws CouldNotPlaceObjectException {
    var settings = Settings.getInstance();
    settings.updateImportTarget(false);
    settings.updateImportFileType(FileType.JSON);

    String[] inputFiles = {
        "001.task-with-time.json", "002.task-with-time.json",
        "003.task-with-time.json", "004.task-with-time.json"
    };

    for (String file : inputFiles) {
      Input input = InputOutputHandle.getInputFrom(file);

      Field field = new Field(input.getWidth(), input.getHeight());

      for (FixedObject obj : input.getInputObjects()) {
        field.addBaseObject(obj);
      }

      field.show();
    }

  }

}
