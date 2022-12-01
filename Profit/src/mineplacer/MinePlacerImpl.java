package mineplacer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import model.Deposit;
import model.Field;
import model.Mine;
import model.exceptions.CouldNotPlaceObjectException;
import model.exceptions.CouldNotRemoveObjectException;

public final class MinePlacerImpl extends MinePlacer {

  public MinePlacerImpl(Field field) {
    super(field);
  }

  @Override
  public boolean placeRandomMine() throws CouldNotPlaceObjectException {
    Field field = getField();
    Collection<Deposit> deposits = field.getObjectsOfClass(Deposit.class);



    return false;
  }

  @Override
  public boolean removeRandomMine() throws CouldNotRemoveObjectException {
    Field field = getField();
    List<Mine> mines = new ArrayList<>(field.getObjectsOfClass(Mine.class));

    if(mines.isEmpty()){
      return false;
    }

    Collections.shuffle(mines);
    Mine randomMine = mines.iterator().next();
    field.removeBaseObject(randomMine);

    return true;
  }


}
