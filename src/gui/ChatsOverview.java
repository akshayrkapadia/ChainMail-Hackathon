package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import chainmail.Blockchain;


public class ChatsOverview extends JPanel {
	
	private MainFrame mainFrame;
	
	public ChatsOverview(MainFrame mainFrame) {
		
		this.mainFrame = mainFrame;
		this.setMaximumSize(new Dimension(900, 780));
		this.setLayout(new GridLayout(this.getMainFrame().getClient().getChats().size(), 1));
		this.setBackground(new Color(230, 230, 230));
		
		for (Blockchain chat : this.getMainFrame().getClient().getChats()) {
			
			ChatWidget chatWidget = new ChatWidget(mainFrame, chat);
			chatWidget.setAlignmentX(CENTER_ALIGNMENT);
			
			JPanel widgetBorder = new JPanel();
			widgetBorder.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
			widgetBorder.setOpaque(false);
			widgetBorder.add(chatWidget);
			widgetBorder.setAlignmentX(CENTER_ALIGNMENT);

			this.add(widgetBorder);
		}
	}
	
	public ChatsOverview(MainFrame mainFrame, ArrayList<Blockchain> chats) {
		
		this.mainFrame = mainFrame;
		this.setMaximumSize(new Dimension(900, 780));
		this.setLayout(new GridLayout(chats.size(), 1));
		this.setBackground(new Color(230, 230, 230));
		
		for (Blockchain chat : chats) {
			

		}
	}
	
	
	public MainFrame getMainFrame() {
		return this.mainFrame;
	}

}
