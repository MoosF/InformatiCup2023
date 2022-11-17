package simulation;

import java.util.Collection;

public class Coupled {

  private final SimulatableObject receiver;
  private final Collection<SimulatableObject> giver;


  public Coupled(SimulatableObject receiver, Collection<SimulatableObject> giver) {
    this.receiver = receiver;
    this.giver = giver;
  }

  public SimulatableObject getReceiver() {
    return receiver;
  }

  public Collection<SimulatableObject> getGiver() {
    return giver;
  }
}
