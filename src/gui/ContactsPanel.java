package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import chainmail.Contact;

public class ContactsPanel extends JPanel {
	
	private MainFrame mainFrame;
	
	public ContactsPanel(MainFrame mainFrame) {
		
		this.mainFrame = mainFrame;
		this.setPreferredSize(new Dimension(350, 780));
		this.setMaximumSize(new Dimension(350, 780));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBackground(Color.DARK_GRAY);
		
		for (Contact contact : this.getContacts()) {
			
			ContactWidget contactWidget = new ContactWidget(mainFrame, contact);
			JPanel contactBorder = new JPanel();
			contactBorder.setBorder(BorderFactory.createEmptyBorder(10, 140, 10, 140));
			contactBorder.add(contactWidget);
			this.add(contactBorder);
		}
		
	}
	
	public MainFrame getMainFrame() {
		return this.mainFrame;
	}
	
	public ArrayList<Contact> getContacts() {
		return this.getMainFrame().getClient().getContacts();
	}
}
