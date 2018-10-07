package gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import chainmail.Client;

public class MainFrame extends JPanel {
	
	private Client client;
	private MainWindow mainWindow;
	private SearchPanel searchBar;
	private ChatsOverview messagesOverview;
	
	public MainFrame(MainWindow mainWindow) {	
		this.client = mainWindow.getClient();
		this.mainWindow = mainWindow;
		this.setMaximumSize(new Dimension(900, 850));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		SearchPanel searchBar = new SearchPanel(this);
		this.searchBar = searchBar;
		this.add(searchBar);
		
	
		ChatsOverview messagesOverview = new ChatsOverview(this);
		JScrollPane taskView = new JScrollPane(messagesOverview);
		taskView.setPreferredSize(new Dimension(900, 700));
		this.messagesOverview = messagesOverview;
		this.add(taskView);

	}
	
	public Client getClient() {
		return this.client;
	}
	
	public MainWindow getMainwindow() {
		return this.mainWindow;
	}

}
