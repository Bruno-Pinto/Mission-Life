package core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GamePanel extends JPanel{
	private final int panelWidth;
	private final int panelHeight;
	private final int cols;
	private final int rows;
	private final int uSize;
	private final int gap;
	
	GamePanel(int cols, int rows, int uSize, int gap) {
		this.cols = cols;
		this.rows = rows;
		this.uSize = uSize;
		this.gap = gap;
		panelWidth = cols*uSize-gap;
		panelHeight = rows *uSize-gap;
		this.setPreferredSize(new Dimension(panelWidth, panelHeight));
	}

	@Override
	public void paint(Graphics g) {
		int s = gap - gap/2;
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(new BasicStroke(gap));
		g2D.setColor(Color.darkGray);
		for (int i = 1; i<cols; i++) {
			
			g2D.drawLine(i*uSize - s, 0, i*uSize - s, panelHeight);
		}
		for (int i = 1; i<rows; i++) {
			
			g2D.drawLine(0, i*uSize - s, panelWidth, i*uSize - s);
		}
	}
	
	public void paintRect(GamePanel panel, int x, int y, int w) {
		Graphics g = panel.getGraphics();
		Graphics2D g2D = (Graphics2D) g;
		g2D.fillRect(x, y, w, w);
	}
}
