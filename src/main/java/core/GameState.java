package core;

/**
 * Class to represent the state of the game.
 */
public class GameState {
    /**
     * The previous {@link GameState} in the linked list.
     */
    private GameState previous;
    /**
     * The next {@link GameState} in the linked list.
     */
    private GameState next;
    private int rows;
    private int cols;
    private long generation;
    private double cellSize;
    private double gap;
    private double unitSize; // size of cellsize + gap
    private double scale = 1.0;
    private long delay;
    private byte[][] grid;

    /**
     * Pure {@link GameState} for tracking UI changes. Doesn't require a grid.
     */
    public GameState(int rows, int cols, double cellSize, double gap, long delay) {
        this.rows = rows;
        this.cols = cols;
        this.cellSize = cellSize;
        this.gap = gap;
        this.unitSize = cellSize + gap;
        this.delay = delay;
    }

    /**
     * {@link GameState} to track the progress of the game.
     * @param rows The amount of rows
     * @param cols The amount of columns
     * @param cellSize The cell size
     * @param gap The gap between cells
     * @param delay The delay of the engine
     * @param grid The grid
     */
    public GameState(int rows, int cols, double cellSize, double gap, long delay, long generation, byte[][] grid) {
        this.rows = rows;
        this.cols = cols;
        this.cellSize = cellSize;
        this.gap = gap;
        this.unitSize = cellSize + gap;
        this.delay = delay;
        this.generation = generation;
        this.grid = grid;
    }

    public GameState getPrevious() {
        return previous;
    }
    public void setPrevious(GameState previous) {
        this.previous = previous;
    }
    public GameState getNext() {
        return next;
    }
    public void setNext(GameState next) {
        this.next = next;
    }
    public int getRows() {
        return rows;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }
    public int getCols() {
        return cols;
    }
    public void setCols(int cols) {
        this.cols = cols;
    }
    public double getCellSize() {
        return cellSize * scale;
    }
    public void setCellSize(double cellSize) {
        this.cellSize = cellSize;
    }
    public double getGap() {
        return gap * scale;
    }
    public void setGap(double gap) {
        this.gap = gap;
    }
    public double getUnitSize() {
        return unitSize * scale;
    }
    public void setUnitSize(double unitSize) {
        this.unitSize = unitSize;
    }
    public long getDelay() {
        return delay;
    }
    public void setDelay(long delay) {
        this.delay = delay;
    }
    public byte[][] getGrid() {
        return grid;
    }
    public void setGrid(byte[][] grid) {
        this.grid = grid;
    }
    public void setScale(double scale) { this.scale = scale; }

    /**
     * Creates the next {@link GameState} from the current state. Doesn't modify current {@link GameState}.
     * @param nextGrid The grid for the next {@link GameState}
     * @return The next {@link GameState}
     */
    public GameState createNextState(byte[][] nextGrid) {
        return new GameState(rows, cols, cellSize, gap, delay, generation+1, nextGrid);
    }

    /**
     * Creates a default {@link GameState} for the game.
     * @return The default {@link GameState}
     */
    public static GameState getDefaultState() {
        return new GameState(1000,1000,4,1, 100);
    }

    /**
     * Creates a setting {@link GameState} for the game from the current {@link GameState}.
     * @return The setting {@link GameState}
     */
    public GameState createSettingState() {
        return new GameState(rows, cols, cellSize, gap, delay);
    }

    public GameState copy() {
        GameState newState = new GameState(rows, cols, cellSize, gap, delay, generation, grid);
        newState.setScale(scale);
        return newState;
    }
}
