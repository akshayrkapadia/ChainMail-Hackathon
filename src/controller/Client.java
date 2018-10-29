package controller;

import java.security.PrivateKey;
import java.security.PublicKey;

import model.Contact;

public class Client implements IClient {
	
	private String name;
	private String ipAddress;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private String newMessage;
	private boolean connected;
	
	public Client(String name) {
		this.name = name;
		this.ipAddress = this.findIPAddress();
		this.newMessage = "";
		this.connected = false;
		this.generateKeys();
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
		this.createThreadServer(contact, this).start();
		this.createClientThread(contact, this).start();	
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
	public boolean getConnected() {
		// TODO Auto-generated method stub
		return this.connected;
	}

	@Override
	public void setConnected(boolean connected) {
		// TODO Auto-generated method stub
		this.connected = connected;
	}
}
