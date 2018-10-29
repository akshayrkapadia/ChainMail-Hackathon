package controller;

import java.io.FileInputStream; 
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import model.BlockChain;
import model.Contact;

public class Client implements IClient {
	
	private String name;
	private String ipAddress;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private ArrayList<Contact> contacts;
	private Map<Contact, BlockChain> chats;
	private String newMessage;
	private boolean connected;
	private boolean messageRecieved;
	private Contact me;
	
	public Client() {		
		this.connected = false;
		this.newMessage = "";
		this.messageRecieved = false;
		try {
			FileInputStream file = new FileInputStream("ChainMail.ser");
			ObjectInputStream object = new ObjectInputStream(file);
			Client client = (Client) object.readObject();
			object.close();
			file.close();
			this.name = client.getName();
			this.contacts = client.getContacts();
			this.chats = client.getChats();
			this.ipAddress = client.getIPAddress();
			this.publicKey = client.getPublicKey();
			this.privateKey = client.getPrivateKey();
			this.me = client.me;
		} catch (Exception e) {
			while (true) {
				String name = JOptionPane.showInputDialog("Name");
				this.name = name;
				break;
			}
			this.contacts = new ArrayList<Contact>();
			this.chats = new HashMap<Contact, BlockChain>();
			this.ipAddress = this.findIPAddress();
			this.generateKeys();
			this.me = new Contact(this.name, this.ipAddress);
			this.me.setPublicKey(publicKey);
			this.save();
		}
	}

	@Override
	public PrivateKey getPrivateKey() {
		// TODO Auto-generated method stub
		return this.privateKey;
	}

	@Override
	public PublicKey getPublicKey() {
		// TODO Auto-generated method stub
		return this.publicKey;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public String getIPAddress() {
		// TODO Auto-generated method stub
		return this.ipAddress;
	}

	@Override
	public void setKeys(PublicKey publicKey, PrivateKey privateKey) {
		// TODO Auto-generated method stub
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	@Override
	public void startChat(Contact contact) {
		BlockChain chat = this.getChats().get(contact);
		this.createThreadServer(contact, this, chat).start();
		this.createClientThread(contact, this, chat).start();	
//		this.createMessageWriterThread(this).start();
	}

	@Override
	public String getNewMessage() {
		// TODO Auto-generated method stub
		return this.newMessage;
	}

	@Override
	public void setNewMessage(String message) {
		// TODO Auto-generated method stub
		this.newMessage = message;
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return this.connected;
	}

	@Override
	public void setConnected(boolean connected) {
		// TODO Auto-generated method stub
		this.connected = connected;
	}

	@Override
	public ArrayList<Contact> getContacts() {
		// TODO Auto-generated method stub
		return this.contacts;
	}

	@Override
	public Map<Contact, BlockChain> getChats() {
		// TODO Auto-generated method stub
		return this.chats;
	}

	@Override
	public Contact getMe() {
		// TODO Auto-generated method stub
		return this.me;
	}

	@Override
	public boolean getMessageRecieved() {
		// TODO Auto-generated method stub
		return this.messageRecieved;
	}

	@Override
	public void setMessageRecieved(boolean message) {
		// TODO Auto-generated method stub
		this.messageRecieved = message;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name = name;
	}

	@Override
	public void setIPAddress(String ipAddress) {
		// TODO Auto-generated method stub
		this.ipAddress = ipAddress;
	}
}
