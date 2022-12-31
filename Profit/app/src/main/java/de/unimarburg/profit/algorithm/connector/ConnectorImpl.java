package de.unimarburg.profit.algorithm.connector;

import de.unimarburg.profit.model.Factory;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Mine;
import de.unimarburg.profit.model.Obstacle;
import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

/**
 * This class supplies functionality for finding reachable {@link Mine}s for a {@link Factory} and
 * for connecting {@link Mine}s to a {@link Factory}.
 *
 * @author Fabian Moos
 */
public class ConnectorImpl implements Connector {

  private Field field;
  private TileConnectionInfo[][] connectionMatrix;


  /**
   * TODO
   * @param field TODO
   */
  public ConnectorImpl(Field field) {
    this.field = field;
    this.connectionMatrix = new TileConnectionInfo[field.getHeight()][field.getWidth()];
    for (TileConnectionInfo[] matrix : this.connectionMatrix) {
      Arrays.fill(matrix, null);
    }
  }

  /**
   * TODO
   *
   * @param mines TODO
   * @param field TODO
   */
  @Override
  public void connect(Collection<Mine> mines, Field field) {
    /* */
  }

  /**
   * Connects all mines to the factory if possible.
   *
   * <p>A modified BFS is used to find positions for the placed conveyor belts that are as optimal
   * as possible. This method does not check if the given mines can actually be connected to the
   * given {@link Factory}. The method {@code getReachableMines(Field, Factory)} can be used to find
   * all {@link Mine}s that are not separated from the {@link Factory} by obstacles.</p>
   *
   * @param factory        The {@link Factory} the {@link Mine}s will be connected to.
   * @param minesToConnect The {@link Mine}s that are supposed to be connected to the given
   *                       {@link Factory}.
   * @return {@code true} if all mines in the {@link Collection} could be connected, false
   * otherwise.
   */
  @Override
  public boolean connectMines(Factory factory, Collection<Mine> minesToConnect) {
    /* Just use the results of getReachableMines to find paths from the given Mines to the Factory.
     */
    return false;
  }

  /**
   * Finds all {@link Mine}s that are not separated from the given {@link Factory} by
   * obstacles
   *
   * @param factory The {@link Factory} the reachable mines are searched for.
   * @return a list of mines that potentially can be connected to the factory.
   */
  @Override
  public Collection<Mine> getReachableMines(Factory factory) {
    /* For every free Tile around the Factory do the following (modified BFS)
     *  1. Try to place all possible variants of a conveyor. The conveyor can be placed if the
     *     following is true:
     *     i. The output field of the conveyor must be on the current Tile.
     *     ii. The input field of the conveyor lies on a Tile that is either unhandled or has been
     *         handled (and doesn't deny the conveyor to be placed?).
     *     iii. The field allows the conveyor to be placed.
     *  2. Write all relevant information into the current Tile and all target Tiles.
     *  3. If the input Tile of the placable conveyor hasn't been handled, queue all Tiles around
     *     the input that are not blocked and haven't been handled yet, into a FIFO-queue.
     *  4. If the Tiles around the Input Tile have been handled already, check if it is an input or
     *     an output that has been placed there, if it was an input, queue the Tile, too.
     *  5. The newly placed conveyor gets a special number 0 if it is directly connected to the
     *     factory or the special number of the previous conveyor input + 1.
     *  6. When there is a Mine connected to the input of the conveyor, add the Mine to the list
     *     of reachable mines.
     * When the FIFO queue is empty (or every Mine on the field has been reached?), start search
     * from the next free Tile around the Factory.
     * Comment 1. In the end this method tries to determine for every Tile:
     *            o Which Tiles are reachable from the Tile (starting from the mine).
     *            o How far away from the factory is the Tile (measured in conveyor steps).
     *            This information is saved in an 2-dimensional array. Most likely as a map of
     *            Coordinates, the key being the next input field, the value being a list of
     *            Coordinates where the output of the field can possibly be. Together with the
     *            rest steps that must be taken if using the respective output coordinates.
     */
    return new Vector<>();
  }

  private static class TileConnectionInfo {

  }
}
