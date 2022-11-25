package service;

import static service.FileType.JSON;

/**
 * @author Fabian Moos
 */
public class Settings {

  private static Settings instance = null;

  private FileType exportFileType = JSON;
  private boolean exportToStdOut = true;
  private FileType importFileType = JSON;
  private boolean importFromStdIn = false;

  private Settings() {
  }

  public boolean exportTargetIsStdOut() {
    return this.exportToStdOut;
  }

  public FileType getExportFileType() {
    return this.exportFileType;
  }

  public FileType getImportFileType() {
    return this.exportFileType;
  }

  public static Settings getInstance() {
    if (instance == null) {
      instance = new Settings();
    }
    return instance;
  }

  public boolean importTargetIsStdIn() {
    return this.importFromStdIn;
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
