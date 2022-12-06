package de.uni.marburg.profit.frame;

import de.uni.marburg.profit.model.BaseObject;
import de.uni.marburg.profit.model.Field;
import de.uni.marburg.profit.model.Tile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Extends {@link JPanel} and draws a {@link Field}.
 *
 * @author Yannick Kraml.
 */
public class FieldDrawPanel extends JPanel {


  private static final int TILE_SIZE = 20;
  private static final int BORDER_WITH = 2;
  private static final Color LINE_COLOR = Color.GRAY;
  private final Field field;

  /**
   * Constructor of this Class.
   *
   * @param field {@link Field}, that this {@link JPanel} should draw.
   */
  public FieldDrawPanel(Field field) {
    this.field = field;
    setBackground(Color.WHITE);

    Tile[][] array = field.getTiles();
    setPreferredSize(new Dimension(array.length * TILE_SIZE, array[0].length * TILE_SIZE));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    //Objects
    field.getAllObjects().forEach(baseObject -> drawBaseObject(baseObject, g));

    //Horizontal Lines
    g.setColor(LINE_COLOR);
    for (int i = 0; i < getHeight() / TILE_SIZE + 1; i++) {
      g.drawLine(0, i * TILE_SIZE, getWidth(), i * TILE_SIZE);
    }

    //Vertical Lines
    for (int i = 0; i < getWidth() / TILE_SIZE + 1; i++) {
      g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, getHeight());
    }


  }

  private void drawBaseObject(BaseObject baseObject, Graphics g) {
    Color color;
    switch (baseObject.getClass().getSimpleName()) {
      case "Obstacle" -> color = Color.DARK_GRAY;
      case "Deposit" -> color = Color.LIGHT_GRAY;
      case "Conveyer" -> color = Color.ORANGE;
      case "Mine" -> color = new Color(0, 200, 40);
      case "Factory" -> color = new Color(64, 93, 239);
      case "Combiner" -> color = new Color(255, 150, 0);
      default -> color = new Color((int) (Math.random() * 0x1000000));
    }

    Tile[] tiles = baseObject.getTiles();
    for (Tile tile : tiles) {
      drawTile(baseObject, tile, g, color);
      drawBorder(baseObject, tile, g, color);
    }

  }

  private void drawBorder(BaseObject baseObject, Tile tile, Graphics g, Color color) {
    int relHorPos = tile.getRelHorPos();
    int relVerPos = tile.getRelVerPos();
    boolean hasTopNeighbor = false;
    boolean hasRightNeighbor = false;
    boolean hasBottomNeighbor = false;
    boolean hasLeftNeighbor = false;

    for (Tile neighbor : baseObject.getTiles()) {
      int relHorPosNeighbor = neighbor.getRelHorPos();
      int relVerPosNeighbor = neighbor.getRelVerPos();

      if (relVerPos - 1 == relVerPosNeighbor && relHorPos == relHorPosNeighbor) {
        hasTopNeighbor = true;
      }

      if (relHorPos + 1 == relHorPosNeighbor && relVerPos == relVerPosNeighbor) {
        hasRightNeighbor = true;
      }

      if (relVerPos + 1 == relVerPosNeighbor && relHorPos == relHorPosNeighbor) {
        hasBottomNeighbor = true;
      }

      if (relHorPos - 1 == relHorPosNeighbor && relVerPos == relVerPosNeighbor) {
        hasLeftNeighbor = true;
      }
    }

    int screenHorizontalCoordinate = (baseObject.getX() + tile.getRelHorPos()) * TILE_SIZE;
    int screenVerticalCoordinate = (baseObject.getY() + tile.getRelVerPos()) * TILE_SIZE;

    g.setColor(color.darker().darker());

    if (!hasTopNeighbor) {
      g.fillRect(
          screenHorizontalCoordinate,
          screenVerticalCoordinate,
          TILE_SIZE,
          BORDER_WITH
      );
    }

    if (!hasRightNeighbor) {
      g.fillRect(
          screenHorizontalCoordinate + TILE_SIZE - BORDER_WITH,
          screenVerticalCoordinate,
          BORDER_WITH,
          TILE_SIZE);
    }

    if (!hasBottomNeighbor) {
      g.fillRect(
          screenHorizontalCoordinate,
          screenVerticalCoordinate + TILE_SIZE - BORDER_WITH,
          TILE_SIZE,
          BORDER_WITH
      );
    }

    if (!hasLeftNeighbor) {
      g.fillRect(
          screenHorizontalCoordinate,
          screenVerticalCoordinate,
          BORDER_WITH,
          TILE_SIZE
      );
    }
  }

  private void drawTile(BaseObject baseObject, Tile tile, Graphics g, Color baseObjectColor) {

    char letter;
    switch (tile.getType()) {
      case INPUT, MINE_INPUT -> letter = '+';
      case OUTPUT, DEPOSIT_OUTPUT -> letter = '-';
      case EMPTY, SOLID -> letter = ' ';
      case CROSSABLE -> letter = '#';
      default -> throw new IllegalStateException("Unexpected value: " + tile.getType());
    }

    int screenHorizontalCoordinate = (baseObject.getX() + tile.getRelHorPos()) * TILE_SIZE;
    int screenVerticalCoordinate = (baseObject.getY() + tile.getRelVerPos()) * TILE_SIZE;

    g.setColor(baseObjectColor);
    g.fillRect(screenHorizontalCoordinate, screenVerticalCoordinate, TILE_SIZE, TILE_SIZE);

    int offsetVertical = TILE_SIZE / 2 + 4;
    int offsetHorizontal = TILE_SIZE / 2 - 4;

    g.setColor(Color.WHITE);
    g.drawString(String.valueOf(letter), screenHorizontalCoordinate + offsetHorizontal,
        screenVerticalCoordinate + offsetVertical);
  }


}
