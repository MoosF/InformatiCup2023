package de.uni_marburg.profit;

import de.uni_marburg.profit.model.exceptions.CouldNotPlaceObjectException;
import de.uni_marburg.profit.model.Field;
import de.uni_marburg.profit.model.FixedObject;
import de.uni_marburg.profit.service.Input;
import de.uni_marburg.profit.service.InputOutputHandle;
import de.uni_marburg.profit.service.InputOutputHandle.FileType;
import de.uni_marburg.profit.service.Settings;


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
  public static void main(String[] args) {
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

      field.show();
    }

  }

}
