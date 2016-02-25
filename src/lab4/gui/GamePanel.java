package lab4.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import lab4.data.GameGrid;

/**
 * A panel providing a graphical view of the game grid.
 */

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Observer {

	private final int UNIT_SIZE = 40;
	private GameGrid gameGrid;

	/**
	 * Constructs the game panel using a Graphics object.
	 * 
	 * @param gameGrid
	 *            The grid that is to be displayed.
	 */
	public GamePanel(GameGrid gameGrid) {
		this.gameGrid = gameGrid;
		gameGrid.addObserver(this);
		Dimension d = new Dimension(gameGrid.getSize() * getUnitSize() + 1, gameGrid.getSize() * getUnitSize() + 1);
		this.setMinimumSize(d);
		this.setPreferredSize(d);
		this.setBackground(Color.LIGHT_GRAY);
	}

	/**
	 * Converts pixel coordinates into grid coordinates.
	 * 
	 * @param x
	 *            The x coordinate. (Pixels)
	 * @param y
	 *            The y coordinate. (Pixels)
	 * @return An integer array containing the [x, y] grid coordinates.
	 */
	public int[] getGridPosition(int x, int y) {
		//Coordinates scaled through UNIT_SIZE;
		int[] integerArray = {x / this.getUnitSize(), y / this.getUnitSize()};
		return integerArray;
	}

	/**
	 * Repaints the game panel.
	 */
	
	public void update(Observable arg0, Object arg1) {
		this.repaint();
	}
	
	/**
	 * Paints the game panel.
	 * @param g 
	 * 			A Graphics object.
	 */

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int i = 0; i < this.gameGrid.getSize(); i++) {
			for(int j = 0; j < this.gameGrid.getSize(); j++) {
				int x = i * getUnitSize();
				int y = j * getUnitSize();
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
			g.fillOval(x+1, y+1, getUnitSize()-2, getUnitSize()-2);	
		}
		
	}
	private void paintSquare(Graphics g, int x, int y) {
		g.setColor(Color.black);
		g.drawRect(x, y, getUnitSize(), getUnitSize());
	}
	
	/**
	 * What size does a tile have?
	 * @return 
	 * 		The size of a tile.
	 */
	
	public int getUnitSize() {
		return this.UNIT_SIZE;
	}
	
	

}
