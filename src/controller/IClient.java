package controller;

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
import java.util.Scanner;

import javax.crypto.Cipher;

import model.Contact;

public interface IClient extends Serializable {
	
	String getName();
	String getIPAddress();
	PublicKey getPublicKey();
	PrivateKey getPrivateKey();
	String getNewMessage();
	boolean getConnected();
	void setConnected(boolean connected);
	void setNewMessage(String message);
	void setKeys(PublicKey publicKey, PrivateKey privateKey);
	void startChat(Contact contact);
	
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
	
	default Thread createThreadServer(Contact contact, Client client) {
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
                		client.createMessageWriterThread(client).start();
                        while (true) {
                            try {
                                byte[] encryptedMessage = (byte[]) input.readObject();
                                String decryptedMessage = client.decryptMessage(encryptedMessage);
                                System.out.println(contact.getName() + ": " + decryptedMessage);
                            } catch(Exception e) {
                            }
                        }
					} catch (Exception e) {
					}
				}
			}
		};
		return serverThread;
	}
	
	default Thread createClientThread(Contact contact, Client client) {
		Thread clientThread = new Thread() {
			public void run() {
            	while (true) {
                    try {
                        Socket socket = new Socket(contact.getIPAddress(), 9806);
                        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                        output.writeObject(client.getPublicKey());
                    	System.out.println("Public key sent");
                        while (true) {
                            try {
                            	if (!(client.getNewMessage().equals(""))) {
                            		System.out.println("Writing new message");
                                    byte[] encryptedMessage = client.encryptMessage(client.getNewMessage(), contact);
                                    output.writeObject(encryptedMessage);
                                    client.setNewMessage("");
                            	}
                            } catch (Exception e) {
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
		};
		return clientThread;
	}
	
	default Thread createMessageWriterThread(Client client) {
		Thread messageWriterThread = new Thread() {
			public void run() {
				System.out.println("Message writer thread started");
				if (client.getNewMessage().equals("")) {
					System.out.println("Write new message");
					Scanner s = new Scanner(System.in);
					String message = s.nextLine();
					client.setNewMessage(message);
					s.close();
				}
			}
		};
		return messageWriterThread;
	}

}
