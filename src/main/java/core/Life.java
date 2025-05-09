package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Life {

    static int rows = 40; // 15x36 for Glider
    static int cols = 60;
    static int w = 15;
    static int gap = 2;
    static int uSize = w + gap;
    static int panelWidth = cols*uSize-gap;
    static int panelHeight = rows*uSize-gap;
    static GamePanel panel = new GamePanel(panelWidth, panelHeight);
    static GameFrame frame = new GameFrame();
    static int[][] grid1 = new int[rows][cols];
    static int[][] grid2 = new int[rows][cols];
    static int flip = 0;
    static Timer timer = new Timer();
    static long delay = 150;

    public static void main(String[] args) throws InterruptedException {

        grid1 = randomFill(grid1);
//		grid1 = fillGliderGun(grid1);

//		print(grid1);
//		System.out.println(panelWidth + " " + panelHeight);

        Thread.sleep(10);

        startGame(grid1);
    }

    //starts the game and paints the first frame
    static void startGame(int[][] grid) {
        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                if(grid[i][j] == 1) {
                    paintSquare(panel, (uSize)*j, (uSize)*i, w);
                }
            }
        }

        //starts a repeating task
        timer.scheduleAtFixedRate(new TimerTask(){
            public void run() {
                if(flip==0) {
                    Life.run(grid1, grid2);
                    flip=1;
                }
                else if(flip==1) {
                    Life.run(grid2, grid1);
                    flip=0;
                }
            }
        }, delay, delay);
//			run(grid1, grid2);
    }

    //runs the game/calculates what the next step will be
    static void run(int[][] gridOld, int[][] gridNew) {
        int sum = 0;
//		System.out.println();
        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                if(i==0 || i==rows-1 || j==0 || j==cols-1) {
                    sum = neighboursBorder(gridOld, i, j);
                }
                else {
                    sum = neighbours(gridOld, i ,j);
                }
                if(sum==3) {
                    gridNew[i][j] = 1;
                }
                else if(sum==2 && gridOld[i][j]==1) {
                    gridNew[i][j] = 1;
                }
                else {
                    gridNew[i][j] = 0;
                }
//				System.out.print(sum);
            }
//			System.out.println();
        }
//		System.out.println();
//		print(gridNew);
        drawFrame(gridNew);
    }

    //counts neighbours of inside fields
    static int neighbours(int[][]grid, int i, int j) {
        int sum=0;
        sum += grid[i-1][j-1];
        sum += grid[i-1][j];
        sum += grid[i-1][j+1];
        sum += grid[i][j-1];
        sum += grid[i][j+1];
        sum += grid[i+1][j-1];
        sum += grid[i+1][j];
        sum += grid[i+1][j+1];
        return sum;
    }

    //counts neighbours of border fields
    static int neighboursBorder(int[][]grid, int i, int j) {
        int sum=0;
        if(i>0) {
            sum += grid[i-1][j];
            if(j>0) {
                sum += grid[i-1][j-1];
            }
            if(j<cols-1) {
                sum += grid[i-1][j+1];
            }
        }
        if(i<rows-1) {
            sum += grid[i+1][j];
            if(j>0) {
                sum += grid[i+1][j-1];
            }
            if(j<cols-1) {
                sum += grid[i+1][j+1];
            }
        }
        if(j>0) {
            sum += grid[i][j-1];
        }
        if(j<cols-1) {
            sum += grid[i][j+1];
        }
        return sum;
    }

    //draws the frame
    static void drawFrame(int[][] grid) {
        panel.repaint();
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                if(grid[i][j] == 1) {
                    paintSquare(panel, (uSize)*j, (uSize)*i, w);
                }
            }
        }
    }

    //paint a square
    public static void paintSquare(GamePanel panel, int x, int y, int w) {
        Graphics g = panel.getGraphics();
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.black);
        g2D.fillRect(x, y, w, w);
    }

    //print the grid in console
    static void print(int[][] grid) {
        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    //fill the grid with glider gun pattern
    static int[][] fillGliderGun(int[][] grid){
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
        return grid;
    }

    //fill the grid with random 0 or 1
    static int[][] randomFill(int[][] grid){
        Random random = new Random();
        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                grid[i][j] = random.nextInt(2);
            }
        }
        return grid;
    }
}