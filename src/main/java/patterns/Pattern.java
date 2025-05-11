package patterns;

import core.LifeLogger;

public class Pattern {
    private static final LifeLogger log = new LifeLogger(Pattern.class);

    private final String name;
    private final PatternType type;
    private final byte[][] grid;
    private final int rows;
    private final int cols;

    public Pattern(String name, PatternType type, byte[][] grid) {
        log.fine("Created pattern " + name + " with type " + type);
        this.name = name;
        this.type = type;
        this.grid = grid;
        this.rows = grid.length;
        this.cols = grid[0].length;
    }

    public String getName() { return name; }
    public PatternType getType() { return type; }
    public byte[][] getGrid() { return grid; }
    public int getRows() { return rows; }
    public int getCols() { return cols; }
}
