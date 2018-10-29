package view;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import controller.Client;

public class MainFrame extends JFrame {
	
	private Client client;
	
	public MainFrame(Client client) {
		this.client = client;
		
		this.setPreferredSize(new Dimension(1300,850));
		this.setMaximumSize(new Dimension(1300,850));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("ChainMail");
		// Icon made by Freepik from www.flaticon.com 
		this.setIconImage(new ImageIcon(getClass().getResource("/images/icon.png")).getImage());
		
		MainPanel mainPanel = new MainPanel(this);
		
		this.getContentPane().add(mainPanel);
		this.pack();	
		this.setVisible(true);
	}
	
	public Client getClient() {
		return this.client;
	}

}