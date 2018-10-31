package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import model.BlockChain;
import model.Contact;

public class ChatsPanel extends JPanel {
	
	private MainPanel MainPanel;
	
	public ChatsPanel(MainPanel MainPanel) {
		
		this.MainPanel = MainPanel;
		this.setMaximumSize(new Dimension(900, 780));
		this.setLayout(new GridLayout(this.getMainPanel().getClient().getChats().size(), 1));
		this.setBackground(new Color(230, 230, 230));
		
		for (Map.Entry<Contact, BlockChain> entry : this.getMainPanel().getClient().getChats().entrySet()) {
			if (entry.getValue().getLength() != 0) {
				ChatWidget chatWidget = new ChatWidget(MainPanel, entry.getValue());
				chatWidget.setAlignmentX(CENTER_ALIGNMENT);
				
				JPanel widgetBorder = new JPanel();
				widgetBorder.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
				widgetBorder.setOpaque(false);
				widgetBorder.add(chatWidget);
				widgetBorder.setAlignmentX(CENTER_ALIGNMENT);

				this.add(widgetBorder);	
			}
		}
	}
	
	public ChatsPanel(MainPanel MainPanel, Map<Contact, BlockChain> chats) {
		
		this.MainPanel = MainPanel;
		this.setMaximumSize(new Dimension(900, 780));
		this.setLayout(new GridLayout(chats.size(), 1));
		this.setBackground(new Color(230, 230, 230));
		
		for (Map.Entry<Contact, BlockChain> entry : chats.entrySet()) {
			if (entry.getValue().getLength() != 0) {
				ChatWidget chatWidget = new ChatWidget(MainPanel, entry.getValue());
				chatWidget.setAlignmentX(CENTER_ALIGNMENT);
				
				JPanel widgetBorder = new JPanel();
				widgetBorder.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
				widgetBorder.setOpaque(false);
				widgetBorder.add(chatWidget);
				widgetBorder.setAlignmentX(CENTER_ALIGNMENT);
	
				this.add(widgetBorder);
			}
		}
	}
	
	
	public MainPanel getMainPanel() {
		return this.MainPanel;
	}

}
