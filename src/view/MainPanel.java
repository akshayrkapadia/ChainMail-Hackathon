package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MainPanel extends JPanel {
	
	private MainFrame mainFrame;
	private SearchPanel searchPanel;
	private ContactsPanel contactsPanel;
	private ChatsPanel chatsPanel;
	private StatusPanel statusPanel;
	
	public MainPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setMaximumSize(new Dimension(1300, 850));
		this.setLayout(new BorderLayout());

		SearchPanel searchPanel = new SearchPanel(this);
		this.searchPanel = searchPanel;
		this.add(searchPanel, BorderLayout.PAGE_START);
		
		ContactsPanel contactsPanel = new ContactsPanel(this);
		this.contactsPanel = contactsPanel;
		this.add(contactsPanel, BorderLayout.LINE_START);
	
		ChatsOverview chatsOverview = new ChatsOverview(this);
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
	
	public ConatctsPanel getContactsPanel() {
		return this.contactsPanel;
	}
	
	public ChatsPanel getChatsPanel() {
		return this.chatsPanel;
	}
	
	public StatusPanel getStatusPanel() {
		return this.statusPanel;
	}
}
