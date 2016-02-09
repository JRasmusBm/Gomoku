package lab4.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import lab4.client.GomokuClient;
import lab4.data.GomokuGameState;

/*
 * The GUI class
 */

public class GomokuGUI implements Observer {

	private GomokuClient client;
	private GomokuGameState gamestate;
	
	//Strings
	private final String gameName = "Gomoku";
	private final String connectButtonToolTip = "Click this button to connect";
	private final String disconnectButtonToolTip = "Click this button to disconnect";
	private final String newGameButtonToolTip = "Click this button to clear the bord and create a new game";
	
	//Buttons n' shit
	private JButton connectButton;
	private JButton disconnectButton;
	private JButton newGameButton;
	private JLabel messageLabel;
	private GamePanel gamePanel;
	private Dimension buttonDim = new Dimension(20, 20);

	/**
	 * The constructor
	 * 
	 * @param g
	 *            The game state that the GUI will visualize
	 * @param c
	 *            The client that is responsible for the communication
	 */
	public GomokuGUI(GomokuGameState gameState, GomokuClient gomokuClient) {
		this.client = gomokuClient;
		this.gamestate = gameState;
		client.addObserver(this);
		gamestate.addObserver(this);

		JFrame frame = new JFrame(gameName);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		createGamePanel(frame);
		SpringLayout layout = new SpringLayout();
		
		labelCreator();
		buttonCreator();
		
		frame.add(gamePanel);
		frame.add(connectButton);
		frame.add(newGameButton);
		frame.add(disconnectButton);
		frame.add(messageLabel);
		
		layoutCreator(layout, frame);
		frame.addKeyListener(new Listen2Keyboard());
		frame.setLayout(layout);
		frame.setLocation(0, 0);
		frame.setVisible(true);

	}
	
	private void layoutCreator(SpringLayout layout, JFrame frame) {
		layout.putConstraint(SpringLayout.NORTH, gamePanel, 0, SpringLayout.WEST, frame.getContentPane());
		layout.putConstraint(SpringLayout.NORTH, connectButton, 10, SpringLayout.SOUTH, gamePanel);
		layout.putConstraint(SpringLayout.NORTH, newGameButton, 10, SpringLayout.SOUTH, gamePanel);
		layout.putConstraint(SpringLayout.NORTH, disconnectButton, 10, SpringLayout.SOUTH, gamePanel);
		
		layout.putConstraint(SpringLayout.WEST, newGameButton, 5, SpringLayout.EAST, connectButton);
		layout.putConstraint(SpringLayout.WEST, disconnectButton, 5, SpringLayout.EAST, newGameButton);
		
		layout.putConstraint(SpringLayout.NORTH, messageLabel, 10, SpringLayout.SOUTH, connectButton);
	}
	
	private void createGamePanel(JFrame frame) {
		this.gamePanel = new GamePanel(this.gamestate.getGameGrid());
		this.gamePanel.addMouseListener(new Listen2Mouse(frame));
	}
	
	private void labelCreator() {
		this.messageLabel = new JLabel("Welcome to Gomoku! :)");
	}

	private void buttonCreator() {
		this.connectButton = new JButton("Connect");
		this.connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ConnectionWindow(client);
			}
		});
		
		this.newGameButton = new JButton("New Game");
		this.newGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gamestate.newGame();
			}
		});

		this.disconnectButton = new JButton("Rage Quit");
		this.disconnectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gamestate.disconnect();
			}
		});
		
		this.connectButton.setToolTipText(connectButtonToolTip);
		this.connectButton.setMaximumSize(buttonDim);
		this.disconnectButton.setToolTipText(disconnectButtonToolTip);
		this.disconnectButton.setMaximumSize(buttonDim);
		this.newGameButton.setToolTipText(newGameButtonToolTip);
		this.newGameButton.setSize(buttonDim);
		
	}

	public void update(Observable arg0, Object arg1) {

		// Update the buttons if the connection status has changed
		if (arg0 == client) {
			if (client.getConnectionStatus() == GomokuClient.UNCONNECTED) {
				connectButton.setEnabled(true);
				newGameButton.setEnabled(false);
				disconnectButton.setEnabled(false);
			} else {
				connectButton.setEnabled(false);
				newGameButton.setEnabled(true);
				disconnectButton.setEnabled(true);
			}
		}

		// Update the status text if the gamestate has changed
		if (arg0 == gamestate) {
			messageLabel.setText(gamestate.getMessageString());
		}

	}
	
	private class Listen2Mouse extends MouseAdapter{
		private JFrame frame;
		
		public Listen2Mouse(JFrame frame) {
			// TODO Auto-generated constructor stub
			this.frame = frame;
		}
		
		public void mouseClicked(MouseEvent e) {
			int[] xy = gamePanel.getGridPosition(e.getX(), e.getY());
			//System.out.println("Clicked: " + "x:" + xy[0] + " y:" + xy[1]);
			if(xy[0] < gamestate.getGameGrid().getSize()){
				gamestate.move(xy[0], xy[1]);
			}
		}
		
		public void mouseEntered(MouseEvent e) {
			frame.requestFocus();
		}
	}
	
	private class Listen2Keyboard extends KeyAdapter {
		private SpringLayout layout2 = new SpringLayout();
		private JFrame frame2 = new JFrame("Please enter coordinates for move: ");
		private JButton okButton = new JButton("OK");
		private JButton cancelButton = new JButton("Cancel");
		private JLabel xLabel = new JLabel("Enter x coordinate: ");
		private JLabel yLabel = new JLabel("Enter y coordinate: ");
		private JTextField fieldx = new JTextField();
		private JTextField fieldy = new JTextField();
		
		public void keyTyped(KeyEvent e) {
			if (e.getKeyChar() == 'c') {
				String s = !gamestate.cheatModeOn() ? "Activating cheatmode" : "Deactivating cheatmode";
				messageLabel.setText(s);
				gamestate.toggleCheatMode();
			} else if ( e.getKeyChar() == 'p') {
				System.out.println('p');
				createFrame2();
				frame2.setVisible(true);
				
			}
		}
		
		private void createFrame2() {
			frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			frame2.add(okButton);
			okButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int xValue = Integer.parseInt(fieldx.getText());
					int yValue = Integer.parseInt(fieldy.getText());
					gamestate.move(xValue, yValue);
					frame2.dispose();
				}
				
			});
			
			frame2.add(cancelButton);
			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					frame2.dispose();
				}
				
			});
			frame2.add(xLabel);
			frame2.add(yLabel);
			frame2.add(fieldx);
			fieldx.setPreferredSize(new Dimension(100, 50));
			frame2.add(fieldy);
			fieldy.setPreferredSize(new Dimension(100, 50));
			
			setConstraints4Frame2();
			frame2.setLayout(layout2);
		}
		
		private void moveXY() {
			
		}
		
		private void setConstraints4Frame2() {
			layout2.putConstraint(SpringLayout.WEST, xLabel, 10, SpringLayout.WEST, frame2.getContentPane());
			layout2.putConstraint(SpringLayout.WEST, yLabel, 10, SpringLayout.WEST, frame2.getContentPane());
			
			layout2.putConstraint(SpringLayout.NORTH, xLabel, 40, SpringLayout.NORTH, frame2.getContentPane());
			layout2.putConstraint(SpringLayout.NORTH, fieldx, 20, SpringLayout.NORTH, frame2.getContentPane());
			
			layout2.putConstraint(SpringLayout.NORTH, yLabel, 40, SpringLayout.SOUTH, xLabel);
			layout2.putConstraint(SpringLayout.NORTH, fieldy, 20, SpringLayout.SOUTH, fieldx);
			
			layout2.putConstraint(SpringLayout.WEST, fieldx, 20, SpringLayout.EAST, xLabel);
			layout2.putConstraint(SpringLayout.WEST, fieldy, 20, SpringLayout.EAST, yLabel);
			
			layout2.putConstraint(SpringLayout.NORTH, okButton, 20, SpringLayout.SOUTH, fieldy);
			layout2.putConstraint(SpringLayout.EAST, okButton, -10, SpringLayout.WEST, cancelButton);
			
			layout2.putConstraint(SpringLayout.NORTH, cancelButton, 20, SpringLayout.SOUTH, fieldy);
			layout2.putConstraint(SpringLayout.EAST, cancelButton, 0, SpringLayout.EAST, fieldy);
		}
		
	}

}
