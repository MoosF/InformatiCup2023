package service;

import java.util.List;
import model.FixedObject;
import model.Product;

/**
 * @author Fabian Moos
 */
public abstract class AbstractInput implements InputFile {
  protected int width;
  protected int height;
  protected int turns;
  protected int time;
  protected final List<FixedObject> objects;
  protected final List<Product> products;

  AbstractInput(List<FixedObject> objects, List <Product> products) {
    this.objects = objects;
    this.products = products;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public List<FixedObject> getInputObjects() {
    return objects;
  }

  @Override
  public List<Product> getProducts() {
    return products;
  }

  @Override
  public int getTurns() {
    return turns;
  }

  @Override
  public int getTime() {
    return time;
  }
}
