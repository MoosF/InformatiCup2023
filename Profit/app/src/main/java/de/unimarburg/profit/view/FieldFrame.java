package de.unimarburg.profit.view;


import de.unimarburg.profit.model.Field;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;

/**
 * This class can show a {@link Field} inside a {@link JFrame}.
 *
 * @author Yannick Kraml
 */
public class FieldFrame extends JFrame {

  private FieldFrame() {
    setTitle("Profit!");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
  }

  /**
   * Creates a {@link FieldFrame}.
   *
   * @param field {@link Field} that should be shown.
   */
  public static void createFieldFrame(Field field) {
    FieldFrame fieldFrame = new FieldFrame();
    FieldDrawPanel fieldDrawPanel = new FieldDrawPanel(field);

    new Timer().scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        fieldDrawPanel.repaint();
      }
    }, 0, 1000 / 24);

    fieldDrawPanel.requestFocus();
    fieldFrame.add(fieldDrawPanel);
    fieldFrame.setVisible(true);
    fieldFrame.pack();
  }


}
