package view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.Contact;

public class ContactsPanel extends JPanel {
	
	private MainPanel mainPanel;
	
	public ContactsPanel(MainPanel mainPanel) {
		
		this.mainPanel = mainPanel;
		this.setPreferredSize(new Dimension(350, 780));
		this.setMaximumSize(new Dimension(350, 780));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBackground(Color.WHITE);
		
		JLabel contactLabel = new JLabel("Contacts");
		contactLabel.setForeground(Color.BLACK);
		contactLabel.setAlignmentX(CENTER_ALIGNMENT);
		this.add(contactLabel);
				
		for (Contact contact : this.getContacts()) {
			ContactWidget contactWidget = new ContactWidget(mainPanel, contact);
			JPanel contactBorder = new JPanel();
			contactBorder.setBorder(BorderFactory.createEmptyBorder(10, 140, 10, 140));
			contactBorder.add(contactWidget);
			this.add(contactBorder);
		}
		
		
	}
	
	public MainPanel getMainPanel() {
		return this.mainPanel;
	}
	
	public ArrayList<Contact> getContacts() {
		return this.getMainPanel().getClient().getContacts();
	}
}
