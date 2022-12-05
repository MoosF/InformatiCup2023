package de.uni_marburg.profit.frame;

import javax.swing.JFrame;
import de.uni_marburg.profit.model.Field;

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
    fieldFrame.add(fieldDrawPanel);
    fieldFrame.setVisible(true);
    fieldFrame.pack();
  }


}
