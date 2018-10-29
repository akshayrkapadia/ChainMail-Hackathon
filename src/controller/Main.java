package controller;

import view.MainFrame;

public class Main {
	
	public static void main(String[] args) {
		Client client = new Client();
		MainFrame mainFrame = new MainFrame(client);
		
	}

}
