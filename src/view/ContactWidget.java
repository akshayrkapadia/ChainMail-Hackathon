package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.Client;
import model.Contact;

public class ContactWidget extends JPanel implements ActionListener {
	
	private MainPanel MainPanel;
	private Contact contact;
	
	public ContactWidget(MainPanel MainPanel, Contact contact) {
		
		this.MainPanel = MainPanel;
		this.contact = contact;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setMaximumSize(new Dimension(340, 80));
		this.setPreferredSize(new Dimension(340, 80));
		this.setBackground(Color.WHITE);
		
		JLabel contactLabel = new JLabel(this.getContact().getName());
		contactLabel.setAlignmentX(CENTER_ALIGNMENT);
		this.add(contactLabel);
		
		JLabel ipLabel = new JLabel(this.getContact().getIPAddress());
		ipLabel.setAlignmentX(CENTER_ALIGNMENT);
		this.add(ipLabel);
		
		JPanel buttonArray = new JPanel();
		buttonArray.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		buttonArray.setBackground(Color.WHITE);
		
		JButton editButton = new JButton("Edit");
		editButton.setBackground(new Color(100,200, 250));
		editButton.setForeground(Color.WHITE);
		editButton.setToolTipText("Edit contact information");
		editButton.setBorderPainted(false);
		editButton.addActionListener(this);
		editButton.setActionCommand("Edit");
		editButton.setAlignmentX(CENTER_ALIGNMENT);
		editButton.setAlignmentY(BOTTOM_ALIGNMENT);
		buttonArray.add(editButton);
		
		JButton chatButton = new JButton("Chat");
		chatButton.setBackground(new Color(100,200, 250));
		chatButton.setForeground(Color.WHITE);
		chatButton.setToolTipText("Starts a chat with the contact");
		chatButton.setBorderPainted(false);
		chatButton.addActionListener(this);
		chatButton.setActionCommand("Chat");
		chatButton.setAlignmentX(CENTER_ALIGNMENT);
		chatButton.setAlignmentY(BOTTOM_ALIGNMENT);
		buttonArray.add(chatButton);
		
		
		this.add(buttonArray);
	}
	
	public MainPanel getMainPanel() {
		return this.MainPanel;
	}
	
	public Client getClient() {
		return this.getMainPanel().getClient();
	}
	
	public Contact getContact() {
		return this.contact;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Edit")) {
			Contact contact = this.getContact();
			String[] options = {"Name", "IP Address"};
			String editField = (String)JOptionPane.showInputDialog( null, "Contact Field", "Contact Field", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (editField.equals("Name")) {
				while (true) {
					String name = JOptionPane.showInputDialog("Name");
					if (name == null) {
						break;
					} else {
						contact.setName(name);
						break;
					}
				}
			} else if (editField.equals("IP Address")) {
				while (true) {
					String ipAddress = JOptionPane.showInputDialog("IP Address");
					if (ipAddress == null) {
						break;
					} else {
						contact.setName(ipAddress);
						break;
					}
				}
			}
			this.getMainPanel().getClient().save();
			this.getMainPanel().updateHome();
		} else if (e.getActionCommand().equals("Chat")) {
			this.getMainPanel().getClient().startChat(contact);
			this.getMainPanel().updateMessages(this.getMainPanel().getClient().getChats().get(contact));
			this.getMainPanel().getClient().save();
		}
	}

}
