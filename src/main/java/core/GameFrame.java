package core;

import javax.swing.*;

public class GameFrame extends JFrame{
	
	GameFrame(GamePanel gamePanel){

		add(gamePanel);
		pack();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("A Game of Life");
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
}
