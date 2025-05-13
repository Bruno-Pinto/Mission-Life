package user_interface.controllers;

public interface LifeListener {
    void onGridUpdate(byte[][] oldGrid, byte[][] newGrid);
}
