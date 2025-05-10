package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Life {

    private final int rows = 40; // 15x36 for Glider
    private final int cols = 60;
    private final int cellWidth = 16; // width of square
    private final int gap = 1; // gap between squares
    private final int uSize = cellWidth + gap;
    private final GamePanel panel;
    private final GameFrame frame;
    private int[][] grid1 = new int[rows][cols];
    private int[][] grid2 = new int[rows][cols];
    private int flip = 0;
    private final long delay = 100;
    private static final boolean FILL_GUN = true;
    private static final Timer timer = new Timer();
    private static final Random random = new Random();

    public Life() {
        panel = new GamePanel(cols, rows, uSize, gap);
        frame = new GameFrame(panel);
    }

    public void start() {
        frame.setVisible(true);
        if (FILL_GUN) {
            fillGliderGun(grid1);
        } else {
            randomFill(grid1);
        }

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }

        startGame(grid1);
    }

    //starts the game and paints the first frame
    void startGame(int[][] grid) {
        for (int i = 0; i< rows; i++) {
            for (int j=0; j<cols; j++) {
                if(grid[i][j] == 1) {
                    paintSquare(panel, (uSize)*j, (uSize)*i, cellWidth, 1);
                }
            }
        }

        //starts a repeating task
        timer.scheduleAtFixedRate(new TimerTask(){
            public void run() {
                if(flip==0) {
                    Life.this.run(grid1, grid2);
                    flip=1;
                }
                else if(flip==1) {
                    Life.this.run(grid2, grid1);
                    flip=0;
                }
            }
        }, delay, delay);
//			run(grid1, grid2);
    }

    //runs the game/calculates what the next step will be
    private void run(int[][] gridOld, int[][] gridNew) {
        int sum = 0;
        for (int i = 0; i< rows; i++) {
            for (int j=0; j<cols; j++) {
                sum = countNeighbours(gridOld, i, j);
                if(sum==3) {
                    gridNew[i][j] = 1;
                }
                else if(sum==2 && gridOld[i][j]==1) {
                    gridNew[i][j] = 1;
                }
                else {
                    gridNew[i][j] = 0;
                }
            }
        }
        drawFrame(gridNew);
    }

    //counts neighbours of inside fields
    int countNeighbours(int[][]grid, int i, int j) {
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
    void drawFrame(int[][] grid) {
        for (int i = 0; i< rows; i++) {
            for (int j=0; j<cols; j++) {
                paintSquare(panel, (uSize)*j, (uSize)*i, cellWidth, grid[i][j]);
            }
        }
    }

    //paint a square
    public void paintSquare(GamePanel panel, int x, int y, int w, int color) {
        Graphics g = panel.getGraphics();
        Graphics2D g2D = (Graphics2D) g;
        if (color == 1) {
            g2D.setColor(Color.black);
        } else {
            g2D.setColor(Color.white);
        }
        g2D.fillRect(x, y, w, w);
    }

    private boolean withinBounds(int[][] grid, int x, int y) {
        return x >= 0 && y >= 0 && x < grid.length && y < grid[0].length;
    }

    //fill the grid with glider gun pattern
    private void fillGliderGun(int[][] grid){
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
                grid[i][j] = gun.charAt(index)-48;
                index++;
            }
        }
    }

    //fill the grid with random 0 or 1
    private void randomFill(int[][] grid){
        for (int i = 0; i< rows; i++) {
            for (int j=0; j<cols; j++) {
                grid[i][j] = random.nextInt(2);
            }
        }
    }
}