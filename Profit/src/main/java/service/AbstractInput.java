package service;

import java.util.List;
import model.FixedObject;
import model.Product;

/**
 * This abstract class represents an input of a given profit game.
 *
 * @author Fabian Moos
 */
public abstract class AbstractInput {

  protected int width;
  protected int height;
  protected int turns;
  protected int time;
  protected final List<FixedObject> objects;
  protected final List<Product> products;

  AbstractInput(List<FixedObject> objects, List<Product> products) {
    this.objects = objects;
    this.products = products;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public List<FixedObject> getInputObjects() {
    return objects;
  }

  public List<Product> getProducts() {
    return products;
  }

  public int getTurns() {
    return turns;
  }

  public int getTime() {
    return time;
  }
}
