package core;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import user_interface.controllers.Controller;
import user_interface.controllers.GameController;
import user_interface.controllers.MenuController;
import java.net.URL;
import java.util.List;

public class ViewLoader {
    private final LifeLogger log;
    private Stage stage;
    private GameController gameController;
    private Life life;

    public ViewLoader(Stage stage) {
        log = new LifeLogger(ViewLoader.class);
        log.logInitialize(ViewLoader.class);
        initializeStage(stage);
        log.logFinishedInitialize(ViewLoader.class);
    }

    public void close() {
        log.info("Closing application");
        if (life != null) { life.stop(); }
        stage.close();
    }

    public void loadMenu() {
        MenuController menuController = (MenuController) loadScene("/views/MenuView.fxml");
        assert menuController != null;
        menuController.initialize(this);
    }

    public void loadGame() {
        gameController = (GameController) loadScene("/views/GameView.fxml");
        life = new Life(GameState.getDefaultState());
        assert gameController != null;
        gameController.initialize(this, life);
        new Thread(() -> {
            life.addListener(gameController);
            life.initialize();
        }).start();
    }

    private void initializeStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(_ -> close());
        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                close();
            } else if (event.getCode() == KeyCode.R && life != null) {
                gameController.reset();
                life.restart();
            }
        });
        Screen primary = Screen.getPrimary();
        Screen screen = primary;
        for (Screen s : Screen.getScreens()) {
            if (!s.equals(primary)) {
                screen = s;
                break;
            }
        }
        Rectangle2D bounds = screen.getBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
    }

    private Controller loadScene(String fxmlPath) {
        try {
            URL controllerPath = getClass().getResource(fxmlPath);
            log.info("Searching for FXML file: " + controllerPath);
            FXMLLoader loader = new FXMLLoader(controllerPath);
            Pane rootNode = loader.load();
            Controller controller = loader.getController();
            Scene scene = new Scene(rootNode);
            stage.setScene(scene);
            return controller;
        } catch (Exception e) {
            log.error("Failed to load FXML at: " + fxmlPath, e);
        }
        return null;
    }
}
