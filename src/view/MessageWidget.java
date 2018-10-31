package view;

import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDateTime;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Client;
import model.Block;
import model.BlockChain;

public class MessageWidget extends JPanel {
	private MainPanel mainPanel;
	private Block block;
	private BlockChain chat;
	
	public MessageWidget(MainPanel mainPanel, Block block, BlockChain chat) {
		
		this.mainPanel = mainPanel;
		this.block = block;
		this.chat = chat;
		this.setBorder(BorderFactory.createEmptyBorder(10, 250, 10, 250));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setMaximumSize(new Dimension(900, 95));
		this.setPreferredSize(new Dimension(900, 95));
		if (!chat.getContact().getName().equals(block.getRecipient().getName())) {
			this.setBackground(new Color(100,200, 250));
		} else {
			this.setBackground(Color.WHITE);
		}
		
		String message;
		if (this.getChat().getLength() == 1) {
			message = new String(this.getChat().getHead().getMessage());
		} else {
			byte[] encryptedMessage = this.getChat().getHead().getMessage();
			message = this.getClient().decryptMessage(encryptedMessage);
		}
		JLabel messageLabel = new JLabel(message);
		messageLabel.setAlignmentX(CENTER_ALIGNMENT);
		this.add(messageLabel);
		
		LocalDateTime timestamp = this.getChat().getHead().getTimestamp();
		if (timestamp != null) {
			JLabel date = new JLabel("Date: " + timestamp.toString());
			date.setAlignmentX(CENTER_ALIGNMENT);
			this.add(date);
		}
		
	}
	
	public MainPanel getMainPanel() {
		return this.mainPanel;
	}
	
	public Block getBlock() {
		return this.block;
	}
	
	public Client getClient() {
		return this.getMainPanel().getClient();
	}
	
	public BlockChain getChat() {
		return this.chat;
	}
}