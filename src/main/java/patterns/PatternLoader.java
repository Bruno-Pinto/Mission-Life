package patterns;

import core.LifeLogger;
import exceptions.InvalidFileFormatException;

import java.io.File;
import java.util.*;

public class PatternLoader {
    private static final LifeLogger log = new LifeLogger(PatternLoader.class);

    /**
     * Loads all possible patterns.
     * @return A list of all valid patterns. Can be empty.
     */
    public static List<Pattern> loadAllPatterns() {
        log.info("Loading all patterns...");
        ArrayList<Pattern> patterns = new ArrayList<>();
        for (PatternType patternType : PatternType.values()) {
            patterns.addAll(loadPatternsWithType(patternType));
        }
        return patterns;
    }

    /**
     * Loads all patterny from a specific type.
     * @param patternType The pattern type
     * @return A list of all patterns with the patternType. Can be empty.
     */
    public static List<Pattern> loadPatternsWithType(PatternType patternType) {
        log.info("Loading all " + patternType.getName() + " patterns...");
        ArrayList<Pattern> patterns = new ArrayList<>();

        try {
            // Access the directory as a resource
            String path = patternType.getPath();
            var resource = PatternLoader.class.getResource(path);

            if (resource == null) {
                log.warn("Resource path " + path + " not found.");
                return Collections.emptyList();
            }

            // Load files within the resource path
            File[] files = new File(Objects.requireNonNull(resource.toURI())).listFiles();
            if (files == null) return patterns;

            for (File file : files) {
                try {
                    Pattern pattern = createPattern(patternType, file);
                    patterns.add(pattern);
                } catch (Exception e) {
                    log.error("Failed to load pattern from file: " + file.getName(), e);
                }
            }
        } catch (Exception e) {
            log.error("Error accessing resource path: " + patternType.getPath(), e);
        }
        return patterns;
    }

    /**
     * Loads and creates a pattern from a file
     * @param patternType The pattern type
     * @param file The file to read from
     * @return The pattern
     */
    private static Pattern createPattern(PatternType patternType, File file) throws InvalidFileFormatException, IllegalArgumentException{
        byte[][] grid = readGrid(file);
        if (grid.length == 0) {
            throw new IllegalArgumentException("Grid is empty for file: " + file.getName());
        }
        return new Pattern(file.getName().substring(0, file.getName().length()-4), patternType, grid);
    }

    /**
     * Reads a grid from a file.
     * The grid will be of length == 0 if there was an issue reading the file.
     * @param file The file containing the grid
     * @return The read grid
     */
    private static byte[][] readGrid(File file) throws InvalidFileFormatException {
        try (Scanner scanner = new Scanner(file)) {
            int rows = scanner.nextInt();
            int columns = scanner.nextInt();
            log.finer("Reading " + rows + " rows and " + columns + " columns...");
            scanner.nextLine();
            byte[][] byteGrid = new byte[rows][columns];
            for (int row = 0; row < rows; row++) {
                String line = scanner.nextLine();
                for (int column = 0; column < columns; column++) {
                    if (column >= line.length()) { byteGrid[row][column] = 0; }
                    else { byteGrid[row][column] = (byte) ((byte) line.charAt(column)-48); }
                }
            }
            return byteGrid;
        } catch (Exception e) {
            throw new InvalidFileFormatException("Invalid file format", file.getPath(), e);
        }
    }

    /**
     * Checks if a directory is valid
     * @param directory The directory to check
     * @return true if valid, false if invalid
     */
    private static boolean assertValidDirectory(File directory) {
        return directory.isDirectory() && directory.listFiles() != null;
    }
}