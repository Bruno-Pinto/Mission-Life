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
	private final int cellSize;
	private final int gap;
	private final int uSize;
	private Graphics2D g2D;
	private static final Color squareColor = Color.CYAN;
	private static final Color gridColor = Color.BLACK;
	private static final Color backgroundColor = gridColor;
	
	GamePanel(int cols, int rows, int cellWidth, int gap) {
		this.cols = cols;
		this.rows = rows;
		this.gap = gap;
		this.cellSize = cellWidth;
		this.uSize = cellWidth + gap;
		panelWidth = cols*uSize-gap;
		panelHeight = rows *uSize-gap;
		this.setPreferredSize(new Dimension(panelWidth, panelHeight));
	}

	public void setG2D() {
		g2D = (Graphics2D) this.getGraphics();
	}

	public Graphics2D getG2D() { return g2D; }

	@Override
	public void paint(Graphics g) {
		Graphics2D graphics2D = (Graphics2D) g;
		int s = gap - gap/2;
		graphics2D.setColor(backgroundColor);
		graphics2D.fillRect(0, 0, panelWidth, panelHeight);
		graphics2D.setStroke(new BasicStroke(gap));
		graphics2D.setColor(gridColor);
		if (gap!=0) {
			for (int i = 1; i<cols; i++) {
				graphics2D.drawLine(i*uSize - s, 0, i*uSize - s, panelHeight);
			}
			for (int i = 1; i<rows; i++) {
				graphics2D.drawLine(0, i*uSize - s, panelWidth, i*uSize - s);
			}
		}
	}

	public void paintSquare( int x, int y, int state) {
		if (state == 1) {
			g2D.setColor(squareColor);
		} else {
			g2D.setColor(backgroundColor);
		}
		g2D.fillRect(x, y, cellSize, cellSize);
	}
}
