package frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import model.BaseObject;
import model.Field;
import model.Tile;

/**
 * Extends {@link JPanel} and draws a {@link Field}.
 *
 * @author Yannick Kraml.
 */
public class FieldDrawPanel extends JPanel {


  public static final int TILE_SIZE = 20;
  private final Field field;

  /**
   * Constructor of this Class.
   *
   * @param field {@link Field}, that this {@link JPanel} should draw.
   */
  public FieldDrawPanel(Field field) {
    this.field = field;
    setBackground(Color.WHITE);

    Tile[][] array = field.getArray();
    setPreferredSize(new Dimension(array.length * TILE_SIZE, array[0].length * TILE_SIZE));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    field.getObjects().forEach(baseObject -> drawBaseObject(baseObject, g));

    //Vertical Lines
    g.setColor(Color.BLACK);
    for (int i = 0; i < getHeight() / TILE_SIZE + 1; i++) {
      g.drawLine(0, i * TILE_SIZE, getWidth(), i * TILE_SIZE);
    }

    //Horizontal Lines
    for (int i = 0; i < getWidth() / TILE_SIZE + 1; i++) {
      g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, getHeight());
    }


  }

  private void drawBaseObject(BaseObject baseObject, Graphics g) {
    Color color = new Color((int) (Math.random() * 0x1000000));
    for (Tile tile : baseObject.getTiles()) {
      drawTile(baseObject, tile, g, color);
    }
  }

  private void drawTile(BaseObject baseObject, Tile tile, Graphics g, Color baseObjectColor) {

    char letter;
    switch (tile.getType()) {
      case INPUT -> letter = '+';
      case OUTPUT -> letter = '-';
      case EMPTY -> letter = ' ';
      case CROSSABLE -> letter = '#';
      case SOLID -> letter = '.';
      case MINE_INPUT -> letter = '+';
      case DEPOSIT_OUTPUT -> letter = '-';
      default -> throw new IllegalStateException("Unexpected value: " + tile.getType());
    }

    int screenHorizontalCoordinate = (baseObject.getX() + tile.getX()) * TILE_SIZE;
    int screenVerticalCoordinate = (baseObject.getY() + tile.getY()) * TILE_SIZE;

    g.setColor(baseObjectColor);
    g.fillRect(screenHorizontalCoordinate, screenVerticalCoordinate, TILE_SIZE, TILE_SIZE);

    int offsetVertical = TILE_SIZE / 2 + 4;
    int offsetHorizontal = TILE_SIZE / 2 - 4;

    g.setColor(Color.WHITE);
    g.drawString(String.valueOf(letter), screenHorizontalCoordinate + offsetHorizontal,
        screenVerticalCoordinate + offsetVertical);
  }


}
