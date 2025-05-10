package core;

public enum GameMode {
    RANDOM("Random", 0),
    CHOOSE("Choose grid setup", 1);

    private String name;
    private int id;

    private GameMode(String name, int id) {
        this.name = name;
        this.id = id;
    }
    public String getName() { return name; }
    public int getId() { return id; }
}
