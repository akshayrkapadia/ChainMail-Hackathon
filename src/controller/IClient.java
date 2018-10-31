package controller;

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
import java.util.Map;
import java.util.Scanner;

import javax.crypto.Cipher;

import model.Block;
import model.BlockChain;
import model.Contact;

public interface IClient extends Serializable {
	
	String getName();
	String getIPAddress();
	PublicKey getPublicKey();
	PrivateKey getPrivateKey();
	String getNewMessage();
	ArrayList<Contact> getContacts();
	Map<Contact, BlockChain> getChats();
	Contact getMe();
	boolean getMessageRecieved();
	boolean isConnected();
	void setConnected(boolean connected);
	void setMessageRecieved(boolean message);
	void setNewMessage(String message);
	void setKeys(PublicKey publicKey, PrivateKey privateKey);
	void startChat(Contact contact);
	void setName(String name);
	void setIPAddress(String ipAddress);
	
    default String findIPAddress() {
        try {
            final DatagramSocket socket = new DatagramSocket();
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            String ipAddress = socket.getLocalAddress().getHostAddress();
            return ipAddress;
        } catch (Exception e) {
            return null;
        }
    }
	
    default void generateKeys() {
        try {
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
            keyGenerator.initialize(2048);
            KeyPair keyPair = keyGenerator.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();
            this.setKeys(publicKey, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }	
    
    default void addContact(Contact contact) {
    	if (!(this.getContacts().contains(contact))) {
        	this.getContacts().add(contact);
        	BlockChain chat = new BlockChain(contact);
        	this.addChat(chat);
    	}
    }
    
    default void addChat(BlockChain chat) {
    	this.getChats().put(chat.getContact(), chat);
    }

    default Contact findContact(String name) {
    	Contact target = null;
    	for (Contact contact : this.getContacts()) {
    		if (contact.getName().equals(name)) {
    			target = contact;
    		}
    	}
    	return target;
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
	
	default boolean mineBlock(Block block, Contact contact) {
		BlockChain chat = this.getChats().get(contact);
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
	
	default Thread createThreadServer(Contact contact, Client client, BlockChain chat) {
		Thread serverThread = new Thread() {
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
                        client.setConnected(true);
                        while (client.isConnected()) {
                            try {
                                Block block = (Block) input.readObject();
                                byte[] encryptedMessage = block.getMessage();
                                String decryptedMessage = client.decryptMessage(encryptedMessage);
                                if (block.equals(chat.getHead())) {
                                	Block confirmedBlock = new Block(block.getIndex(), client.encryptMessage(decryptedMessage, client.getMe()), block.getRecipient(), block.getNext(), block.getTimestamp());
                                } else if (client.mineBlock(block, contact)) {
                                	client.setMessageRecieved(true);
                                	chat.addBlock(block);
                                	ObjectOutputStream confirmation = new ObjectOutputStream(socket.getOutputStream());
									confirmation.writeObject(new Block(block.getIndex(), client.encryptMessage(decryptedMessage, contact), contact, block.getNext()));
                                } else if (decryptedMessage.equals("Confirmation Failure")) {
                                	
                                } else {

                                	ObjectOutputStream confirmation = new ObjectOutputStream(socket.getOutputStream());
									confirmation.writeObject(new Block(block.getIndex(), client.encryptMessage("Confirmation Failure", contact), contact, block.getNext()));
                                }
                                System.out.println(contact.getName() + ": " + decryptedMessage);
                            } catch(Exception e) {
                            }
                        }
					} catch (Exception e) {
					}
					break;
				}
			}
		};
		return serverThread;
	}
	
	default Thread createClientThread(Contact contact, Client client, BlockChain chat) {
		Thread clientThread = new Thread() {
			public void run() {
            	while (true) {
                    try {
                        Socket socket = new Socket(contact.getIPAddress(), 9806);
                        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                        output.writeObject(client.getPublicKey());
                    	System.out.println("Public key sent");
                    	System.out.println("Message writer thread started");
        				Thread.sleep(1000);
        				while (client.isConnected()) {
        					if (!(client.getNewMessage().equals(""))) {
	                            try {
                            		System.out.println("Sending new message");
                                    byte[] encryptedMessage = client.encryptMessage(client.getNewMessage(), contact);
                                    Block block = new Block(chat.getLength()+1, encryptedMessage, contact, chat.getHead());
                                    output.writeObject(block);
                            		System.out.println("Message sent");
                                    client.setNewMessage("");
	                            } catch (Exception e) {
	                            }
        					}
        				}
                    } catch (Exception e) {
                    }
                    break;
                }
            }
		};
		return clientThread;
	}
}
