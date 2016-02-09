package lab4.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import lab4.data.GameGrid;

/**
 * A panel providing a graphical view of the game board
 */

public class GamePanel extends JPanel implements Observer {

	private final int UNIT_SIZE = 40;
	private GameGrid gameGrid;

	/**
	 * The constructor
	 * 
	 * @param grid
	 *            The grid that is to be displayed
	 */
	public GamePanel(GameGrid gameGrid) {
		this.gameGrid = gameGrid;
		gameGrid.addObserver(this);
		Dimension d = new Dimension(gameGrid.getSize() * UNIT_SIZE + 1, gameGrid.getSize() * UNIT_SIZE + 1);
		this.setMinimumSize(d);
		this.setPreferredSize(d);
		this.setBackground(Color.LIGHT_GRAY);
	}

	/**
	 * Returns a grid position given pixel coordinates of the panel
	 * 
	 * @param x
	 *            the x coordinates
	 * @param y
	 *            the y coordinates
	 * @return an integer array containing the [x, y] grid position
	 */
	public int[] getGridPosition(int x, int y) {
		//Coordinates scaled through UNIT_SIZE;
		int[] integerArray = {x / this.UNIT_SIZE, y / this.UNIT_SIZE};
		return integerArray;
	}

	public void update(Observable arg0, Object arg1) {
		this.repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int i = 0; i < this.gameGrid.getSize(); i++) {
			for(int j = 0; j < this.gameGrid.getSize(); j++) {
				int x = i * UNIT_SIZE;
				int y = j * UNIT_SIZE;
				paintSquare(g, x, y);
				paintPiece(g, i, j, x, y);
			}
		}
		
		
	}
	
	private void paintPiece(Graphics g, int i, int j,  int x, int y) {
		int valueOfLocation = gameGrid.getLocation(i, j);
		if(valueOfLocation != 0) {
			Color c = valueOfLocation == 1 ? Color.black : Color.red;
			g.setColor(c);
			g.fillOval(x+1, y+1, UNIT_SIZE-2, UNIT_SIZE-2);	
		}
		
	}
	private void paintSquare(Graphics g, int x, int y) {
		g.setColor(Color.black);
		g.drawRect(x, y, UNIT_SIZE, UNIT_SIZE);
	}
	
	public int getUnitSize() {
		return this.UNIT_SIZE;
	}
	
	

}
