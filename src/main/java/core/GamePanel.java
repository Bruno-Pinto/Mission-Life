package core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GamePanel extends JPanel{
	private final Life life;
	
	GamePanel(Life life, int w, int h){
		this.life = life;
		this.setPreferredSize(new Dimension(w, h));
	}

	@Override
	public void paint(Graphics g) {
		int s = life.gap - life.gap/2;
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(new BasicStroke(life.gap));
		g2D.setColor(Color.darkGray);
		for (int i = 1; i<life.cols; i++) {
			
			g2D.drawLine(i*life.uSize - s, 0, i*life.uSize - s, life.panelHeight);
		}
		for (int i = 1; i<life.rows; i++) {
			
			g2D.drawLine(0, i*life.uSize - s, life.panelWidth, i*life.uSize - s);
		}
	}
	
	public void paintRect(GamePanel panel, int x, int y, int w) {
		Graphics g = panel.getGraphics();
		Graphics2D g2D = (Graphics2D) g;
		g2D.fillRect(x, y, w, w);
	}
}
