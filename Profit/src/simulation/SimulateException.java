package simulation;

public class SimulateException extends Exception {

  public SimulateException(Exception e) {
    super(e);
  }

  public SimulateException(String message) {
    super(message);
  }
}
