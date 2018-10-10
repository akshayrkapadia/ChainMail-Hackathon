package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chainmail.Blockchain;
import chainmail.Client;
import chainmail.Contact;

public class ContactWidget extends JPanel implements ActionListener {

	private MainFrame mainFrame;
	private Contact contact;
	
	public ContactWidget(MainFrame mainFrame, Contact contact) {
		
		this.mainFrame = mainFrame;
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
		buttonArray.setAlignmentX(CENTER_ALIGNMENT);
		buttonArray.setAlignmentY(BOTTOM_ALIGNMENT);
		buttonArray.setBackground(Color.WHITE);
		
		JButton startChat = new JButton("Start Chat");
		startChat.setBackground(new Color(100,200, 250));
		startChat.setForeground(Color.WHITE);
		startChat.setToolTipText("Start chatting");
		startChat.setBorderPainted(false);
		startChat.addActionListener(this);
		startChat.setActionCommand("Start Chat");
		startChat.setAlignmentX(BOTTOM_ALIGNMENT);
		buttonArray.add(startChat);
		this.add(buttonArray);
	}
	
	public MainFrame getMainFrame() {
		return this.mainFrame;
	}
	
	public Client getClient() {
		return this.getMainFrame().getClient();
	}
	
	public Contact getContact() {
		return this.contact;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Start Chat")) {
			Contact contact = this.getContact();
			this.getMainFrame().getClient().startChat(contact);
			this.getMainFrame().update(this.getMainFrame().getClient().getChat(contact));
			this.getMainFrame().getClient().save();
		}
	}
}
