package core;

import javax.swing.JFrame;

public class GameFrame extends JFrame{
	
	GameFrame(){

		this.add(Life.panel);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("main.Life");
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
	}
	
}
