package user_interface.controllers;

import core.LifeLogger;
import core.ViewLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;


public class MenuController implements Controller {

    private final LifeLogger log;
    private ViewLoader viewLoader;

    @FXML
    private AnchorPane anchorPane;

    public MenuController() {
        log = new LifeLogger(MenuController.class);
    }

    public void initialize(ViewLoader viewLoader) {
        log.logInitialize(MenuController.class);
        this.viewLoader = viewLoader;
        //Starts game on Enter pressed
        anchorPane.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {startGame();}
        });
        log.logFinishedInitialize(MenuController.class);
    }

    @FXML
    private void close(ActionEvent event) {
        viewLoader.close();
    }

    @FXML
    private void startGame() {
        viewLoader.loadGame();
    }
}
