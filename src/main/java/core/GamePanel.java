package core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GamePanel extends JPanel{

	GamePanel(int w, int h){
		 
		this.setPreferredSize(new Dimension(w, h));
		
	}
	
	public void paint(Graphics g) {
		int s = Life.gap - Life.gap/2;
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(new BasicStroke(Life.gap));
		g2D.setColor(Color.darkGray);
		for (int i = 1; i<Life.cols; i++) {
			
			g2D.drawLine(i*Life.uSize - s, 0, i*Life.uSize - s, Life.panelHeight);
		}
		for (int i = 1; i<Life.rows; i++) {
			
			g2D.drawLine(0, i*Life.uSize - s, Life.panelWidth, i*Life.uSize - s);
		}
	}
	
	public void paintRect(GamePanel panel, int x, int y, int w) {
		Graphics g = panel.getGraphics();
		Graphics2D g2D = (Graphics2D) g;
		g2D.fillRect(x, y, w, w);
	}
}
