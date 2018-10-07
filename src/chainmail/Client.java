package chainmail;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;

public class Client implements IClient {
	
	private String name;
	private ArrayList<Contact> contacts;
	private ArrayList<Blockchain> chats;
	private String ipAddress;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private RSAPublicKeySpec publicKeySpec;
	private RSAPrivateKeySpec privateKeySpec;
	
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
	public void setKeys(PrivateKey privateKey, PublicKey publicKey, RSAPublicKeySpec publicKeySpec, RSAPrivateKeySpec privateKeySpec) {
		this.privateKey = privateKey;
		this.publicKey = publicKey;
		this.publicKeySpec = publicKeySpec;
		this.privateKeySpec = privateKeySpec;	
	}

	@Override
	public void setName(String name) {
		this.name = name;
		
	}

	@Override
	public RSAPublicKeySpec getPublicKeySpec() {
		return this.publicKeySpec;
	}

	@Override
	public RSAPrivateKeySpec getPrivateKeySpec() {
		return this.privateKeySpec;
	}
	
	public  void startChat(Contact contact) {
		if (this.getChat(contact) == null) {
			this.addChat(new Blockchain(contact));
		}
		this.recievePublicKey(contact, this).run();
		this.sendPublicKey(contact, this).run();		
	}

}
