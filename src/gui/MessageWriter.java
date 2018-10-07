package gui;

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

import chainmail.Blockchain;
import chainmail.Contact;

public class MessageWriter extends JPanel implements ActionListener, KeyListener {
	
	
	private MainFrame mainFrame;
	private JTextField textField;
	private JButton sendButton;
	private Contact contact;
	
	public MessageWriter(MainFrame mainFrame, Contact contact) {
		this.contact = contact;
		this.mainFrame = mainFrame;
		this.setPreferredSize(new Dimension(950, 30));
		this.setMaximumSize(new Dimension(3000, 30));
		this.setBackground(Color.DARK_GRAY);
		
		JTextField textField = new JTextField(30);
		textField.requestFocus();
		textField.setAlignmentX(CENTER_ALIGNMENT);
		textField.setAlignmentY(CENTER_ALIGNMENT);
		textField.addKeyListener(this);
		this.textField = textField;
		this.add(textField);

		JButton sendButton = new JButton("Send");
		sendButton.setBackground(new Color(0,155,255));
		sendButton.setForeground(Color.WHITE);
		sendButton.setToolTipText("Send a message");
		sendButton.setBorderPainted(false);
		sendButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		sendButton.setAlignmentX(CENTER_ALIGNMENT);
		sendButton.setAlignmentY(CENTER_ALIGNMENT);
		sendButton.addActionListener(this);
		sendButton.setActionCommand("Send");
		this.sendButton = sendButton;
		this.add(sendButton);
	}
	
	public JTextField getTextField() {
		return this.textField;
	}
	
	public MainFrame getMainFrame() {
		return this.mainFrame;
	}
	
	public Contact getContact() {
		return this.contact;
	}

	public JButton getSendButton() {
		return this.sendButton;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Send")) {
			this.getMainFrame().getClient().createClientThread(this.getContact(), this.getMainFrame().getClient(), this.getTextField().getText());		
			this.getMainFrame().getClient().save();
			this.getMainFrame().update(this.getMainFrame().getClient().getChat(this.getContact()));
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	     int key = e.getKeyCode();
	     if (key == KeyEvent.VK_ENTER) {
	        this.getSendButton().doClick();
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
