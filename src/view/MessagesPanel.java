package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import controller.Client;
import model.Block;
import model.BlockChain;

public class MessagesPanel extends JPanel {
	
	private MainPanel mainPanel;
	private BlockChain chat;
	
	public MessagesPanel(MainPanel mainPanel, BlockChain chat) {
		this.mainPanel = mainPanel;
		this.chat = chat;
		this.setMaximumSize(new Dimension(900, 780));
		this.setPreferredSize(new Dimension(900, 780));
		this.setLayout(new GridLayout(chat.getLength(), 1));
		this.setBackground(new Color(230, 230, 230));
		
		Block block = chat.getHead();
		while(block.getNext() != null) {
			MessageWidget messageWidget = new MessageWidget(mainPanel, block, chat);
			messageWidget.setAlignmentX(CENTER_ALIGNMENT);
			
			JPanel widgetBorder = new JPanel();
			widgetBorder.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
			widgetBorder.setOpaque(false);
			widgetBorder.add(messageWidget);
			widgetBorder.setAlignmentX(CENTER_ALIGNMENT);
	
			this.add(widgetBorder);
		}
	}
	
	
	public MainPanel getMainPanel() {
		return this.mainPanel;
	}
	
	public Client getClient() {
		return 	this.getMainPanel().getClient();	
	}

}
