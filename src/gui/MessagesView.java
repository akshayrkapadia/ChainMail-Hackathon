package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chainmail.Block;
import chainmail.Blockchain;
import chainmail.Client;
import chainmail.Contact;

public class MessagesView extends JPanel {
	
		private MainFrame mainFrame;
		private Blockchain chat;
		
	public MessagesView(MainFrame mainFrame, Blockchain chat) {
		this.mainFrame = mainFrame;
		this.chat = chat;
		this.setMaximumSize(new Dimension(900, 780));
		this.setLayout(new GridLayout(chat.length(), 1));
		this.setBackground(new Color(230, 230, 230));
		
		Block block = chat.getHead();
		while(block.getNext() != null) {
			
			MessageWidget messageWidget = new MessageWidget(mainFrame, block, chat);
			messageWidget.setAlignmentX(CENTER_ALIGNMENT);
			
			JPanel widgetBorder = new JPanel();
			widgetBorder.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
			widgetBorder.setOpaque(false);
			widgetBorder.add(messageWidget);
			widgetBorder.setAlignmentX(CENTER_ALIGNMENT);

			this.add(widgetBorder);
		}
	}
	
	
	public MainFrame getMainFrame() {
		return this.mainFrame;
	}
	
	public Client getClient() {
		return 	this.getMainFrame().getClient();	
	}
}

