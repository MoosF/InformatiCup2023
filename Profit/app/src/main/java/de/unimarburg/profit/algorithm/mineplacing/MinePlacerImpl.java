package de.unimarburg.profit.algorithm.mineplacing;

import de.unimarburg.profit.model.Deposit;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Tile;
import de.unimarburg.profit.model.enums.TileType;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import de.unimarburg.profit.model.exceptions.CouldNotRemoveObjectException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * Implementation of {@link MinePlacer}. It works with evolutionary algorithms.
 *
 * @author Yannick Kraml.
 */
public class MinePlacerImpl implements MinePlacer {


  @Override
  public Map<Mine, Deposit> placeMines(Field field, Map<Mine, Deposit> possibleMines) {

    Map<Mine, Deposit> placedMines = new HashMap<>();

    for (Entry<Mine, Deposit> entry : possibleMines.entrySet()) {
      Mine mine = entry.getKey();
      Deposit deposit = entry.getValue();
      try {
        field.addBaseObject(mine);
        placedMines.put(mine, deposit);
      } catch (CouldNotPlaceObjectException ignored) {
        //If a single Mines can not be placed. It will just be ignored.
      }
    }

    return placedMines;
  }

  @Override
  public void removeUselessMines(Field field) {

    Collection<Mine> mines = field.getObjectsOfClass(Mine.class);

    for (Mine mine : mines) {

      boolean isUseless = true;

      Optional<Tile> outputTileOptional = Arrays.stream(mine.getTiles()).filter(
          tile -> tile.getType().equals(TileType.OUTPUT)).findFirst();

      if (outputTileOptional.isPresent()) {
        Tile outputTile = outputTileOptional.get();

        int x = mine.getX() + outputTile.getRelHorPos();
        int y = mine.getY() + outputTile.getRelVerPos();

        Tile[][] tiles = field.getTiles();
        isUseless = !(neighborIsInput(tiles, x + 1, y)
            | neighborIsInput(tiles, x - 1, y)
            | neighborIsInput(tiles, x, y - 1)
            | neighborIsInput(tiles, x, y + 1));

      }

      if (isUseless) {
        try {
          field.removeBaseObject(mine);
        } catch (CouldNotRemoveObjectException e) {
          //Ignore
        }
      }

    }


  }

  private static boolean neighborIsInput(Tile[][] tiles, int x, int y) {
    return x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length && tiles[x][y].getType()
        .equals(TileType.INPUT);
  }


}
