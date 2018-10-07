package chainmail;

import javax.swing.JFrame;

import gui.MainWindow;

public class Main {
	
	public static void main(String[] args) {
		Client client = new Client("Akshay");
		Contact ubuntu = new Contact("Ubuntu", "152.23.16.223");
		Contact saumil = new Contact("Saumil", "152.23.237.180");
		client.addContact(ubuntu);
		client.addContact(saumil);
		Blockchain chat = new Blockchain(ubuntu);
		client.addChat(chat);
		JFrame mainWindow = new MainWindow(client);
		
		
		
//		System.out.println(client.getIPAddress());
//		
//		client.startChat(ubuntu);
	}

}
