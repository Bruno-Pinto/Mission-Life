package core;

import user_interface.ConsoleInterface;
import user_interface.UserInterface;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Life {

    private static final Random random = new Random();

    private final int rows = 260; // 15x36 for Glider
    private final int cols = 480;
    private final int cellSize = 4; // width of square
    private final int gap = 1; // gap between squares
    private final int uSize = cellSize + gap;
    private final long delay = 10;
    private byte[][] grid1 = new byte[rows][cols];
    private byte[][] grid2 = new byte[rows][cols];

    private final LifeLogger log = new LifeLogger(Life.class);
    private final UserInterface userInterface;
    private final GamePanel panel;
    private final GameFrame frame;

    private boolean running = true;
    private GameMode gameMode;

    public Life() {
        panel = new GamePanel(cols, rows, cellSize, gap);
        frame = new GameFrame(this, panel);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    running = false;
                    frame.dispose();
                }
            }
        });
        panel.setG2D();
        userInterface = new ConsoleInterface();
    }

    public void initialize() {
        frame.setVisible(true);
//        gameMode = userInterface.getGameMode();
        gameMode = GameMode.RANDOM;
        if (gameMode == GameMode.RANDOM) {
            randomFill(grid1);
        } else {
            //TODO: add logic to get Patterns from UI
        }
        run();

    }

    /**
     * Stops life
     */
    public void stop() {
        running = false;
        frame.dispose();
    }

    //runs the game/calculates what the next step will be
    private void run() {
        if (!running) { return;}
        int sum = 0;
        for (int i = 0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                sum = countNeighbours(grid1, i, j);
                if(sum==3 || (sum==2 && grid1[i][j]==1)) {
                    grid2[i][j] = 1;
                } else {
                    grid2[i][j] = 0;
                }
            }
        }
        drawFrame(grid2);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
            running = false;
        }
        grid1 = grid2;
        grid2 = new byte[rows][cols];
        run();
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
    private void drawFrame(byte[][] grid) {
        for (int i = 0; i< rows; i++) {
            for (int j=0; j<cols; j++) {
                panel.paintSquare((uSize)*j, (uSize)*i, grid[i][j]);
            }
        }
    }

    private boolean withinBounds(byte[][] grid, int x, int y) {
        return x >= 0 && y >= 0 && x < grid.length && y < grid[0].length;
    }

    private void fillGliderGun(byte[][] grid){
        String gun = "000000000000000000000000100000000000"
                   + "000000000000000000000010100000000000"
                   + "000000000000110000001100000000000011"
                   + "000000000001000100001100000000000011"
                   + "110000000010000010001100000000000000"
                   + "110000000010001011000010100000000000"
                   + "000000000010000010000000100000000000"
                   + "000000000001000100000000000000000000"
                   + "000000000000110000000000000000000000";
        int index=0;
        for (int i=0; i<9; i++) {
            for (int j=0; j<36; j++) {
                grid[i][j] = (byte) (gun.charAt(index)-48);
                index++;
            }
        }
    }

    private void randomFill(byte[][] grid){
        for (int i = 0; i< rows; i++) {
            for (int j=0; j<cols; j++) {
                grid[i][j] = (byte) random.nextInt(2);
            }
        }
    }
}