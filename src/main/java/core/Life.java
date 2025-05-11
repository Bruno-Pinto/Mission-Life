package core;

import patterns.Pattern;
import user_interface.ConsoleInterface;
import user_interface.UserInterface;

import java.awt.*;
import java.util.Random;

public class Life {

    private static final Random random = new Random();
    private static final long START_TIME = System.currentTimeMillis();

    private static final double SCALE = 1;
    private final int rows = (int) (270* SCALE); // 15x36 for Glider
    private final int cols = (int) (500* SCALE);
    private final int cellSize = (int) (4/ SCALE); // width of square
    private final int gap = 1; // gap between squares
    private final int uSize = cellSize + gap;
    private final long delay = 50;
    private byte[][] grid1 = new byte[rows][cols];
    private byte[][] grid2 = new byte[rows][cols];

    private final LifeLogger log = new LifeLogger(Life.class);
    private final UserInterface userInterface;
    private final GamePanel panel;
    private final GameFrame frame;

    private long lastGenerationTime = START_TIME;
    private long generation;
    private boolean running = true;
    private boolean restart = false;
    private GameMode gameMode;

    public Life() {
        panel = new GamePanel(cols, rows, cellSize, gap);
        frame = new GameFrame(this, panel);
        panel.setG2D();
        userInterface = new ConsoleInterface();
    }

    public void initialize() {
        log.info("Initializing Life");
        generation = 0;
        frame.setVisible(true);
//        gameMode = userInterface.getGameMode();
        gameMode = GameMode.RANDOM;
        if (gameMode == GameMode.RANDOM) {
            randomFill(grid1);
//            fillPatternCenter(PatternLoader.loadPatternsWithType(PatternType.OSCILLATOR).get(3));
//            fillPatternCenter(PatternLoader.loadPatternsWithType(PatternType.OSCILLATOR).getFirst());
//            fillPatternCenterRight(PatternLoader.loadPatternsWithType(PatternType.SPACESHIP).get(3));
//            fillPatternCenter(PatternLoader.loadPatternsWithType(PatternType.GENERATOR).get(2));

        } else {
        }
        running = true;
        log.info("Finished Initializing Life, starting game");
        run();
    }

    /**
     * Stops life
     */
    public void stop() {
        running = false;
        frame.dispose();
    }

    public void restart() {
        grid1 = new byte[rows][cols];
        grid2 = new byte[rows][cols];
        panel.repaint();
        restart = true;
        running = false;
    }

    //runs the game/calculates what the next step will be
    private void run() {
        while (running) {
            int sum = 0;
            for (int i = 0; i<rows; i++) {
                for (int j=0; j<cols; j++) {
                    sum = countNeighbours(grid1, i, j);
                    if(sum==3 || (sum==2 && grid1[i][j]==1)) {
                        grid2[i][j] = 1;
                    }
                }
            }
            drawFrame();
            generation++;
            if (generation % 100 == 0) {
                log.finer("Generation " + generation + " took " + (System.currentTimeMillis() - lastGenerationTime) + " milliseconds");
                lastGenerationTime = System.currentTimeMillis();
            }
            if (generation % 5000 == 0) {
                restart();
            }
            grid1 = grid2;
            grid2 = new byte[rows][cols];
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                log.error("Thread interrupted", e);
                running = false;
            }
        }
        if (restart) {
            restart = false;
            running = true;
            log.finer("Restarting Life");
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

    //draws the frame
    private void drawFrame() {
        double startTime = System.currentTimeMillis();
        Graphics2D g2d = panel.getG2D();
        synchronized (g2d) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (grid1[i][j] != grid2[i][j]) {
                        panel.paintSquare((uSize) * j, (uSize) * i, grid2[i][j]);
                    }
                }
            }
        }
        double elapsedTime = System.currentTimeMillis() - startTime;
        log.finest("Time to draw frame " + generation + " in milliseconds: " + elapsedTime);
    }

    private boolean withinBounds(byte[][] grid, int x, int y) {
        return x >= 0 && y >= 0 && x < grid.length && y < grid[0].length;
    }

    private void fillPatternTopLeft(Pattern pattern) {
        log.fine("Filling pattern " + pattern.getName());
        byte[][] grid = pattern.getGrid();
        for (int i=0; i<grid.length; i++) {
            System.arraycopy(grid[i], 0, grid1[i], 0, grid[i].length);
        }
    }

    private void fillPatternCenterLeft(Pattern pattern) {
        log.fine("Filling pattern " + pattern.getName());
        byte[][] grid = pattern.getGrid();
        int startX = (rows-grid.length)/2;
        for (int i=0; i<grid.length; i++) {
            System.arraycopy(grid[i], 0, grid1[i+startX], 10, grid[i].length);
        }
    }

    private void fillPatternCenterRight(Pattern pattern) {
        log.fine("Filling pattern " + pattern.getName());
        byte[][] grid = pattern.getGrid();
        int startX = (rows-grid.length)/2;
        int startY = grid1[0].length-grid[0].length-10;
        for (int i=0; i<grid.length; i++) {
            System.arraycopy(grid[i], 0, grid1[i+startX], startY, grid[i].length);
        }
    }

    private void fillPatternCenter(Pattern pattern) {
        log.fine("Filling pattern " + pattern.getName());
        byte[][] grid = pattern.getGrid();
        int startX = (rows-grid.length)/2;
        int startY = (cols-grid[0].length)/2;
        for (int i=0; i<grid.length; i++) {
            System.arraycopy(grid[i], 0, grid1[i+startX], startY, grid[i].length);
        }
    }

    private void randomFill(byte[][] grid){
        for (int i = 0; i< rows; i++) {
            for (int j=0; j<cols; j++) {
                grid[i][j] = (byte) (random.nextInt(2)%2);
            }
        }
    }
}