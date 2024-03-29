package de.unimarburg.profit.service;

import de.unimarburg.profit.service.InputOutputHandle.FileType;

/**
 * Saves Settings for the IO.
 *
 * @author Fabian Moos
 */
public class Settings {

  private static Settings instance = null;

  private FileType exportFileType = FileType.JSON;
  private FileType importFileType = FileType.JSON;
  private boolean exportToStdOut = true;
  private String exportFileName = "default.json";
  private boolean importFromStdIn = true;

  private Settings() {
  }

  public boolean exportTargetIsStdOut() {
    return this.exportToStdOut;
  }

  public String getExportFileName() {
    return this.exportFileName;
  }

  public FileType getExportFileType() {
    return this.exportFileType;
  }

  public FileType getImportFileType() {
    return this.importFileType;
  }

  /**
   * Singleton getInstance() Method.
   *
   * @return Instance of {@link Settings}.
   */
  public static Settings getInstance() {
    if (instance == null) {
      instance = new Settings();
    }
    return instance;
  }

  public boolean importTargetIsStdIn() {
    return this.importFromStdIn;
  }

  public void updateExportFileName(String exportFileName) {
    this.exportFileName = exportFileName;
  }

  public void updateExportFileType(FileType exportFileType) {
    this.exportFileType = exportFileType;
  }

  public void updateExportTarget(boolean exportToStdOut) {
    this.exportToStdOut = exportToStdOut;
  }

  public void updateImportFileType(FileType importFileType) {
    this.importFileType = importFileType;
  }

  public void updateImportTarget(boolean importFromStdIn) {
    this.importFromStdIn = importFromStdIn;
  }
}
