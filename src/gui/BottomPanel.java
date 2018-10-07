package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BottomPanel extends JPanel implements ActionListener {
	
private MainFrame mainFrame;
	
	public BottomPanel(MainFrame mainFrame) {		
		
		this.mainFrame = mainFrame;
		this.setPreferredSize(new Dimension(950, 35));
		this.setMaximumSize(new Dimension(3000,35));
		this.setBackground(Color.DARK_GRAY);
		
		JLabel ipAddress = new JLabel("My IP Address: " + this.getMainFrame().getClient().findIPAddress());
		ipAddress.setForeground(Color.WHITE);
		ipAddress.setAlignmentX(LEFT_ALIGNMENT);
		this.add(ipAddress);
		
		JButton addContact = new JButton("Add Contact");
		addContact.setBackground(new Color(0,155,235));
		addContact.setForeground(Color.WHITE);
		addContact.setToolTipText("Add a new task");
		addContact.setBorderPainted(false);	
		addContact.addActionListener(this);
		addContact.setActionCommand("Add Contact");
		addContact.setAlignmentX(CENTER_ALIGNMENT);
		addContact.setAlignmentY(CENTER_ALIGNMENT);
		this.add(addContact);		
		
		JButton homeButton = new JButton("Home");
		homeButton.setBackground(new Color(0, 155, 235));
		homeButton.setForeground(Color.WHITE);
		homeButton.setToolTipText("Go back to the chats overview");
		homeButton.setBorderPainted(false);
		homeButton.addActionListener(this);
		homeButton.setActionCommand("Home");
		homeButton.setAlignmentX(CENTER_ALIGNMENT);
		homeButton.setAlignmentY(CENTER_ALIGNMENT);
		this.add(homeButton);
		
	}
	
	public MainFrame getMainFrame() {
		return this.mainFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Add")) {
		} else if (e.getActionCommand().equals("Home")) {
			this.getMainFrame().update();
			this.getMainFrame().getClient().save();
		}
		
	}

}
