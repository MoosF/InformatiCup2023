package service;

/**
 * This class defines supported file types for input and output files. The file type that is
 * currently used can be set in the {@link Settings} of the program.
 *
 * @author Fabian Moos
 * @see Input
 * @see InputOutputHandle
 * @see Settings
 */
public enum FileType {
  JSON,
  XML,
  TOML,
}
