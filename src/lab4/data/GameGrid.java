package lab4.data;

import java.util.Observable;

/**
 * Represents a two-dimensional game grid for a game of Gomoku.
 * 
 */

public class GameGrid extends Observable {
	private int gridSize;
	private int[][] gridList;
	private static final int EMPTY = 0;
	private static final int INROW = 5;

	/*
	public static void main(String[] args) {
		GameGrid test = new GameGrid(15);
		test.gridList[3][2] = 1;
	}
	*/
	
	/**
	 *  
	 * Initializes a game grid with size*size squares.
	 * @param size
	 *            The width/height of the game grid.
	 */

	public GameGrid(int size) {
		this.gridSize = size;
		this.gridList = new int[size][size];
	}

	/**
	 * What is in the square?
	 * 
	 * @param x
	 *            The x coordinate of the tile.
	 * @param y
	 *            The y coordinate of the tile.
	 *            
	 * @return The value of the specified tile.
	 */
	public int getLocation(int x, int y) {
		return this.gridList[x][y];
	}

	/**
	 * What is the grid size?
	 * @return The grid size.
	 */
	public int getSize() {
		return this.gridSize;
	}

	/**
	 * Attempts to place a tile in the specified square.
	 * 
	 * @param x
	 *            The x position.
	 * @param y
	 *            The y position.
	 * @param player
	 * 				The player performing the move.
	 * 
	 * @return (1) true if the tile was successfully placed, (2) false if it the square was already occupied.
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
	 *
	 * Clears the grid, removing all the tiles.
	 */
	public void clearGrid() {
		this.gridList = new int[this.gridSize][this.gridSize];
		setChanged();
		notifyObservers();
	}

	/**
	 * Does the player have 5 tiles in a row?
	 * 
	 * @param player
	 *            The player whose tiles are being checked.
	 * @return (1) true if player currently has 5 tiles in row, (2) false if he doesn't.
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
