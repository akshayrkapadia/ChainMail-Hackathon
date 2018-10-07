package chainmail;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gui.MainWindow;

public class Main {
	
	public static void main(String[] args) {
		Client client = new Client();
		JFrame mainWindow = new MainWindow(client);
	}

}
