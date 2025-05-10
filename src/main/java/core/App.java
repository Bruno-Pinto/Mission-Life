package core;

public class App {

    private static final LifeLogger log = new LifeLogger(App.class);

    public static void main(String[] args) {
        log.info("Running App in mode: " + System.getProperty("appMode"));
        Life life = new Life();
        life.initialize();
        life.start();
    }
}