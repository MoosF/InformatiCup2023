package de.unimarburg.profit.model;

import de.unimarburg.profit.model.enums.TileType;
import de.unimarburg.profit.model.exceptions.CouldNotPlaceObjectException;
import de.unimarburg.profit.model.exceptions.CouldNotRemoveObjectException;
import de.unimarburg.profit.view.FieldFrame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

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
        tiles[i][j] = new Tile(0, 0, TileType.EMPTY);
      }
    }

  }

  /**
   * Returns all {@link Tile} of this {@link Field}.
   *
   * @return Tiles, that construct the {@link Field}.
   */
  public Tile[][] getTiles() {
    return tiles;
  }

  /**
   * Adds a {@link BaseObject} to this {@link Field}.
   *
   * @param o new model.BaseObject on the {@link Field}.
   */
  public void addBaseObject(BaseObject o) throws CouldNotPlaceObjectException {

    if (baseObjectCanBePlaced(o)) {

      for (Tile tile : o.getTiles()) {
        int verPos = o.getX() + tile.getRelHorPos();
        int horPos = o.getY() + tile.getRelVerPos();
        tiles[verPos][horPos] = tile;
      }

      if (!objects.containsKey(o.getClass())) {
        objects.put(o.getClass(), new ArrayList<>());
      }
      objects.get(o.getClass()).add(o);


    } else {
      throw new CouldNotPlaceObjectException(o.getX(), o.getY());
    }

  }

  /**
   * Checks if a {@link BaseObject} can be placed.
   *
   * @param o {@link BaseObject} to be placed.
   * @return True if the {@link BaseObject} can be placed. False otherwise.
   */
  public boolean baseObjectCanBePlaced(BaseObject o) {

    Collection<Tile> tilesCollection = Arrays.stream(o.getTiles()).toList();

    for (Tile tile : tilesCollection) {
      int horPos = o.getX() + tile.getRelHorPos();
      int verPos = o.getY() + tile.getRelVerPos();
      if (!tileCanBePlaced(horPos, verPos, tile)) {
        return false;
      }
    }

    return true;
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
  @SuppressWarnings("unchecked")
  public <K extends BaseObject> Collection<K> getObjectsOfClass(Class<K> clazz) {
    if (!objects.containsKey(clazz)) {
      objects.put(clazz, new ArrayList<>());
    }
    return (Collection<K>) objects.get(clazz);
  }


  /**
   * Makes a copy of this {@link Field}. Beware, that the same {@link BaseObject}s are used for the
   * copy.
   *
   * @return Copy of this {@link Field}
   */
  public Field copy() {
    Field copy = new Field(getWidth(), getHeight());
    for (BaseObject object : getAllObjects()) {
      try {
        copy.addBaseObject(object);
      } catch (CouldNotPlaceObjectException e) {
        throw new RuntimeException(e);
      }
    }

    return copy;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  private boolean tileCanBePlaced(int horPos, int verPos, Tile tile) {

    //Check if Tile inside borders.
    if (verPos < 0 || horPos < 0 || verPos >= height || horPos >= width) {
      return false;
    }

    //Check if Tile is placed on top of another tile.
    Tile targetTile = tiles[horPos][verPos];
    boolean targetTileIsEmpty = targetTile.getType().equals(TileType.EMPTY);
    boolean targetTileIsCrossable = targetTile.getType().equals(TileType.CROSSABLE);
    boolean tileIsCrossable = tile.getType().equals(TileType.CROSSABLE);
    if (!(targetTileIsEmpty || targetTileIsCrossable && tileIsCrossable)) {
      return false;
    }

    TileType type = tile.getType();
    Collection<Tile> neighbors = getNeighbors(horPos, verPos);

    for (Tile neighbor : neighbors) {

      //DEPOSIT_OUTPUT can not be placed next to a INPUT
      if (Objects.equals(neighbor.getType(), TileType.INPUT) && Objects.equals(type,
          TileType.DEPOSIT_OUTPUT)) {
        return false;
      }

      //INPUT can not be placed next to a DEPOSIT_OUTPUT
      if (Objects.equals(neighbor.getType(), TileType.DEPOSIT_OUTPUT) && Objects.equals(type,
          TileType.INPUT)) {
        return false;
      }

      //Output can not be placed next to a MINE_INPUT
      if (Objects.equals(neighbor.getType(), TileType.MINE_INPUT) && Objects.equals(type,
          TileType.OUTPUT)) {
        return false;
      }

      //MINE_INPUT can not be placed next to a OUTPUT
      if (Objects.equals(neighbor.getType(), TileType.OUTPUT) && Objects.equals(type,
          TileType.MINE_INPUT)) {
        return false;
      }

    }

    //Checks if it only connects to one input, if it is an output
    if (Objects.equals(type, TileType.OUTPUT)) {
      long inputCount = neighbors.stream()
          .filter(neighbor -> neighbor.getType().equals(TileType.INPUT)).count();
      if (inputCount > 1) {
        return false;
      }
    }

    //Checks if connected to an output, that is already connected to an input.
    if (type.equals(TileType.INPUT) || type.equals(TileType.MINE_INPUT)) {

      for (Tile neighbor : neighbors) {

        boolean neighborIsRegularOutput = neighbor.getType().equals(TileType.OUTPUT);
        boolean neighborIsDepositOutput = neighbor.getType().equals(TileType.DEPOSIT_OUTPUT);
        boolean neighborIsOutput = neighborIsRegularOutput || neighborIsDepositOutput;
        boolean neighborHasInput = hasInputAsNeighbor(neighbor);

        if (neighborIsOutput && neighborHasInput) {
          return false;
        }
      }
    }

    return true;
  }

  private boolean hasInputAsNeighbor(Tile tile) {

    if (tile.getObject().isPresent()) {
      BaseObject object = tile.getObject().get();
      int horPos = object.getX() + tile.getRelHorPos();
      int verPos = object.getY() + tile.getRelVerPos();

      Collection<Tile> neighbors = getNeighbors(horPos, verPos);
      for (Tile neighbor : neighbors) {
        if (neighbor.getType().equals(TileType.INPUT) || neighbor.getType()
            .equals(TileType.MINE_INPUT)) {
          return true;
        }
      }
      return false;
    }

    return false;
  }

  private Collection<Tile> getNeighbors(int horPos, int verPos) {
    Collection<Tile> neighbors = new LinkedList<>();
    addNeighbor(neighbors, horPos, verPos - 1);
    addNeighbor(neighbors, horPos + 1, verPos);
    addNeighbor(neighbors, horPos, verPos + 1);
    addNeighbor(neighbors, horPos - 1, verPos);
    return neighbors;
  }

  private void addNeighbor(Collection<Tile> neighbors, int horPos, int verPos) {
    if (horPos >= 0 && verPos >= 0 && horPos < tiles.length && verPos < tiles[horPos].length) {
      neighbors.add(tiles[horPos][verPos]);
    }
  }

}