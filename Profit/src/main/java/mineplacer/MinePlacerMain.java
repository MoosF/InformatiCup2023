package mineplacer;

import model.Field;
import model.FixedObject;
import model.Mine;
import model.enums.MineSubType;
import model.exceptions.CouldNotPlaceObjectException;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import service.Input;
import service.InputOutputHandle;
import service.InputOutputHandle.FileType;
import service.Settings;

public class MinePlacerMain {


  public static void main(String[] args) throws CouldNotPlaceObjectException {

    var settings = Settings.getInstance();
    settings.updateImportTarget(false);
    settings.updateImportFileType(FileType.JSON);

    String[] inputFiles = {
        "001.task-with-time.json",
        "002.task-with-time.json",
        "003.task-with-time.json",
        "004.task-with-time.json"
    };

    for (String file : inputFiles) {

      Field field = createFieldFromJson(file);

      MinePlaceFinder minePlaceFinder = new MinePlaceFinder(field);
      Placement[] possiblePlacements = minePlaceFinder.getAllPlacements();

      NondominatedPopulation population = new Executor()
          .withProblemClass(MinePlacingProblem.class, field, possiblePlacements)
          .withAlgorithm("NSGAII")
          .withMaxEvaluations(1000)
          .run();

      placeMines(field, possiblePlacements, population.iterator().next());

      field.show();
    }

  }

  private static Field createFieldFromJson(String file) throws CouldNotPlaceObjectException {
    Input input = InputOutputHandle.getInputFrom(file);

    Field field = new Field(input.getWidth(), input.getHeight());

    for (FixedObject obj : input.getInputObjects()) {
      field.addBaseObject(obj);
    }
    return field;
  }

  private static void placeMines(Field field, Placement[] possiblePlacements, Solution solution)
      throws CouldNotPlaceObjectException {

    boolean[] binary = EncodingUtils.getBinary(solution.getVariable(0));
    for (int i = 0; i < binary.length; i++) {

      boolean shouldBePlaced = binary[i];
      Placement possiblePlacement = possiblePlacements[i];

      int horPos = possiblePlacement.getHorPos();
      int verPos = possiblePlacement.getVerPos();
      MineSubType mineSubType = possiblePlacement.getMineSubType();
      Mine mine = Mine.createMine(horPos, verPos, mineSubType);
      if (shouldBePlaced && field.baseObjectCanBePlaced(mine)) {
        field.addBaseObject(mine);
      }


    }
  }

}
