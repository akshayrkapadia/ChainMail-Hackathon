package gui;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import chainmail.Client;

public class MainWindow extends JFrame {
	
	private Client client;
	
	public MainWindow(Client client) {
		this.client = client;
		this.setPreferredSize(new Dimension(950,850));
		this.setMaximumSize(new Dimension(950,850));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("ChainMail");
		// Icon made by Freepik from www.flaticon.com 
		this.setIconImage(new ImageIcon(getClass().getResource("/images/icon.png")).getImage());
		
		MainFrame mainFrame = new MainFrame(this);
		
		this.getContentPane().add(mainFrame);
		this.pack();	
		this.setVisible(true);
	}
	
	public Client getClient() {
		return this.client;
	}

}
