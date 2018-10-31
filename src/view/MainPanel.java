package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import controller.Client;
import model.BlockChain;
import model.Contact;

public class MainPanel extends JPanel {
	
	private MainFrame mainFrame;
	private SearchPanel searchPanel;
	private JScrollPane contactsPanel;
	private ChatsPanel chatsPanel;
	private StatusPanel statusPanel;
	private JScrollPane chatsView;
	private MessageWriter messageWriter;
	private JScrollPane messagesPanel;
	
	public MainPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setMaximumSize(new Dimension(1300, 850));
		this.setLayout(new BorderLayout());

		SearchPanel searchPanel = new SearchPanel(this);
		this.searchPanel = searchPanel;
		this.add(searchPanel, BorderLayout.PAGE_START);
		
		ContactsPanel contactsPanel = new ContactsPanel(this);
		JScrollPane contactsPane = new JScrollPane(contactsPanel);
		this.contactsPanel = contactsPane;
		this.add(contactsPanel, BorderLayout.LINE_START);
	
		ChatsPanel chatsOverview = new ChatsPanel(this);
		JScrollPane chatsView = new JScrollPane(chatsOverview);
		chatsOverview.setPreferredSize(new Dimension(900, 700));
		this.chatsView = chatsView;
		this.add(chatsView, BorderLayout.CENTER);
		
		StatusPanel statusPanel = new StatusPanel(this, true);
		this.statusPanel = statusPanel;
		this.add(statusPanel, BorderLayout.PAGE_END);

	}
	
	public MainFrame getMainFrame() {
		return this.mainFrame;
	}
	
	public Client getClient() {
		return this.getMainFrame().getClient();
	}
	
	public SearchPanel getSearchPanel() {
		return this.searchPanel;
	}
	
	public JScrollPane getContactsPanel() {
		return this.contactsPanel;
	}
	
	public ChatsPanel getChatsPanel() {
		return this.chatsPanel;
	}
	
	public StatusPanel getStatusPanel() {
		return this.statusPanel;
	}
	
	public void updateHome() {
		this.removeAll();
		
		SearchPanel searchPanel = new SearchPanel(this);
		this.searchPanel = searchPanel;
		this.add(searchPanel, BorderLayout.PAGE_START);
		
		ContactsPanel contactsPanel = new ContactsPanel(this);
		JScrollPane contactsPane = new JScrollPane(contactsPanel);
		this.contactsPanel = contactsPane;
		this.add(contactsPanel, BorderLayout.LINE_START);
	
		ChatsPanel chatsOverview = new ChatsPanel(this);
		JScrollPane chatsView = new JScrollPane(chatsOverview);
		chatsOverview.setPreferredSize(new Dimension(900, 700));
		this.chatsView = chatsView;
		this.add(chatsView, BorderLayout.CENTER);
		
		StatusPanel statusPanel = new StatusPanel(this, true);
		this.statusPanel = statusPanel;
		this.add(statusPanel, BorderLayout.PAGE_END);
		
		this.repaint();
		this.revalidate();
		this.setVisible(true);
	}
	
	public void updateHome(Map<Contact, BlockChain> chats) {
		this.removeAll();
		
		SearchPanel searchPanel = new SearchPanel(this);
		this.searchPanel = searchPanel;
		this.add(searchPanel, BorderLayout.PAGE_START);
		
		ContactsPanel contactsPanel = new ContactsPanel(this);
		JScrollPane contactsPane = new JScrollPane(contactsPanel);
		this.contactsPanel = contactsPane;
		this.add(contactsPanel, BorderLayout.LINE_START);
	
		ChatsPanel chatsOverview = new ChatsPanel(this, chats);
		JScrollPane chatsView = new JScrollPane(chatsOverview);
		chatsOverview.setPreferredSize(new Dimension(900, 700));
		this.chatsView = chatsView;
		this.add(chatsView, BorderLayout.CENTER);
		
		StatusPanel statusPanel = new StatusPanel(this, true);
		this.statusPanel = statusPanel;
		this.add(statusPanel, BorderLayout.PAGE_END);
		
		this.repaint();
		this.revalidate();
		this.setVisible(true);
	}
	
	public void updateMessages(BlockChain chat) {
		this.removeAll();
		
		JLabel contact = new JLabel(chat.getContact().getName());
		contact.setForeground(Color.BLACK);
		contact.setAlignmentX(CENTER_ALIGNMENT);
		contact.setAlignmentY(TOP_ALIGNMENT);
		this.add(contact, BorderLayout.PAGE_START);
		
		MessagesPanel messagesOverview = new MessagesPanel(this, chat);
		JScrollPane messagesView = new JScrollPane(messagesOverview);
		this.messagesPanel = messagesView;
		
		MessageWriter messageWriter = new MessageWriter(this, chat.getContact());
		this.messageWriter = messageWriter;
		messageWriter.setAlignmentX(CENTER_ALIGNMENT);
		messageWriter.setAlignmentY(BOTTOM_ALIGNMENT);
		
		JPanel messageUnit = new JPanel();
		messageUnit.add(messagesView);
		messageUnit.add(messageWriter);
		messageUnit.setAlignmentX(CENTER_ALIGNMENT);
		messageUnit.setAlignmentY(CENTER_ALIGNMENT);
		this.add(messageUnit, BorderLayout.CENTER);
		
		StatusPanel statusPanel = new StatusPanel(this, false);
		this.statusPanel = statusPanel;
		statusPanel.setAlignmentX(CENTER_ALIGNMENT);
		statusPanel.setAlignmentY(BOTTOM_ALIGNMENT);
		this.add(statusPanel, BorderLayout.PAGE_END);
		
		this.repaint();
		this.revalidate();
		this.setVisible(true);
	}
}
