package patterns;

import core.LifeLogger;
import exceptions.InvalidFileFormatException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

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
        File directory = new File(patternType.getPath());
        if (!assertValidDirectory(directory)) {
            log.warn("Path " + patternType.getPath() + " is not a directory");
            return Collections.emptyList();
        }
        ArrayList<Pattern> patterns = new ArrayList<>();
        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            try {
                Pattern pattern = createPattern(patternType, file);
                patterns.add(pattern);
            } catch (Exception e) {
                log.error("Failed to load file", e);
            }
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
        return new Pattern(file.getName(), patternType, grid);
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
            byte[][] byteGrid = new byte[rows][columns];
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    byteGrid[row][column] = scanner.nextByte();
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