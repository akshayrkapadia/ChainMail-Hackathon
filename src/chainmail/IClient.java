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
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
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
	void setKeys(PrivateKey privateKey, PublicKey publicKey);
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
	
	default void saveKeys(PrivateKey privateKey, PublicKey publicKey) {
		try {
			FileOutputStream publicKeyFile = new FileOutputStream("publicKey.ser");
			FileOutputStream privateKeyFile = new FileOutputStream("privateKey.ser");
			ObjectOutputStream publicKeyFileObject = new ObjectOutputStream(publicKeyFile);
			ObjectOutputStream privateKeyFileObject = new ObjectOutputStream(privateKeyFile);
			publicKeyFileObject.writeObject(publicKey);
			privateKeyFileObject.writeObject(privateKey);
			publicKeyFileObject.close();
			privateKeyFileObject.close();
			publicKeyFile.close();
			privateKeyFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	default void generateKeys() {
		try {
			KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("DSA", "SUN");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGenerator.initialize(1024, random);
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
			byte[] messageBytes = message.getBytes("UTF8");
		    Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");   
		    cipher.init(Cipher.ENCRYPT_MODE, contact.getPublicKey());  
		    return cipher.doFinal(messageBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	default String decryptMessage(byte[] encryptedMessage) {
		try {
		    Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");   
		    cipher.init(Cipher.DECRYPT_MODE, this.getPrivateKey());  
		    return new String(cipher.doFinal(encryptedMessage), "UTF8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	default Thread createClientThread(Contact contact, Client client) {
		Thread clientThread = new Thread() {
			public void run() {
				while (true) {
					try {
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
							Block outputBlock = new Block(0, null, encryptedMessage, null);
							output.writeObject(outputBlock);
						}
					} catch(Exception e) {
					}
	
				}
			}
		};
		return clientThread;
	}
	
	default Thread createServerThread(Contact contact) {
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
							System.out.println("Message received: " + inputBlock.getMessage());
						}
					} catch(Exception e) {
					}
				}
			}
		};
		return serverThread;
	}
}
