package simulation;

/**
 * Exception, that should be thrown, if it was not possible to remove resources from a
 * {@link SimulatableObject}.
 *
 * @author Yannick Kraml
 */
public class RemoveResourceException extends Exception {

  public RemoveResourceException(String message) {
    super(message);
  }
}
