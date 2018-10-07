package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import chainmail.Contact;

public class BottomPanel extends JPanel implements ActionListener {
	
private MainFrame mainFrame;
	
	public BottomPanel(MainFrame mainFrame, boolean home) {		
		
		this.mainFrame = mainFrame;
		this.setPreferredSize(new Dimension(950, 35));
		this.setMaximumSize(new Dimension(3000,35));
		this.setBackground(Color.DARK_GRAY);
		
		JLabel ipAddress = new JLabel("My IP Address: " + this.getMainFrame().getClient().findIPAddress());
		ipAddress.setForeground(Color.WHITE);
		ipAddress.setAlignmentX(LEFT_ALIGNMENT);
		this.add(ipAddress);
		
		if (home) {
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
			
			JButton startChat = new JButton("Start Chat");
			startChat.setBackground(new Color(0,155,235));
			startChat.setForeground(Color.WHITE);
			startChat.setToolTipText("Start a new chat");
			startChat.setBorderPainted(false);	
			startChat.addActionListener(this);
			startChat.setActionCommand("Start Chat");
			startChat.setAlignmentX(CENTER_ALIGNMENT);
			startChat.setAlignmentY(CENTER_ALIGNMENT);
			this.add(startChat);
		}
		
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
		} else if (e.getActionCommand().equals("Add Contact")) {
			while (true) {
				String name = JOptionPane.showInputDialog("Name");
				if (name == null) {
					break;
				}
				String ipAddress = JOptionPane.showInputDialog("IP Address");
				if (ipAddress == null) {
					break;
				}
				this.getMainFrame().getClient().addContact(new Contact(name, ipAddress));
				this.getMainFrame().getClient().save();
				this.getMainFrame().update();
				break;
			}
		} else if (e.getActionCommand().equals("Start Chat")) {
			ArrayList<String> optionsList = new ArrayList<String>();
			for (Contact contact : this.getMainFrame().getClient().getContacts()) {
				if (this.getMainFrame().getClient().getChat(contact) == null) {
					optionsList.add(contact.getName());
				}
			}
			if (optionsList.size() == 0) {
				
			} else {
				while (true) {
					String[] options = new String[optionsList.size()];
					for (int i = 0; i<options.length; i++) {
						options[i] = optionsList.get(i);
					}
					String contactName;
					contactName = (String)JOptionPane.showInputDialog( null, "Importance", "Importance", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					if (contactName == null) {
						break;
					} else {
						Contact contact = this.getMainFrame().getClient().findContact(contactName);
						this.getMainFrame().getClient().startChat(contact);
						this.getMainFrame().getClient().recievePublicKey(contact, this.getMainFrame().getClient()).start();
						this.getMainFrame().getClient().sendPublicKey(contact, this.getMainFrame().getClient()).start();
						this.getMainFrame().getClient().createServerThread(contact, this.getMainFrame().getClient(), this.getMainFrame()).start();
						this.getMainFrame().update(this.getMainFrame().getClient().getChat(contact));
						this.getMainFrame().getClient().save();
						break;
					}
				}
			}
		}
		
	}

}
