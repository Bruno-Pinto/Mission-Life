package patterns;

/**
 * Enum to define the different pattern types.
 */
public enum PatternType {

    STILL_LIFE("Still Life" , "patterns/still_life", "Still patterns that do not change."),
    OSCILLATOR("Oscillator" , "patterns/oscillator", "Patterns that oscillate between two or more states."),
    SPACESHIP("Spaceship" , "patterns/spaceship", "Patterns that move across the grid."),
    GENERATOR("Generator" , "patterns/generator", "Patterns that generate other patterns.");

    /**
     * The name of the pattern type.
     */
    private final String name;
    /**
     * The path to the pattern directory.
     */
    private final String path;
    /**
     * The description of the pattern type.
     */
    private final String description;

    /**
     * Constructor for the PatternType enum.
     *
     * @param name the name of the pattern type
     * @param path the path to the pattern directory
     */
    PatternType(String name, String path, String description) {
        this.name = name;
        this.path = path;
        this.description = description;
    }

    /**
     * Returns the name of the pattern type.
     *
     * @return the name of the pattern type
     */
    public String getName() {
        return name;
    }
    /**
     * Returns the path to the pattern directory.
     *
     * @return the path to the pattern directory
     */
    public String getPath() {
        return path;
    }
    /**
     * Returns the description of the pattern type.
     *
     * @return the description of the pattern type
     */
    public String getDescription() {
        return description;
    }
}
