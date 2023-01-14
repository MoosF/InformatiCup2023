package de.unimarburg.profit.view;

import de.unimarburg.profit.model.BaseObject;
import de.unimarburg.profit.model.Field;
import de.unimarburg.profit.model.Tile;
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
  private static final int BORDER_WIDTH = 2;
  private static final double LINE_WIDTH = TILE_SIZE * 0.1;
  private static final double LINE_LENGTH = TILE_SIZE * 0.4;
  private static final Color LINE_COLOR = Color.GRAY;

  private final Field field;
  private final Environment env;

  /**
   * Constructor of this Class.
   *
   * @param field {@link Field}, that this {@link JPanel} should draw.
   */
  public FieldDrawPanel(Field field) {
    this.env = new Environment(700, 500);
    this.field = field;

    setBackground(Color.WHITE);
    setPreferredSize(new Dimension(env.getWidth(), env.getHeight()));

    PanningAndZoomMouseAdapter myMouseAdapter = new PanningAndZoomMouseAdapter(env);
    addMouseMotionListener(myMouseAdapter);
    addMouseListener(myMouseAdapter);
    addMouseWheelListener(myMouseAdapter);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    //Objects
    field.getAllObjects().forEach(baseObject -> drawBaseObject(baseObject, g));

    //Horizontal Lines
    g.setColor(LINE_COLOR);
    for (int i = 0; i < field.getHeight() + 1; i++) {
      drawLine(g, 0, i * TILE_SIZE, field.getWidth() * TILE_SIZE, i * TILE_SIZE);
    }

    //Vertical Lines
    for (int i = 0; i < field.getWidth() + 1; i++) {
      drawLine(g, i * TILE_SIZE, 0, i * TILE_SIZE, field.getHeight() * TILE_SIZE);
    }

  }

  private void drawBaseObject(BaseObject baseObject, Graphics g) {
    Color color;
    switch (baseObject.getClass().getSimpleName()) {
      case "Obstacle" -> color = Color.DARK_GRAY;
      case "Deposit" -> color = Color.LIGHT_GRAY;
      case "Conveyor" -> color = Color.ORANGE;
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

    int horPos = (baseObject.getX() + tile.getRelHorPos()) * TILE_SIZE;
    int verPos = (baseObject.getY() + tile.getRelVerPos()) * TILE_SIZE;

    g.setColor(color.darker().darker());

    if (!hasTopNeighbor) {
      fillRectangle(g, horPos, verPos, TILE_SIZE, BORDER_WIDTH);
    }

    if (!hasRightNeighbor) {
      fillRectangle(g, horPos + TILE_SIZE - BORDER_WIDTH, verPos, BORDER_WIDTH, TILE_SIZE);
    }

    if (!hasBottomNeighbor) {
      fillRectangle(g, horPos, verPos + TILE_SIZE - BORDER_WIDTH, TILE_SIZE, BORDER_WIDTH);
    }

    if (!hasLeftNeighbor) {
      fillRectangle(g, horPos, verPos, BORDER_WIDTH, TILE_SIZE);
    }
  }

  private void drawTile(BaseObject baseObject, Tile tile, Graphics g, Color baseObjectColor) {

    int horPos = (baseObject.getX() + tile.getRelHorPos()) * TILE_SIZE;
    int verPos = (baseObject.getY() + tile.getRelVerPos()) * TILE_SIZE;

    g.setColor(baseObjectColor);
    fillRectangle(g, horPos, verPos, TILE_SIZE, TILE_SIZE);

    g.setColor(Color.WHITE);
    switch (tile.getType()) {
      case INPUT, MINE_INPUT -> drawInput(g, horPos, verPos);
      case OUTPUT, DEPOSIT_OUTPUT -> drawOutput(g, horPos, verPos);
      case EMPTY, SOLID -> {
      }
      case CROSSABLE -> drawCrossable(g, horPos, verPos);
      default -> throw new IllegalStateException("Unexpected value: " + tile.getType());
    }

  }

  private void drawCrossable(Graphics g, int horPos, int verPos) {

    fillRectangle(
        g,
        (int) (horPos + TILE_SIZE / 5 * 2 - LINE_WIDTH / 2),
        (int) (verPos + TILE_SIZE / 2 - LINE_LENGTH / 2),
        (int) LINE_WIDTH,
        (int) LINE_LENGTH
    );

    fillRectangle(
        g,
        (int) (horPos + TILE_SIZE / 5 * 3 - LINE_WIDTH / 2),
        (int) (verPos + TILE_SIZE / 2 - LINE_LENGTH / 2),
        (int) LINE_WIDTH,
        (int) LINE_LENGTH
    );

    fillRectangle(
        g,
        (int) (horPos + TILE_SIZE / 2 - LINE_LENGTH / 2),
        (int) (verPos + TILE_SIZE / 5 * 2 - LINE_WIDTH / 2),
        (int) LINE_LENGTH,
        (int) LINE_WIDTH
    );

    fillRectangle(
        g,
        (int) (horPos + TILE_SIZE / 2 - LINE_LENGTH / 2),
        (int) (verPos + TILE_SIZE / 5 * 3 - LINE_WIDTH / 2),
        (int) LINE_LENGTH,
        (int) LINE_WIDTH
    );

  }

  private void drawOutput(Graphics g, int horPos, int verPos) {
    fillRectangle(
        g,
        (int) (horPos + (TILE_SIZE - LINE_LENGTH) / 2),
        (int) (verPos + TILE_SIZE / 2 - LINE_WIDTH / 2),
        (int) LINE_LENGTH,
        (int) LINE_WIDTH
    );

  }

  private void drawInput(Graphics g, int horPos, int verPos) {

    fillRectangle(
        g,
        (int) (horPos + TILE_SIZE / 2 - LINE_LENGTH / 2),
        (int) (verPos + TILE_SIZE / 2 - LINE_WIDTH / 2),
        (int) LINE_LENGTH,
        (int) LINE_WIDTH
    );

    fillRectangle(
        g,
        (int) (horPos + TILE_SIZE / 2 - LINE_WIDTH / 2),
        (int) (verPos + TILE_SIZE / 2 - LINE_LENGTH / 2),
        (int) LINE_WIDTH,
        (int) LINE_LENGTH
    );

  }


  protected void fillRectangle(Graphics g, int horPos, int verPos, int width, int height) {

    DoublePoint topLeftWorldPoint = new DoublePoint(horPos, verPos);
    DoublePoint botRightWorldPoint = new DoublePoint(horPos + width, verPos + height);

    DoublePoint topLeftScreenPoint = PointCalculator.calcWorldToScreen(env, topLeftWorldPoint);
    DoublePoint botRightScreenPoint = PointCalculator.calcWorldToScreen(env, botRightWorldPoint);

    double screenWidth = botRightScreenPoint.getX() - topLeftScreenPoint.getX();
    double screenHeight = botRightScreenPoint.getY() - topLeftScreenPoint.getY();

    g.fillRect(
        (int) Math.floor(topLeftScreenPoint.getX()),
        (int) Math.floor(topLeftScreenPoint.getY()),
        (int) Math.ceil(screenWidth),
        (int) Math.ceil(screenHeight)
    );
  }

  protected void drawLetter(Graphics g, char letter, int horPos, int verPos) {

    DoublePoint worldPoint = new DoublePoint(horPos, verPos);
    DoublePoint screenPoint = PointCalculator.calcWorldToScreen(env, worldPoint);

    g.drawString(String.valueOf(letter), (int) screenPoint.getX(), (int) screenPoint.getY());
  }

  protected void drawLine(Graphics g, int fromX, int fromY, int toX, int toY) {

    DoublePoint fromWorldPoint = new DoublePoint(fromX, fromY);
    DoublePoint toWorldPoint = new DoublePoint(toX, toY);

    DoublePoint fromScreenPoint = PointCalculator.calcWorldToScreen(env, fromWorldPoint);
    DoublePoint toScreenPoint = PointCalculator.calcWorldToScreen(env, toWorldPoint);

    g.drawLine(
        (int) fromScreenPoint.getX(),
        (int) fromScreenPoint.getY(),
        (int) toScreenPoint.getX(),
        (int) toScreenPoint.getY()
    );
  }
}
