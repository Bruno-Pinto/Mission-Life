package user_interface.controllers;

import core.Life;
import core.LifeCanvas;
import core.LifeLogger;
import core.ViewLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class GameController implements Controller, LifeListener {
    private final LifeLogger log;
    private ViewLoader viewLoader;
    private Life life;
    private double scale = 1.0;
    private double maxScale = 100.0;
    private double minScale = 0.0;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private LifeCanvas lifeCanvas;
    @FXML
    private Pane mapPane;
    @FXML
    Pane mapFocus;

    public GameController() {
        log = new LifeLogger(GameController.class);
    }


    public void initialize(ViewLoader viewLoader, Life life) {
        log.logInitialize(GameController.class);
        this.viewLoader = viewLoader;
        this.life = life;
        lifeCanvas = new LifeCanvas();
        lifeCanvas.initialize(life.getState().createSettingState());
        scrollPane.setContent(lifeCanvas);
        calculateScales();
        initializeResizeListeners();
        onWindowResize();
        onMapMove();
        log.logFinishedInitialize(GameController.class);
    }

    /**
     * Calculates the maximum and minimum scales for the canvas based on the current size of the canvas and scroll pane.
     */
    private void calculateScales() {
        double maxHScale = 5000.0 / lifeCanvas.getWidth();
        double maxVScale = 5000.0 / lifeCanvas.getHeight();
        maxScale = Math.min(maxHScale, maxVScale);
        double minHScale = scrollPane.getWidth() / lifeCanvas.getWidth();
        double minVScale = scrollPane.getHeight() / lifeCanvas.getHeight();
        minScale = Math.max(minHScale, minVScale);
    }

    /**
     * Initializes the resize listeners for the canvas and scroll pane.
     */
    private void initializeResizeListeners() {
        anchorPane.widthProperty().addListener((_, _, _) -> onWindowResize());
        anchorPane.heightProperty().addListener((_, _, _) -> onWindowResize());
        scrollPane.viewportBoundsProperty().addListener((_, _, _) -> onMapMove());
        lifeCanvas.addEventHandler(ScrollEvent.SCROLL, (ScrollEvent event) -> {
            log.finest("Scroll event: " + event.getDeltaY());
            scale += event.getDeltaY()/200;
            if (scale <= maxScale && scale >= minScale) {
                scaleLifeCanvas(event.getDeltaY()/200);
                onMapMove();
            } else {
                scale -= event.getDeltaY()/200;
                log.finer("Max scale reached");
            }
            event.consume();
        });
    }

    /**
     * Resets the canvas to its initial state.
     */
    public void reset() {
        log.finer("Resetting canvas");
        lifeCanvas.paint();
    }

    @FXML
    private void backToMenu() {
        log.finer("Back to menu");
        life.stop();
        viewLoader.loadMenu();
    }

    @Override
    public void onGridUpdate(byte[][] oldGrid, byte[][] newGrid) {
        lifeCanvas.drawFrame(oldGrid, newGrid);
    }

    private boolean movingMap = false;
    private void onMapMove() {
        if (movingMap) {
            log.finest("Already moving map focus");
            return;
        }
        movingMap = true;
        debounce(()-> {
            log.finer("Moving map focus");
            double fullWidth = mapPane.getWidth();
            double fullHeight = mapPane.getHeight();
            double vRatio = lifeCanvas.getHeight() / scrollPane.getHeight();
            double hRatio = lifeCanvas.getWidth() / scrollPane.getWidth();
            log.finest("vRatio: " + vRatio + " hRatio: " + hRatio);
            mapFocus.setMinWidth(scrollPane.getWidth()/4/hRatio);
            mapFocus.setPrefWidth(scrollPane.getWidth()/4/hRatio);
            mapFocus.setMaxWidth(scrollPane.getWidth()/4/hRatio);
            mapFocus.setMinHeight(scrollPane.getHeight()/4/vRatio);
            mapFocus.setPrefHeight(scrollPane.getHeight()/4/vRatio);
            mapFocus.setMaxHeight(scrollPane.getHeight()/4/vRatio);

            double vBounds = -lifeCanvas.getWidth()/scrollPane.getViewportBounds().getMinX();
            double hBounds = -lifeCanvas.getHeight()/scrollPane.getViewportBounds().getMinY();
            mapFocus.setLayoutX(scrollPane.getWidth()/4/vBounds);
            mapFocus.setLayoutY(scrollPane.getHeight()/4/hBounds);
            movingMap = false;
            log.finest("MapFocus size: " + mapFocus.getWidth() + "x" + mapFocus.getHeight());
            log.finest("X: " + mapFocus.getLayoutX() + " Y: " + mapFocus.getLayoutY());
            }, 500
        );
    }

    private void onWindowResize() {
        debounce(() -> {
            log.finer("Resizing canvas");
            mapPane.setPrefWidth(scrollPane.getWidth()/5);
            mapPane.setMaxWidth(scrollPane.getWidth()/5);
            double ratio = lifeCanvas.getHeight()/lifeCanvas.getWidth();
            mapPane.setPrefHeight(scrollPane.getWidth()/5*ratio);
            mapPane.setMaxHeight(scrollPane.getWidth()/5*ratio);
            log.finest("MapPane size: " + mapPane.getWidth() + "x" + mapPane.getHeight());
        }, 100);
    }

    /**
     * Scales the life canvas based on the given delta Y value.
     * @param lastDeltaY the last delta Y value from the scroll event
     */
    private void scaleLifeCanvas(double lastDeltaY) {
        log.finer("Scaling canvas to: " + scale);
        log.finest("Viewport bounds before: " + scrollPane.getHvalue() + "x" + scrollPane.getVvalue());
        double hValue = scrollPane.getHvalue();
        double vValue = scrollPane.getVvalue();
        boolean success = lifeCanvas.rescale(scale);
        if (!success) {
            log.warn("Canvas scale failed");
            maxScale = scale - lastDeltaY/200;
            scale = maxScale;
        } else {
            scrollPane.setHvalue(hValue);
            scrollPane.setVvalue(vValue);
            log.finest("Viewport bounds after: " + scrollPane.getHvalue() + "x" + scrollPane.getVvalue());
        }
        log.finer("Finished scaling canvas");
    }

    /**
     * Debounces the given action for the specified delay in milliseconds.
     * @param action the action to be executed
     * @param delayMillis the delay in milliseconds
     */
    private void debounce(Runnable action, long delayMillis) {
        Thread.ofVirtual().start( () -> {
            try {
                Thread.sleep(delayMillis);
                Platform.runLater(action);
            } catch (InterruptedException e) {
                log.error("Thread interrupted while executing " + action.toString(), e);
                Thread.currentThread().interrupt();
            }
        });
    }
}
