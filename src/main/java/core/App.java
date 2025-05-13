package core;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    private static final LifeLogger log = new LifeLogger(App.class);

    public static void main(String[] args) {
        if (System.getProperty("appMode") == null) { System.setProperty("appMode", "release"); }
        log.info("Running App in mode: " + System.getProperty("appMode"));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("A Game of Life");
        ViewLoader viewLoader = new ViewLoader(primaryStage);
        viewLoader.loadMenu();
        primaryStage.show();
    }
}