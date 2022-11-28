package model;

import frame.FieldFrame;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import model.enums.TileType;
import model.exceptions.CouldNotPlaceObjectException;
import model.exceptions.CouldNotRemoveObjectException;

/**
 * This class models {@link Field}.
 *
 * @author Yevheniia Makara
 */
public class Field {

  private final Map<Class<? extends BaseObject>, Collection<BaseObject>> objects;
  private final int width;
  private final int height;
  private final Tile[][] tiles;

  /**
   * Constructor of {@link Field}.
   *
   * @param width  width of the {@link Field}.
   * @param height height of the {@link Field}.
   */
  public Field(int width, int height) {
    this.width = width;
    this.height = height;
    objects = new HashMap<>();

    tiles = new Tile[width][height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        tiles[i][j] = new Tile(i, j, TileType.EMPTY);
      }
    }

  }

  /**
   * Returns all {@link Tile} of this {@link Field}.
   *
   * @return Tiles, that construct the {@link Field}.
   */
  public Tile[][] getArray() {
    return tiles;
  }

  /**
   * Adds a {@link BaseObject} to this {@link Field}.
   *
   * @param o new model.BaseObject on the {@link Field}.
   */
  public void addBaseObject(BaseObject o) throws CouldNotPlaceObjectException {

    for (Tile tile : o.getTiles()) {
      int verticalLocation = o.getX() + tile.getRelHorPos();
      int horizontalLocation = o.getY() + tile.getRelVerPos();

      Tile targetTile = tiles[verticalLocation][horizontalLocation];
      boolean targetTileIsEmpty = targetTile.getType().equals(TileType.EMPTY);
      boolean targetTileIsCrossable = targetTile.getType().equals(TileType.CROSSABLE);
      boolean tileIsCrossable = tile.getType().equals(TileType.CROSSABLE);

      if (targetTileIsEmpty || targetTileIsCrossable && tileIsCrossable) {
        tiles[verticalLocation][horizontalLocation] = tile;
      } else {
        throw new CouldNotPlaceObjectException(verticalLocation, horizontalLocation);
      }
    }

    if (!objects.containsKey(o.getClass())) {
      objects.put(o.getClass(), new ArrayList<>());
    }
    objects.get(o.getClass()).add(o);
  }

  /**
   * Removes a {@link BaseObject} of this {@link Field}.
   *
   * @param o model.BaseObject on the {@link Field}, that will be removed.
   */
  public void removeBaseObject(BaseObject o) throws CouldNotRemoveObjectException {
    if (!this.getAllObjects().contains(o)) {
      throw new CouldNotRemoveObjectException(o);
    }

    objects.get(o.getClass()).remove(o);
    for (Tile tile : o.getTiles()) {
      int verticalPostion = o.getX() + tile.getRelHorPos();
      int horizontalPosition = o.getY() + tile.getRelVerPos();
      tiles[verticalPostion][horizontalPosition] = new Tile(verticalPostion, horizontalPosition,
          TileType.EMPTY);
    }

  }

  /**
   * Shows the {@link Field} in a {@link javax.swing.JFrame}.
   */
  public void show() {
    FieldFrame.createFieldFrame(this);
  }

  /**
   * Returns all {@link BaseObject}, that are placed on this {@link Field}.
   *
   * @return Collection of all placed {@link BaseObject}.
   */
  public Collection<BaseObject> getAllObjects() {
    Collection<BaseObject> allObjects = new ArrayList<>();
    objects.forEach((clazz, object) -> allObjects.addAll(object));
    return allObjects;
  }

  /**
   * Returns all {@link BaseObject}s of the field, that belong to a given class.
   *
   * @param clazz Class which all searched {@link BaseObject} should have.
   * @param <K>   ignore
   * @return A collection of all {@link BaseObject}s, that belong to the given class.
   */
  public <K extends BaseObject> Collection<K> getObjectsOfClass(Class<K> clazz) {
    if (!objects.containsKey(clazz)) {
      objects.put(clazz, new ArrayList<>());
    }
    return (Collection<K>) objects.get(clazz);
  }

}
