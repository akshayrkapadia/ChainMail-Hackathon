package chainmail;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.Cipher;

import gui.MainFrame;


public interface IClient extends Serializable {
	
	String getName();
	ArrayList<Contact> getContacts();
	ArrayList<Blockchain> getChats();
	String getIPAddress();
	PublicKey getPublicKey();
	PrivateKey getPrivateKey();
	void setKeys(PrivateKey privateKey, PublicKey publicKey);
	void setName(String name);
	void startChat(Contact contact);
	
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
	
	default Thread sendPublicKey(Contact contact, Client client) {
		Thread sendPublicKeyThread = new Thread() {
			public void run() {
				while (true) {
					try {
						Socket socket = new Socket(contact.getIPAddress(), 9806);
						ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
						output.writeObject(client.getPublicKey());
						System.out.println("Pub sent");
						break;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		return sendPublicKeyThread;
	}
	
	default Thread createClientThread(Contact contact, Client client, String message) {
		Thread clientThread = new Thread() {
			public void run() {
				while (true) {
					try {
						Socket socket = new Socket(contact.getIPAddress(), 9807);
						ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
						try {
							byte[] encryptedMessage = client.encryptMessage(message, contact);
							Block outputBlock = new Block(0, encryptedMessage, contact, client.getChat(contact).getHead());
							output.writeObject(outputBlock);
							System.out.println("sent");
							break;
						} catch(Exception e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		return clientThread;
	}
	
	
	default Thread recievePublicKey(Contact contact, Client client, MainFrame mainFrame) {
		Thread recievePublicKeyThread = new Thread() {
			public void run() {
				try {
					System.out.println("Started pub serv");
					ServerSocket serverSocket = new ServerSocket(9806);
					Socket socket = serverSocket.accept();
					System.out.println("Pub conn");
					ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
					PublicKey publicKey = (PublicKey) input.readObject();
					contact.setPublicKey(publicKey);
					System.out.println("Pub recv");
					socket.close();
					serverSocket.close();
					client.createServerThread(contact, client, mainFrame).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		return recievePublicKeyThread;
	}
	
	
	default Thread createServerThread(Contact contact, Client client, MainFrame mainFrame) {
		Thread serverThread = new Thread() {
			public void run() {
				while (true) {
					try {
						System.out.println("main Serv started");
						ServerSocket serverSocket = new ServerSocket(9807);
						Socket socket = serverSocket.accept();
						System.out.println("Serv conn");
						ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
						try {
							Block inputBlock = (Block) input.readObject();
							String decryptedMessage = client.decryptMessage(inputBlock.getMessage());
							System.out.println(decryptedMessage);
							if (decryptedMessage.equals("Confirmed")) {
								System.out.println("Message Confirmed");
								Blockchain chat = client.getChat(contact);
								Block newBlock = new Block(chat.length(), client.encryptMessage(decryptedMessage, contact), inputBlock.getRecipient(), chat.getHead(), inputBlock.getTimestamp());
								chat.addBlock(newBlock);
								client.save();
							} else if (client.mineBlock(inputBlock, contact)) {
								System.out.println("> " + decryptedMessage);
								Blockchain chat = client.getChat(contact);
								Block newBlock = new Block(chat.length(), client.encryptMessage(decryptedMessage, contact), inputBlock.getRecipient(), chat.getHead(), inputBlock.getTimestamp());
								chat.addBlock(newBlock);
								while (true) {
									ObjectOutputStream confirmation = new ObjectOutputStream(socket.getOutputStream());
									confirmation.writeObject(new Block(0, client.encryptMessage("Confirmed", contact), contact, chat.getHead().getNext()));
									break;
								}
								client.save();
								mainFrame.update(client.getChat(contact));
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		return serverThread;
	}

}
