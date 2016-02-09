package lab4.data;

import java.util.Observable;

/**
 * Represents the 2-d game grid
 */

public class GameGrid extends Observable {
	private int gridSize;
	private int[][] gridList;
	public static final int EMPTY = 0;
	public static final int ME = 1;
	public static final int OTHER = 2;
	public static final int INROW = 5;

	/**
	 * Constructor
	 * 
	 * @param size
	 *            The width/height of the game grid
	 */
	public static void main(String[] args) {
		GameGrid test = new GameGrid(15);
		test.gridList[3][2] = 1;
	}

	public GameGrid(int size) {
		this.gridSize = size;
		this.gridList = new int[size][size];
	}

	/**
	 * Reads a location of the grid
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 * @return the value of the specified location
	 */
	public int getLocation(int x, int y) {
		return this.gridList[x][y];
	}

	/**
	 * Returns the size of the grid
	 * 
	 * @return the grid size
	 */
	public int getSize() {
		return this.gridSize;
	}

	/**
	 * Enters a move in the game grid
	 * 
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 * @param player
	 * @return true if the insertion worked, false otherwise
	 */
	public boolean move(int x, int y, int player) {
		//System.out.println(player);
		if (this.gridList[x][y] == EMPTY) {
			this.gridList[x][y] = player;
			setChanged();
			notifyObservers();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Clears the grid of pieces
	 */
	public void clearGrid() {
		this.gridList = new int[this.gridSize][this.gridSize];
		setChanged();
		notifyObservers();
	}

	/**
	 * Check if a player has 5 in row
	 * 
	 * @param player
	 *            the player to check for
	 * @return true if player has 5 in row, false otherwise
	 */
	
	public boolean isWinner(int player) {
		for (int i = 0; i < this.gridSize; i++) {
			for (int j = 0; j < this.gridSize; j++) {
				if (this.gridList[i][j] == player) {
					if (yrow(i, j) || xrow(i, j) || negDiag(i, j) || posDiag(i, j)) {
						return true;
					}
				}

			}
		}
		return false;
	}
	
	private boolean negDiag(int r, int c){
        int p = this.gridList[r][c];
        int row = 0;
        for (int x = c, y = r; x < this.gridSize && y < this.gridSize; x++,y++) {
            if (p == this.gridList[y][x]) {
                row++;
            } else {
                break;
            }
        }
        
        return (row >= INROW);
        
    }
    
    private boolean posDiag(int r, int c){
        int p = this.gridList[r][c];
        int row = 0;
        
        if(r == 0 || c == 0) { return false; }
        
        for (int x = c, y = r; x < this.gridSize && y < this.gridSize; x--,y++) {
            if (p == this.gridList[y][x]) {
                row++;
            } else {
                break;
            }
        }
        
        return (row >= INROW);
        
    }

    private boolean xrow(int r, int c){
        int p = gridList[r][c];
        int row = 0;
        for (int i = c; i < this.gridSize; i++) {
            if (p == this.gridList[r][i]) {
                row++;
            } else {
                break;
            }
        }
        
        return (row >= INROW);
        
    }

    private boolean yrow(int r, int c){
        int p = gridList[r][c];
        int row = 0;
        for (int i = r; i < this.gridSize; i++) {
            if (p == this.gridList[i][c]) {
                row++;
            } else {
                break;
            }
        }
        
        return (row >= INROW);
        
    }

}
