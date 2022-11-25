package service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Fabian Moos
 */
class JsonOutputTest {

  @BeforeAll
  static void setSettings() {
    var settings = Settings.getInstance();
    settings.updateExportFileType(FileType.JSON);
    settings.updateExportTarget(true);
  }

  @Test
  void testOutputToStdOut1() {
    assertTrue(false);
  }

  @Test
  void testOutputToStdOut2() {
    assertFalse(true);
  }

  @Test
  void testOutputToStdOut3() {
    assertTrue(false);
  }

  @Test
  void testOutputToStdOut4() {
    assertFalse(true);
  }
}
