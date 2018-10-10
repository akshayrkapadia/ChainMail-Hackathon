package chainmail;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.Cipher;

import gui.MainFrame;


public interface IClient extends Serializable {
	
	String getName();
	ArrayList<Contact> getContacts();
	ArrayList<Blockchain> getChats();
	String getIPAddress();
	PublicKey getPublicKey();
	PrivateKey getPrivateKey();
	boolean getWriteNew();
	String getNewMessage();
	void setKeys(PrivateKey privateKey, PublicKey publicKey);
	void setName(String name);
	void startChat(Contact contact);
	void setNewMessage(String message);
	void setWriteNew(boolean writeNew);
	
	default String findIPAddress() {
		try {
			final DatagramSocket socket = new DatagramSocket();
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			String ipAddress = socket.getLocalAddress().getHostAddress();
			return ipAddress;
		} catch(Exception e) {
			return null;
		}
	}
	
	default Contact findContact(String name) {
		for (Contact contact : this.getContacts()) {
			if (name.equals(contact.getName())) {
				return contact;
			}
		}
		return null;
	}
	
	default void addContact(Contact contact) {
		if (this.findContact(contact.getName()) == null) {
			this.getContacts().add(contact);
		}
	}

	default void save() {
		try {
			FileOutputStream file = new FileOutputStream("ChainMail.ser");
			ObjectOutputStream object = new ObjectOutputStream(file);
			object.writeObject(this);
			object.close();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	default void addChat(Blockchain chat) {
		this.getChats().add(chat);
	}
	
	default void generateKeys() {
		try {
			KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
			keyGenerator.initialize(2048);
			KeyPair keyPair = keyGenerator.generateKeyPair();
			PrivateKey privateKey = keyPair.getPrivate();
			PublicKey publicKey = keyPair.getPublic();
			this.setKeys(privateKey, publicKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	default byte[] encryptMessage(String message, Contact contact) {
		try {
			byte[] messageBytes = message.getBytes();
		    Cipher cipher = Cipher.getInstance("RSA");
		    cipher.init(Cipher.ENCRYPT_MODE, contact.getPublicKey());
		    return cipher.doFinal(messageBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	default String decryptMessage(byte[] encryptedMessage) {
		try {
		    Cipher cipher = Cipher.getInstance("RSA");   
		    cipher.init(Cipher.DECRYPT_MODE, this.getPrivateKey());  
		    return new String(cipher.doFinal(encryptedMessage));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	default Blockchain getChat(Contact contact) {
		if (this.getChats() != null) {
			for (Blockchain chat : this.getChats()) {
				if (chat.getContact().getName().equals(contact.getName())) {
					return chat;
				}
			}
		}
		return null;
	}
	
	default boolean mineBlock(Block block, Contact contact) {
		Blockchain chat = this.getChat(contact);
		if (chat != null && chat.getHead() != null) {
			Block head = chat.getHead();
			byte[] blockPreviousHash = block.getPreviousHash();
			byte[] headHash = head.hash();
			if (Arrays.equals(headHash, blockPreviousHash)) {
				return true;
			}
		}
		return false;
	}
	
	default Thread createClientThread(Contact contact, Client client) {
		Thread sendPublicKeyThread = new Thread() {
			public void run() {
				while (true) {
					try {
						Socket socket = new Socket(contact.getIPAddress(), 9806);
						ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
						output.writeObject(client.getPublicKey());
						while (true) {
							if (client.getWriteNew()) {
								try {
									byte[] encryptedMessage = client.encryptMessage(client.getNewMessage(), contact);
									Block outputBlock = new Block(0, encryptedMessage, contact, client.getChat(contact).getHead());
									output.writeObject(outputBlock);
								} catch(Exception e) {
								}
							}
			
						}
					} catch (Exception e) {
					}
				}
			}
		};
		return sendPublicKeyThread;
	}
	
	
	default Thread createServerThread(Contact contact, Client client) {
		Thread recievePublicKeyThread = new Thread() {
			public void run() {
				while (true) {
					try {
						ServerSocket serverSocket = new ServerSocket(9806);
						Socket socket = serverSocket.accept();
						System.out.println("Connected to " + contact.getName() + "@" + contact.getIPAddress());
						ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
						PublicKey publicKey = (PublicKey) input.readObject();
						contact.setPublicKey(publicKey);
						System.out.println("Public key recieved");
						while (true) {
							try {
								Block inputBlock = (Block) input.readObject();
								String decryptedMessage = client.decryptMessage(inputBlock.getMessage());
								if (decryptedMessage.equals("Confirmed")) {
									System.out.println("Message Confirmed");
									Blockchain chat = client.getChat(contact);
									Block newBlock = new Block(chat.length(), client.encryptMessage(decryptedMessage, contact), inputBlock.getRecipient(), chat.getHead(), inputBlock.getTimestamp());
									chat.addBlock(newBlock);
								} else if (client.mineBlock(inputBlock, contact)) {
									System.out.println(">> " + decryptedMessage);
									Blockchain chat = client.getChat(contact);
									Block newBlock = new Block(chat.length(), client.encryptMessage(decryptedMessage, contact), inputBlock.getRecipient(), chat.getHead(), inputBlock.getTimestamp());
									chat.addBlock(newBlock);
									ObjectOutputStream confirmation = new ObjectOutputStream(socket.getOutputStream());
									confirmation.writeObject(new Block(0, client.encryptMessage("Confirmed", contact), contact, chat.getHead().getNext()));
								} else {
									System.out.println("Failed");
								}
							} catch(Exception e) {
							}
						}
					} catch (Exception e) {
					}
				}
			}
		};
		return recievePublicKeyThread;
	}

}
