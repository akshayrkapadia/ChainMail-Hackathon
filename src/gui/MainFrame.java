package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import chainmail.Blockchain;
import chainmail.Client;
import chainmail.Contact;

public class MainFrame extends JPanel {
	
	private Client client;
	private MainWindow mainWindow;
	private SearchPanel searchBar;
	private JScrollPane chatsView;
	private BottomPanel bottomPanel;
	private JScrollPane messagesView;
	private MessageWriter messageWriter;
	
	public MainFrame(MainWindow mainWindow) {	
		this.client = mainWindow.getClient();
		this.mainWindow = mainWindow;
		this.setMaximumSize(new Dimension(900, 850));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		SearchPanel searchBar = new SearchPanel(this);
		this.searchBar = searchBar;
		this.add(searchBar);
		
	
		ChatsOverview chatsOverview = new ChatsOverview(this);
		JScrollPane chatsView = new JScrollPane(chatsOverview);
		chatsOverview.setPreferredSize(new Dimension(900, 700));
		this.chatsView = chatsView;
		this.add(chatsView);
		
		BottomPanel bottomPanel = new BottomPanel(this, true);
		this.bottomPanel = bottomPanel;
		this.add(bottomPanel);

	}
	
	public Client getClient() {
		return this.client;
	}
	
	public MainWindow getMainwindow() {
		return this.mainWindow;
	}
	
	
	public JPanel getBottomPanel() {
		return this.bottomPanel;
	}
	
	public JScrollPane getMessagesView() {
		return this.messagesView;
	}
	
	public void update() {
		this.removeAll();
		
		SearchPanel searchBar = new SearchPanel(this);
		this.searchBar = searchBar;
		this.add(searchBar);
		
		ChatsOverview chatsOverview = new ChatsOverview(this);
		JScrollPane chatsView = new JScrollPane(chatsOverview);
		this.chatsView = chatsView;
		this.add(chatsOverview);
		
		BottomPanel bottomPanel = new BottomPanel(this, true);
		this.bottomPanel = bottomPanel;
		this.add(bottomPanel);
		
		this.repaint();
		this.revalidate();
		this.setVisible(true);
	}
	
	public void update(ArrayList<Blockchain> chats) {
		this.removeAll();
		
		SearchPanel searchBar = new SearchPanel(this);
		this.searchBar = searchBar;
		this.add(searchBar);
		
		ChatsOverview chatsOverview = new ChatsOverview(this, chats);
		JScrollPane chatsView = new JScrollPane(chatsOverview);
		this.chatsView = chatsView;
		this.add(chatsOverview);
		
		BottomPanel bottomPanel = new BottomPanel(this, true);
		this.bottomPanel = bottomPanel;
		this.add(bottomPanel);
		
		this.repaint();
		this.revalidate();
		this.setVisible(true);
	}
	
	public void update(Blockchain chat) {
		this.removeAll();
		
		JPanel contactPanel = new JPanel();
		contactPanel.setPreferredSize(new Dimension(950, 30));
		contactPanel.setMaximumSize(new Dimension(3000, 30));
		contactPanel.setBackground(Color.DARK_GRAY);
		JLabel contact = new JLabel(chat.getContact().getName());
		contact.setForeground(Color.WHITE);
		contactPanel.add(contact);
		this.add(contactPanel);
		
		MessagesView messagesOverview = new MessagesView(this, chat);
		JScrollPane messagesView = new JScrollPane(messagesOverview);
		this.messagesView = messagesView;
		this.add(messagesView);
		
		MessageWriter messageWriter = new MessageWriter(this, chat.getContact());
		this.messageWriter = messageWriter;
		this.add(messageWriter);
		
		BottomPanel bottomPanel = new BottomPanel(this, false);
		this.bottomPanel = bottomPanel;
		this.add(bottomPanel);
		
		this.repaint();
		this.revalidate();
		this.setVisible(true);
	}

}
