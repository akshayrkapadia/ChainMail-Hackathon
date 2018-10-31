package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Contact;

public class StatusPanel extends JPanel implements ActionListener {
	
	private MainPanel mainPanel;

	public StatusPanel(MainPanel mainPanel, boolean home) {		
		
		this.mainPanel = mainPanel;
		this.setPreferredSize(new Dimension(950, 35));
		this.setMaximumSize(new Dimension(3000,35));
		this.setBackground(Color.DARK_GRAY);
		
		JLabel ipAddress = new JLabel("My IP Address: " + this.getMainPanel().getClient().findIPAddress());
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
	
	public MainPanel getMainPanel() {
		return this.mainPanel;
	}
	
	private static final Pattern PATTERN = Pattern.compile(
	        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

	public static boolean validate(final String ip) {
	    return PATTERN.matcher(ip).matches();
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Add")) {
		} else if (e.getActionCommand().equals("Home")) {
			this.getMainPanel().updateHome();
			this.getMainPanel().getClient().save();
		} else if (e.getActionCommand().equals("Add Contact")) {
			while (true) {
				String name = JOptionPane.showInputDialog("Name");
				if (name == null) {
					break;
				} else if (name.length() == 0) {
					JOptionPane.showMessageDialog(null, "Must enter contact name", "Error", JOptionPane.WARNING_MESSAGE);	
					continue;
				} else if (this.getMainPanel().getClient().findContact(name) != null) {
					JOptionPane.showMessageDialog(null, "Contact already exists", "Error", JOptionPane.WARNING_MESSAGE);	
					continue;
				}
				String ipAddress;
				while (true) {
					ipAddress = JOptionPane.showInputDialog("IP Address");
					if (ipAddress == null) {
						break;
					} else if (!validate(ipAddress)) {
						JOptionPane.showMessageDialog(null, "Invalid IP Address", "Error", JOptionPane.WARNING_MESSAGE);	
						continue;
					} else {
						break;
					}
				}
				if (ipAddress == null) {
					break;
				}
				this.getMainPanel().getClient().addContact(new Contact(name, ipAddress));
				this.getMainPanel().getClient().save();
				this.getMainPanel().updateHome();
				break;
			}
		} else if (e.getActionCommand().equals("Start Chat")) {
			ArrayList<String> optionsList = new ArrayList<String>();
			for (Contact contact : this.getMainPanel().getClient().getContacts()) {
				if (!(this.getMainPanel().getClient().getChats().containsKey(contact))) {
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
					contactName = (String)JOptionPane.showInputDialog( null, "Contact", "Contact", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					if (contactName == null) {
						break;
					} else {
						Contact contact = this.getMainPanel().getClient().findContact(contactName);
						this.getMainPanel().getClient().startChat(contact);
						this.getMainPanel().updateMessages(this.getMainPanel().getClient().getChats().get(contact));
						this.getMainPanel().getClient().save();
						break;
					}
				}
			}
		}
		
	}
}
