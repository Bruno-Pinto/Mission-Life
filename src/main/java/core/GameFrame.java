package core;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameFrame extends JFrame{

	GameFrame(Life life, GamePanel gamePanel){
		add(gamePanel);
		pack();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("A Game of Life");
		setLocationRelativeTo(null);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					life.stop();
				} else if (e.getKeyCode() == KeyEvent.VK_R) {
					life.restart();
				}
			}
		});
	}
	
}
