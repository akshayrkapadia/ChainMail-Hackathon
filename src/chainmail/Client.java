package chainmail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Client implements IClient {
	
	private String name;
	private ArrayList<Contact> contacts;
	private ArrayList<Blockchain> chats;
	private String ipAddress;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	
	public Client(String name) {
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
		} catch (Exception e) {
			this.name = null;
			this.contacts = new ArrayList<Contact>();
			this.chats = new ArrayList<Blockchain>();
			this.ipAddress = this.findIPAddress();
			this.generateKeys();
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public ArrayList<Blockchain> getChats() {
		// TODO Auto-generated method stub
		return this.chats;
	}

	@Override
	public String getIPAddress() {
		// TODO Auto-generated method stub
		return this.ipAddress;
	}

	@Override
	public ArrayList<Contact> getContacts() {
		return this.contacts;
	}

	@Override
	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	@Override
	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}

	@Override
	public void setKeys(PrivateKey privateKey, PublicKey publicKey) {
		this.privateKey = privateKey;
		this.publicKey = publicKey;
		
	}

	@Override
	public void setName(String name) {
		this.name = name;
		
	}

}
