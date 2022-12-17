package de.unimarburg.profit.algorithm.productchooser;

import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Product;
import de.unimarburg.profit.model.enums.ResourceType;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link ProductAndMinesFinder}.
 *
 * @author Yannick Kraml
 */
public class ProductAndMinesFinderImpl implements ProductAndMinesFinder {


  @Override
  public List<TypeAndMinesCombination> findProductMinesCombination(
      Map<Mine, ResourceType> connectableMines, Collection<Product> products) {
    List<TypeAndMinesCombination> combs = new LinkedList<>();

    //Group the mines by their resourceType.
    Map<ResourceType, Collection<Mine>> resourceMineMap = new HashMap<>();
    for (ResourceType value : ResourceType.values()) {
      resourceMineMap.put(value, new LinkedList<>());
    }
    connectableMines.forEach((mine, resourceType) -> resourceMineMap.get(resourceType).add(mine));

    //Check each product.
    for (Product product : products) {

      Map<ResourceType, Integer> neededResourcesMap = product.getNeededResourcesMap();

      //Check if all needed resources exist.
      boolean allNeededResourcesExist = true;
      for (ResourceType resourceType : neededResourcesMap.keySet()) {
        if (resourceMineMap.get(resourceType).isEmpty()) {
          allNeededResourcesExist = false;
        }
      }

      //Add all mines with the needed resources.
      if (allNeededResourcesExist) {
        Map<ResourceType, Collection<Mine>> minesToConnect = new HashMap<>();

        for (ResourceType resourceType : neededResourcesMap.keySet()) {
          minesToConnect.put(resourceType, resourceMineMap.get(resourceType));
        }

        combs.add(new TypeAndMinesCombination(product.getType(), minesToConnect));
      }
    }

    return combs;
  }

}
