package core;

import patterns.Pattern;
import patterns.PatternLoader;
import patterns.PatternType;
import user_interface.controllers.LifeListener;

import java.util.ArrayList;
import java.util.Random;

public class Life {

    private static final Random random = new Random();
    private static final long START_TIME = System.currentTimeMillis();

    private byte[][] oldGrid;
    private byte[][] newGrid;

    private final LifeLogger log = new LifeLogger(Life.class);
    private final ArrayList<LifeListener> listeners = new ArrayList<>();

    private GameState state;
    private long lastGenerationTime = START_TIME;
    private long generation;
    private boolean running = true;
    private boolean restart = false;

    public Life(GameState state) {
        this.state = state;
    }

    public void initialize() {
        log.info("Initializing Life");
        log.fine("Grid: " + state.getRows() + "x" + state.getCols() + " cell size: " + state.getCellSize() + " gap: " + state.getGap());
        this.oldGrid = new byte[this.state.getRows()][this.state.getCols()];
        this.newGrid = new byte[this.state.getRows()][this.state.getCols()];
        generation = 0;
//        randomFill(grid1);
//        fillPatternCenter(PatternLoader.loadPatternsWithType(PatternType.OSCILLATOR).get(3));
//        fillPatternCenter(PatternLoader.loadPatternsWithType(PatternType.OSCILLATOR).getFirst());
        fillPatternCenter(PatternLoader.loadPatternsWithType(PatternType.SPACESHIP).getLast());
//        fillPatternCenterLeft(PatternLoader.loadPatternsWithType(PatternType.GENERATOR).get(0));

        running = true;
        log.info("Finished Initializing Life, starting game");
        run();
    }

    public void addListener(LifeListener l) {
        listeners.add(l);
    }
    public GameState getState() { return state; }
    public void setState(GameState state) {
        this.state = state;
    }

    private void notifyListeners(byte[][] oldGrid, byte[][] newGrid) {
        for (LifeListener l : listeners) {
            l.onGridUpdate(oldGrid, newGrid);
        }
    }

    /**
     * Stops life
     */
    public void stop() {
        log.info("Stopping Life");
        running = false;
    }

    public void restart() {
        oldGrid = new byte[state.getRows()][state.getCols()];
        newGrid = new byte[state.getRows()][state.getCols()];
        restart = true;
        running = false;
    }

    //runs the game/calculates what the next step will be
    private void run() {
        while (running) {
            int sum = 0;
            for (int i = 0; i<state.getRows(); i++) {
                for (int j=0; j<state.getCols(); j++) {
                    sum = countNeighbours(oldGrid, i, j);
                    if(sum==3 || (sum==2 && oldGrid[i][j]==1)) {
                        newGrid[i][j] = 1;
                    }
                }
            }
            generation++;
            if (generation % 100 == 0) {
                long generationTime = System.currentTimeMillis() - lastGenerationTime;
                log.finer("Generation " + generation + " took " + generationTime + " milliseconds = " + Math.floor(100.0 * (1000.0/generationTime)));
                lastGenerationTime = System.currentTimeMillis();
            }
            notifyListeners(oldGrid, newGrid);
            oldGrid = newGrid;
            newGrid = new byte[state.getRows()][state.getCols()];
            try {
                Thread.sleep(state.getDelay());
            } catch (InterruptedException e) {
                log.error("Thread interrupted", e);
                running = false;
                Thread.currentThread().interrupt();
            }
        }
        if (restart) {
            restart = false;
            running = true;
            log.fine("Restarting Life");
            initialize();
        }
    }

    /**
     * Counts the number of neighbours of a cell
     * @param grid the grid with the cells
     * @param i the row of the cell
     * @param j the column of the cell
     * @return the number of neighbours
     */
    private int countNeighbours(byte[][]grid, int i, int j) {
        int sum=0;
        for (int x=-1; x<=1; x++) {
            for (int y=-1; y<=1; y++) {
                if ((x==0&&y==0) || !withinBounds(grid, x+i, y+j)) { continue; }
                sum += grid[x+i][y+j];
            }
        }
        return sum;
    }

    private boolean withinBounds(byte[][] grid, int x, int y) {
        return x >= 0 && y >= 0 && x < grid.length && y < grid[0].length;
    }

    private void fillPatternCenter(Pattern pattern) {
        log.fine("Filling pattern " + pattern.getName());
        byte[][] grid = pattern.getGrid();
        int startX = (oldGrid.length-grid.length)/2;
        int startY = (oldGrid[0].length-grid[0].length)/2;
        log.finer("Filling pattern at " + startX + "," + startY);
        for (int i=0; i<grid.length; i++) {
            System.arraycopy(grid[i], 0, oldGrid[i+startX], startY, grid[i].length);
        }
    }

    private void randomFill(byte[][] grid){
        for (int i = 0; i<state.getRows(); i++) {
            for (int j=0; j<state.getCols(); j++) {
                grid[i][j] = (byte) (random.nextInt(2)%2);
            }
        }
    }
}