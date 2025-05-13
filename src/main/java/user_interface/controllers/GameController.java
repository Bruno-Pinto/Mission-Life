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
        anchorPane.widthProperty().addListener((_, _, _) -> onWindowResize());
        anchorPane.heightProperty().addListener((_, _, _) -> onWindowResize());
        lifeCanvas = new LifeCanvas();
        scrollPane.setContent(lifeCanvas);
        scrollPane.viewportBoundsProperty().addListener((_, _, _) -> onMapMove());
        lifeCanvas.addEventHandler(ScrollEvent.SCROLL, (ScrollEvent event) -> {
            log.finest("Scroll event: " + event.getDeltaY());
            scale += event.getDeltaY()/200;
            event.consume();
            if (scale <= maxScale) {
                scaleLifeCanvas(event.getDeltaY()/200);
                onMapMove();
            } else {
                log.finer("Max scale reached");
            }
        });
        this.viewLoader = viewLoader;
        this.life = life;
        lifeCanvas.initialize(life.getState().createSettingState());
        onWindowResize();
        onMapMove();
        log.logFinishedInitialize(GameController.class);
    }

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
    @FXML
    private void onMapMove() {
        if (movingMap) {
            log.finest("Already moving map");
            return;
        }
        movingMap = true;
        debounce(()-> {
            log.finer("Moving map");
            double vRatio = lifeCanvas.getHeight()*scale / scrollPane.getHeight();
            double hRatio = lifeCanvas.getWidth()*scale / scrollPane.getWidth();
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

    @FXML
    private void onWindowResize() {
        debounce(() -> {
            log.finest("Resizing canvas");
            mapPane.setPrefWidth(scrollPane.getWidth()/4);
            mapPane.setMaxWidth(scrollPane.getWidth()/4);
            mapPane.setPrefHeight(scrollPane.getHeight()/4);
            mapPane.setMaxHeight(scrollPane.getHeight()/4);
            log.finest("MapPane size: " + mapPane.getWidth() + "x" + mapPane.getHeight());
        }, 100);
    }

    private void scaleLifeCanvas(double lastDeltaY) {
        log.finer("Scaling canvas to: " + scale);
        boolean success = lifeCanvas.rescale(scale);
        if (!success) {
            log.warn("Canvas scale failed");
            maxScale = scale - lastDeltaY/200;
            scale = maxScale;
        }
        log.finer("Finished scaling canvas");
    }

    private void debounce(Runnable action, long delayMillis) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(delayMillis);
                Platform.runLater(action);
            } catch (InterruptedException e) {
                log.error("Thread interrupted", e);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
