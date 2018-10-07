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

public class ChatWidget extends JPanel implements ActionListener {

	private MainFrame mainFrame;
	private Blockchain chat;
	private Contact contact;
	
	public ChatWidget(MainFrame mainFrame, Blockchain chat) {
		
		this.mainFrame = mainFrame;
		this.chat = chat;
		this.setBorder(BorderFactory.createEmptyBorder(10, 250, 10, 250));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setMaximumSize(new Dimension(900, 95));
		this.setPreferredSize(new Dimension(900, 95));
		this.setBackground(Color.WHITE);
		
		JLabel chatContact = new JLabel(chat.getContact().getName());
		chatContact.setAlignmentX(CENTER_ALIGNMENT);
		this.add(chatContact);
		
		String message;
		if (this.getChat().length() == 1) {
			message = new String(this.getChat().getHead().getMessage());
		} else {
			byte[] encryptedMessage = this.getChat().getHead().getMessage();
			message = this.getClient().decryptMessage(encryptedMessage);
		}
		JLabel mostRecentMessage = new JLabel("Most Recent Message: " + message);
		mostRecentMessage.setAlignmentX(CENTER_ALIGNMENT);
		this.add(mostRecentMessage);
		
		LocalDateTime timestamp = this.getChat().getHead().getTimestamp();
		if (timestamp != null) {
			JLabel date = new JLabel("Date: " + timestamp.toString());
			date.setAlignmentX(CENTER_ALIGNMENT);
			this.add(date);
		}

		
		JPanel buttonArray = new JPanel();
		buttonArray.setAlignmentX(CENTER_ALIGNMENT);
		buttonArray.setAlignmentY(BOTTOM_ALIGNMENT);
		buttonArray.setBackground(Color.WHITE);
		
		JButton viewButton = new JButton("View Messages");
		viewButton.setBackground(new Color(100,200, 250));
		viewButton.setForeground(Color.WHITE);
		viewButton.setToolTipText("View the messages for this chat");
		viewButton.setBorderPainted(false);
		viewButton.addActionListener(this);
		viewButton.setActionCommand("View Messages");
		viewButton.setAlignmentX(BOTTOM_ALIGNMENT);
		buttonArray.add(viewButton);
		this.add(buttonArray);
	}
	
public ChatWidget(MainFrame mainFrame, Contact contact) {
		
		this.mainFrame = mainFrame;
		this.contact = contact;
		this.setBorder(BorderFactory.createEmptyBorder(10, 250, 10, 250));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setMaximumSize(new Dimension(900, 95));
		this.setPreferredSize(new Dimension(900, 95));
		this.setBackground(Color.WHITE);
		
		JLabel chatContact = new JLabel(chat.getContact().getName());
		chatContact.setAlignmentX(CENTER_ALIGNMENT);
		this.add(chatContact);
		
		JPanel buttonArray = new JPanel();
		buttonArray.setAlignmentX(CENTER_ALIGNMENT);
		buttonArray.setAlignmentY(BOTTOM_ALIGNMENT);
		buttonArray.setBackground(Color.WHITE);
		
		JButton viewButton = new JButton("View Contact");
		viewButton.setBackground(new Color(100,200, 250));
		viewButton.setForeground(Color.WHITE);
		viewButton.setToolTipText("View this contact");
		viewButton.setBorderPainted(false);
		viewButton.addActionListener(this);
		viewButton.setActionCommand("View Contact");
		viewButton.setAlignmentX(BOTTOM_ALIGNMENT);
		buttonArray.add(viewButton);
		this.add(buttonArray);
	}

	public MainFrame getMainFrame() {
		return this.mainFrame;
	}
	
	public Blockchain getChat() {
		return this.chat;
	}
	
	public Client getClient() {
		return this.getMainFrame().getClient();
	}
	
	public Contact getContact() {
		return this.contact;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("View Messages")) {
			
		}
	}

}
