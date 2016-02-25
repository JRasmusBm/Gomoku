/*
 * Created on 2007 feb 8
 */
package lab4.data;

import java.util.Observable;
import java.util.Observer;

import lab4.client.GomokuClient;

/**
 * Represents the state of a game of Gomoku.
 */

public class GomokuGameState extends Observable implements Observer {

	// Game variables
	private final int DEFAULT_SIZE = 15;
	private GameGrid gameGrid;

	// Move messages
	private final String wasAbleToMoveMessage = "Sending the move.";
	private final String noGameMessage = "There is not game currently running.";
	private final String notYourTurnMessage = "It's not your turn!";
	private final String gameIsOverMessage = "The game is over!";
	private final String youHaveWonGameMessage = "You have won the game!";
	private final String invalidMoveMessage = "You cannot place your tile there.";

	// Disconnect messages
	private final String disconnectedMessage = "You have ended the game by disconnecting, ragequit much?";
	private final String otherDisconnectedMessage = "Your opponent has ragequitted.";

	// State messages
	private final String myTurnMessage = "It's your turn!";
	private final String otherTurnMessage = "It's your opponents turn.";

	// Possible game states
	private final int NOT_STARTED = 0;
	private final int MY_TURN = 1;
	private final int OTHER_TURN = 2;
	private final int FINISHED = 3;
	private int currentState;
	private boolean cheatOn = false;

	private GomokuClient client;

	private String message;

	/**
	 * Takes a game client as a parameter and adds it as an observer. Creates a game grid.
	 * 
	 * @param gc
	 *            The client used to communicate with the other player.
	 */
	public GomokuGameState(GomokuClient gc) {
		client = gc;
		client.addObserver(this);
		gc.setGameState(this);
		currentState = NOT_STARTED;
		gameGrid = new GameGrid(DEFAULT_SIZE);
	}

	/**
	 * Returns the message string, that holds a predefined message depending on what state the game is in.
	 * @return The message string.
	 */
	public String getMessageString() {
		return this.message;
	}

	/**
	 * Returns the game grid.
	 * @return The game grid.
	 */
	public GameGrid getGameGrid() {
		return this.gameGrid;
	}

	/**
	 * Attempts to place a tile in a specified square.
	 * 
	 * @param x
	 *            The x coordinate of the square.
	 * @param y
	 *            The y coordinate of the square.
	 */
	public void move(int x, int y) {
		if (this.cheatOn) {
			if (x >= this.gameGrid.getSize() || y >= this.gameGrid.getSize()) {
				this.client.sendMoveMessage(x, y);
				String s = "Attempting to crash opponent by sending [" + x + ", " + y + "].";
				this.message = s;
				System.out.println(s);
			} else {
				this.gameGrid.move(x, y, MY_TURN);
				String s = "Moving [" + x + ", " + y + "] with cheatmode enabled.";
				this.message = s;
				System.out.println(s);
				this.client.sendMoveMessage(x, y);
			}
			if (gameGrid.isWinner(MY_TURN)) {
				this.message = "Won with cheatmode enabled.";
			}

		} else if (this.currentState == MY_TURN && this.gameGrid.move(x, y, MY_TURN)) {
			// gameGrid.move(x, y, this.currentState);
			System.out.println("Moving [" + x + ", " + y + "].");
			this.message = this.wasAbleToMoveMessage;
			this.client.sendMoveMessage(x, y);
			this.currentState = OTHER_TURN;
			if (gameGrid.isWinner(MY_TURN)) {
				this.message = this.youHaveWonGameMessage;
				this.currentState = FINISHED;
			}
		} else if (this.currentState == NOT_STARTED) {
			this.message = this.noGameMessage;
		} else if (this.currentState == OTHER_TURN) {
			this.message = this.notYourTurnMessage;
		} else if (this.currentState == FINISHED) {
			this.message = this.gameIsOverMessage;
		} else {
			this.message = this.invalidMoveMessage;
		}

		setChanged();
		notifyObservers();
	}

	/**
	 * Starts a new game with the current client.
	 */
	public void newGame() {
		this.gameGrid.clearGrid();
		this.client.sendNewGameMessage();
		this.currentState = OTHER_TURN;
		this.message = otherTurnMessage;
		setChanged();
		notifyObservers();
	}

	/**
	 * Other player has requested a new game. The game state is changed accordingly.
	 */
	public void receivedNewGame() {
		this.gameGrid.clearGrid();
		this.currentState = MY_TURN;
		this.message = myTurnMessage;
		setChanged();
		notifyObservers();
	}

	/**
	 * The connection to the other player is lost, so the game is interrupted.
	 */
	public void otherGuyLeft() {
		this.gameGrid.clearGrid();
		this.client.disconnect();
		this.currentState = FINISHED;
		this.message = this.otherDisconnectedMessage;
		setChanged();
		notifyObservers();
	}

	/**
	 * Disconnects the player from the client.
	 */
	public void disconnect() {
		this.gameGrid.clearGrid();
		this.client.disconnect();
		this.currentState = FINISHED;
		this.message = this.disconnectedMessage;
		setChanged();
		notifyObservers();
	}

	/**
	 * The player receives a move from the other player.
	 * 
	 * @param x
	 *            The x coordinate of the placed tile.
	 * @param y
	 *            The y coordinate of the placed tile.
	 */
	public void receivedMove(int x, int y) {
		if (currentState == OTHER_TURN && x < gameGrid.getSize() && y < gameGrid.getSize()) {
			this.gameGrid.move(x, y, OTHER_TURN);
			if (gameGrid.isWinner(OTHER_TURN)) {
				this.currentState = FINISHED;
				this.message = this.gameIsOverMessage;
			} else {
				this.currentState = MY_TURN;
			}
		} else if (x < gameGrid.getSize() && y < gameGrid.getSize()) {
			this.gameGrid.move(x, y, OTHER_TURN);
			String s =  "Opponent moved [" + x + ", " + y + "] by cheating.";
			this.message = s;
			System.out.println(s);
			this.currentState = MY_TURN;
			if(gameGrid.isWinner(OTHER_TURN)) {
				this.currentState = FINISHED;
				this.message = "Opponent won by cheating";
			}
		} else {
			String s = "Opponent tried to move [" + x + ", " + y + "] by cheating.";
			this.message = s;
			System.out.println(s);
			// this.client.sendMoveMessage(1000, 1000);
		}
		setChanged();
		notifyObservers();

	}
	
	/**
	 * Switches the turn.
	 *
	 */

	public void update(Observable o, Object arg) {

		switch (client.getConnectionStatus()) {
		case GomokuClient.CLIENT:
			message = "Game started, it is your turn!";
			currentState = MY_TURN;
			break;
		case GomokuClient.SERVER:
			message = "Game started, waiting for other player...";
			currentState = OTHER_TURN;
			break;
		}
		setChanged();
		notifyObservers();

	}
	
	/**
	 * Toggles the cheat mode.
	 */

	public void toggleCheatMode() {
		this.cheatOn = !this.cheatOn;
	}
	
	/**
	 * Is the cheat mode on?
	 * @return (1) true if the cheat mode is on, (2) false if it's not.
	 */

	public boolean cheatModeOn() {
		return this.cheatOn;
	}

}
