package model;

import frame.FieldFrame;

import java.util.LinkedList;
import java.util.List;

/**
 * This class models {@link Field}.
 *
 * @author Yevheniia Makara
 */
public class Field {

  private List<BaseObject> objects;
  private final int width;
  private final int height;
  private Tile[][] tiles;

  /**
   * @param width  width of the {@link Field}.
   * @param height height of the {@link Field}.
   */
  public Field(int width, int height) {
    this.width = width;
    this.height = height;
    objects = new LinkedList<>();
    tiles = new Tile[width][height];
  }

  /**
   * @param width   width of the {@link Field}.
   * @param height  height of the {@link Field}.
   * @param objects input objects of the {@link Field}.
   */
  public Field(int width, int height, List<BaseObject> objects) {
    this(width, height);
    this.objects = objects;
  }

  /**
   * @return Tiles, that construct the {@link Field}.
   */
  public Tile[][] getArray() {
    tiles = new Tile[width][height];
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[0].length; j++) {
        tiles[i][j] = new Tile(i, j, TileType.EMPTY);
      }
    }

    for (BaseObject o : objects) {
      for (Tile tile : o.getTiles()) {
        tiles[o.getX() + tile.getX()][o.getY() + tile.getY()] = tile;
      }
    }

    return tiles;
  }

  /**
   * @param o new model.BaseObject on the {@link Field}.
   */
  public void addBaseObject(BaseObject o) {
    objects.add(o);

    for (Tile tile : o.getTiles()) {
      tiles[o.getX() + tile.getX()][o.getY() + tile.getY()] = tile;
    }
  }

  /**
   * @param o model.BaseObject on the {@link Field}, that will be removed.
   */
  public void removeBaseObject(BaseObject o) {
    for (Tile tile : o.getTiles()) {
      tiles[o.getX() + tile.getX()][o.getY() + tile.getY()] =
          new Tile(o.getX() + tile.getX(), o.getY() + tile.getY(), TileType.EMPTY);
    }

    objects.remove(o);
  }

  public void show() {
    FieldFrame.createFieldFrame(this);
  }

  public List<BaseObject> getObjects() {
    return objects;
  }
}
