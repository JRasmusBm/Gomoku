package lab4;

import lab4.client.GomokuClient;
import lab4.data.GomokuGameState;
import lab4.gui.GomokuGUI;

public class GomokuMain {
	
	private final static int port = 4000;
	
	public static void main(String[] args) {
		GomokuClient gomokuClient = new GomokuClient(port);
		//System.out.println("1");
		GomokuGameState gomokuGameState = new GomokuGameState(gomokuClient);
		//System.out.println("2");
		GomokuGUI gomokuGUI = new GomokuGUI(gomokuGameState, gomokuClient);
		//System.out.println("3");
	}

}
