package chainmail;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Scanner;

import javax.crypto.Cipher;


public interface IClient extends Serializable {
	
	String getName();
	ArrayList<Contact> getContacts();
	ArrayList<Blockchain> getChats();
	String getIPAddress();
	PublicKey getPublicKey();
	PrivateKey getPrivateKey();
	RSAPublicKeySpec getPublicKeySpec();
	RSAPrivateKeySpec getPrivateKeySpec();
	void setKeys(PrivateKey privateKey, PublicKey publicKey, RSAPublicKeySpec publicKeySpec, RSAPrivateKeySpec privateKeySpec);
	void setName(String name);
	
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
//	default void saveKeys(PrivateKey privateKey, PublicKey publicKey) {
//		try {
//			FileOutputStream publicKeyFile = new FileOutputStream("publicKey.ser");
//			FileOutputStream privateKeyFile = new FileOutputStream("privateKey.ser");
//			ObjectOutputStream publicKeyFileObject = new ObjectOutputStream(publicKeyFile);
//			ObjectOutputStream privateKeyFileObject = new ObjectOutputStream(privateKeyFile);
//			publicKeyFileObject.writeObject(publicKey);
//			privateKeyFileObject.writeObject(privateKey);
//			publicKeyFileObject.close();
//			privateKeyFileObject.close();
//			publicKeyFile.close();
//			privateKeyFile.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	default void generateKeys() {
		try {
			KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
			keyGenerator.initialize(2048);
			KeyPair keyPair = keyGenerator.generateKeyPair();
			PrivateKey privateKey = keyPair.getPrivate();
			PublicKey publicKey = keyPair.getPublic();
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec rsaPublicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
			RSAPrivateKeySpec rsaPrivateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
			this.setKeys(privateKey, publicKey, rsaPublicKeySpec, rsaPrivateKeySpec);
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
		for (Blockchain chat : this.getChats()) {
			if (chat.getContact().getName().equals(contact.getName())) {
				return chat;
			}
		}
		return null;
	}
	
	default boolean mineBlock(Block block) {
		Blockchain chat = this.getChat(block.getRecipient());
		Block head = chat.getHead();
		if (new String(block.getPreviousHash()).equals(new String(head.hash()))) {
			return true;
		}
		return false;
	}
	
	default Thread createClientThread(Contact contact, Client client) {
		Thread clientThread = new Thread() {
			public void run() {
				while (true) {
					try {
						if (client.getChat(contact) == null) {
							client.addChat(new Blockchain(contact));
						}
						Socket socket = new Socket(contact.getIPAddress(), 9806);
						System.out.println("Connected to " + contact.getName() + " at " + contact.getIPAddress());
						while (true) {
							try {
								ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
								output.writeObject(client.getPublicKey());
								break;
							} catch (Exception e) {
								
							}
						}
						while(true) {
		 					ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
							Scanner scanner = new Scanner(System.in);
							byte[] encryptedMessage = client.encryptMessage(scanner.nextLine(), contact);
							Block outputBlock = new Block(0, encryptedMessage, contact);
							output.writeObject(outputBlock);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
	
				}
			}
		};
		return clientThread;
	}
	
	default Thread createServerThread(Contact contact, Client client) {
		Thread serverThread = new Thread() {
			public void run() {
				while (true) {
					try {
						ServerSocket serverSocket = new ServerSocket(9806);
						Socket socket = serverSocket.accept();
						ObjectInputStream publicKeyInput = new ObjectInputStream(socket.getInputStream());
						PublicKey publicKey = (PublicKey) publicKeyInput.readObject();
						contact.setPublicKey(publicKey);
						System.out.println("Public Key Recieved");
						while(true) {
							ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
							Block inputBlock = (Block) input.readObject();
							String decryptedMessage = client.decryptMessage(inputBlock.getMessage());
							if (decryptedMessage.equals("Confirmed")) {
								Blockchain chat = client.getChat(contact);
								Block newBlock = new Block(chat.length(), client.encryptMessage(decryptedMessage, contact), inputBlock.getRecipient(), inputBlock.getTimestamp());
								chat.addBlock(newBlock);
							} else if (client.mineBlock(inputBlock)) {
								Blockchain chat = client.getChat(contact);
								Block newBlock = new Block(chat.length(), client.encryptMessage(decryptedMessage, contact), inputBlock.getRecipient(), inputBlock.getTimestamp());
								chat.addBlock(newBlock);
								ObjectOutputStream confirmation = new ObjectOutputStream(socket.getOutputStream());
								confirmation.writeObject(new Block(0, client.encryptMessage("Confirmed", contact), contact));
							}
							System.out.println("Message received: " + decryptedMessage);
						}
					} catch(Exception e) {
					}
				}
			}
		};
		return serverThread;
	}
}
