package lab4;

import lab4.client.GomokuClient;
import lab4.data.GomokuGameState;
import lab4.gui.GomokuGUI;

/**
 * 
 *
 *	Gomoku is an ancient game, in which the objective is to place tiles on a board in such a manner, 
 *  that a vertical, horizontal or diagonal row of 5 adjacent pieces is formed. Whichever participant
 *  first accomplishes this wins the game.
 *  
 *  This program is intended to facilitate (online) gameplay.
 *  
 *  
 * @author Jakob Ankarhem 
 * @author Rasmus Bergstrom
 */

public class GomokuMain {
	
	/**
	 * Creates an instance of GomokuMain. Does nothing else.
	 */
	public GomokuMain(){
		super();
	}
	
	private final static int port = 4001;
	
	/**
	 * Starts the Gomuku client, a class to keep track of the state of the game, as well and its GUI. No arguments needed.
	 */
	
	public static void main(String[] args) {
		GomokuClient gomokuClient = new GomokuClient(port);
		//System.out.println("1");
		GomokuGameState gomokuGameState = new GomokuGameState(gomokuClient);
		//System.out.println("2");
		@SuppressWarnings("unused")
		GomokuGUI gomokuGUI = new GomokuGUI(gomokuGameState, gomokuClient);
		//System.out.println("3");
	}

}
