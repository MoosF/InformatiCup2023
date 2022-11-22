package service;

import java.util.List;

import model.Field;
import model.FixedObject;
import model.Product;

/**
 * @author Fabian Moos
 */
public interface Input {

    /**
     * @return the width of {@link Field}
     */
    int getWidth();

    /**
     * @return the height of {@link Field}
     */
    int getHeight();

    /**
     * @return the list of {@link FixedObject}s for this {@link Field}.
     */
    List<FixedObject> getInputObjects();

    /**
     *
     * @return the List of {@link Product}s for this {@link Field}.
     */
    List<Product> getProducts();

    int getTurns();

    int getTime();
}
