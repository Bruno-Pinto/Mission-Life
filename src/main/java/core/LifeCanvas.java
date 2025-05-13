package core;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class LifeCanvas extends Canvas {
    private GameState gameState;
    private final Paint backgroundColor = Paint.valueOf("lightgray");
    private final Paint gridColor = Paint.valueOf("lightgray");
    private final Paint squareColor = Paint.valueOf("darkgray");
    private final LifeLogger log;

    public LifeCanvas() {
        super();
        log = new LifeLogger(LifeCanvas.class);
    }

    public void initialize(GameState gameState) {
        log.logInitialize(LifeCanvas.class);
        this.gameState = gameState;
        setWidth(gameState.getCols() * (gameState.getCellSize() + gameState.getGap()));
        setHeight(gameState.getRows() * (gameState.getCellSize() + gameState.getGap()));
        log.logInitialize(LifeCanvas.class);
    }

    public void updateGameState(GameState gameState) { this.gameState = gameState; }

    public boolean rescale(double scale) {
        log.finer("Rescaling canvas");
        gameState.setScale(scale);
        double width = gameState.getCols() * (gameState.getCellSize() + gameState.getGap());
        double height = gameState.getRows() * (gameState.getCellSize() + gameState.getGap());

        if (width <= 5000 && height <= 5000) {
            setWidth(width);
            setHeight(height);
            paint();
            log.finer("Rescaled canvas to: " + getWidth() + "x" + getHeight());
            return true;
        } else {
            log.error("Rescale resulted in invalid dimensions: " + width + "x" + height);
            return false;
        }
    }

    /**
     * Draws the grid on the {@link LifeCanvas}. Uses the {@link GameState} to calculate sizes.
     */
    public void paint() {
        log.finer("Painting canvas");
        log.finest("Painting with Canvas size: " + getWidth() + "x" + getHeight());
        GraphicsContext graphics2D = getGraphicsContext2D();
        double gap = gameState.getGap();
        double uSize = gameState.getCellSize() + gap;
        int cols = gameState.getCols();
        int rows = gameState.getRows();
        graphics2D.setFill(backgroundColor);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
        graphics2D.setLineWidth(gap);
        graphics2D.setStroke(gridColor);
        if (gap!=0) {
            for (int i = 1; i<cols; i++) {
                graphics2D.strokeLine(i*uSize, 0, i*uSize,getHeight());
            }
            for (int i = 1; i<rows; i++) {
                graphics2D.strokeLine(0, i*uSize, getWidth(), i*uSize);
            }
        }
        log.finer("Finished painting canvas");
    }

    /**
     * Draws the current game frame by comparing two grids and only drawing the diff.
     * @param oldGrid The old grid to compare to
     * @param newGrid The new grid comparing with
     */
    public void drawFrame(byte[][] oldGrid, byte[][] newGrid) {
        log.finest("Drawing frame");
        double cellSize = gameState.getCellSize();
        double gap = gameState.getGap();
        double unitSize = gameState.getUnitSize();
        for (int row = 0; row < newGrid.length; row++) {
            for (int column = 0; column < newGrid[row].length; column++) {
                if (oldGrid[row][column] != newGrid[row][column]) {
                    paintSquare(column*unitSize + gap/2, row*unitSize + gap/2, newGrid[row][column], cellSize);
                }
            }
        }
        log.finest("Finished drawing frame");
    }

    /**
     * Paints out a single square. Cords are for top-left of square.
     * @param x The x coordinate
     * @param y The y coordinate
     * @param state The state to paint the cell in
     * @param cellSize The size of the square
     */
    private void paintSquare( double x, double y, int state, double cellSize) {
        GraphicsContext g2D = getGraphicsContext2D();
        if (state == 1) {
            g2D.setFill(squareColor);
        } else {
            g2D.setFill(backgroundColor);
        }
        g2D.fillRect(x, y, cellSize, cellSize);
    }
}
