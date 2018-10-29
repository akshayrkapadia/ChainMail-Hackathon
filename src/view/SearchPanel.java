package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import model.BlockChain;

public class SearchPanel extends JPanel implements ActionListener, KeyListener {
	
	private MainPanel MainPanel;
	private JTextField searchField;
	private JButton searchButton;
	
	public SearchPanel(MainPanel MainPanel) {
		this.MainPanel = MainPanel;
		this.setPreferredSize(new Dimension(950, 30));
		this.setMaximumSize(new Dimension(3000, 30));
		this.setBackground(Color.DARK_GRAY);
		
		JTextField searchField = new JTextField(30);
		searchField.requestFocus();
		searchField.setAlignmentX(CENTER_ALIGNMENT);
		searchField.setAlignmentY(CENTER_ALIGNMENT);
		searchField.addKeyListener(this);
		this.searchField = searchField;
		this.add(searchField);

		JButton searchButton = new JButton("Search");
		searchButton.setBackground(new Color(0,155,255));
		searchButton.setForeground(Color.WHITE);
		searchButton.setToolTipText("Searches your chats to find the closest match");
		searchButton.setBorderPainted(false);
		searchButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		searchButton.setAlignmentX(CENTER_ALIGNMENT);
		searchButton.setAlignmentY(CENTER_ALIGNMENT);
		searchButton.addActionListener(this);
		searchButton.setActionCommand("Search");
		this.searchButton = searchButton;
		this.add(searchButton);
	}
	
	public JTextField getSearchField() {
		return this.searchField;
	}
	
	public MainPanel getMainPanel() {
		return this.MainPanel;
	}

	public JButton getSearchButton() {
		return this.searchButton;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Search")) {
			try {
				ArrayList<BlockChain> chats = this.getMainPanel().getClient().getChats();
				ArrayList<BlockChain> targetChats = new ArrayList<BlockChain>();
				for (BlockChain chat : chats) {
					if (chat.getContact().getName().contains(this.getSearchField().getText())) {
						targetChats.add(chat);
					}
				}
				if (targetChats.size() == 0) {
					throw new Exception();
				} else {
					this.getMainPanel().update(chats);
				}
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(null, "No chats found");
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	     int key = e.getKeyCode();
	     if (key == KeyEvent.VK_ENTER) {
	        this.getSearchButton().doClick();
	        }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO -generated method stub
		 
	}

}
