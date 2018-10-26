package controller;

import model.Block;
import model.BlockChain;
import model.Contact;

import javax.crypto.Cipher;
import java.io.*;
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

public interface IClient extends Serializable {

    enum Status {IDLE, CONNECTION_NOT_ESTABLISHED, CONNECTION_ESTABLISHED, NEW_MESSAGE}

    String getName();
    String getIPAddr();
    ArrayList<Contact> getContacts();
    ArrayList<BlockChain> getChats();
    PublicKey getPublicKey();
    PrivateKey getPrivateKey();
    Contact getMe();
    Status getStatus();
    String getNewMessage();
    void setNewMessage(String newMessage);
    void setStatus(Status newStatus);
    void setKeys(PrivateKey privateKey, PublicKey publicKey);

    default String findIPAddr() {
        try {
            final DatagramSocket socket = new DatagramSocket();
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            String ipAddress = socket.getLocalAddress().getHostAddress();
            return ipAddress;
        } catch (Exception e) {
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

    default void addChat(BlockChain newChat) {
        this.getChats().add(newChat);
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

    default BlockChain getChat(Contact contact) {
        if (this.getChats() != null) {
            for (BlockChain chat : this.getChats()) {
                if (chat.getContact().getName().equals(contact.getName())) {
                    return chat;
                }
            }
        }
        return null;
    }

    default boolean mineBlock(Block block, Contact contact) {
        BlockChain chat = this.getChat(contact);
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

    default Thread createClientThread(Contact contact, Client client, BlockChain chat) {
        Thread sendPublicKeyThread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Socket socket = new Socket(contact.getIPAddr(), 9806);
                        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                        output.writeObject(client.getPublicKey());
                        while (true) {
                            if (client.getStatus() == Status.CONNECTION_ESTABLISHED) {
                                if (!client.getNewMessage().equals("")) {
                                    try {
                                        byte[] encryptedMessage = client.encryptMessage(client.getNewMessage(), contact);
                                        Block outputBlock = new Block(chat.getLength(), encryptedMessage, chat.getHead(), contact);
                                        output.writeObject(outputBlock);
                                        client.setStatus(Status.CONNECTION_ESTABLISHED);
                                        client.setNewMessage("");
                                    } catch (Exception e) {
                                        client.setStatus(Status.CONNECTION_ESTABLISHED);
                                        client.setNewMessage("");
                                    }
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


    default Thread createServerThread(Contact contact, Client client, BlockChain chat) {
        Thread recievePublicKeyThread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        ServerSocket serverSocket = new ServerSocket(9806);
                        Socket socket = serverSocket.accept();
                        System.out.println("Connected to " + contact.getName() + "@" + contact.getIPAddr());
                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                        PublicKey publicKey = (PublicKey) input.readObject();
                        contact.setPublicKey(publicKey);
                        System.out.println("Public key recieved");
                        client.setStatus(Status.CONNECTION_ESTABLISHED);
                        while (true) {
                            try {
                                Block inputBlock = (Block) input.readObject();
                                String decryptedMessage = client.decryptMessage(inputBlock.getMessage());
                                client.setStatus(Status.NEW_MESSAGE);
                                if (inputBlock.getRecipient().getIPAddr() == client.getMe().getIPAddr()) {
                                    System.out.println("Message Confirmed");
                                    chat.addBlock(inputBlock);
                                    client.setStatus(Status.CONNECTION_ESTABLISHED);
                                } else if (client.mineBlock(inputBlock, contact)) {
                                    System.out.println(">> " + decryptedMessage);
                                    Block newBlock = new Block(chat.getLength(), client.encryptMessage(decryptedMessage, client.getMe()), chat.getHead(), inputBlock.getRecipient(), inputBlock.getTimestamp());
                                    chat.addBlock(newBlock);
                                    ObjectOutputStream confirmation = new ObjectOutputStream(socket.getOutputStream());
                                    confirmation.writeObject(new Block(inputBlock.getIndex(), client.encryptMessage(decryptedMessage, contact), inputBlock.getNext(), inputBlock.getRecipient(), inputBlock.getTimestamp()));
                                    client.setStatus(Status.CONNECTION_ESTABLISHED);
                                } else {
                                    System.out.println("Failed");
                                    client.setStatus(Status.CONNECTION_ESTABLISHED);
                                }
                            } catch(Exception e) {
                                client.setStatus(Status.CONNECTION_ESTABLISHED);
                            }
                        }
                    } catch (Exception e) {
                        client.setStatus(Status.CONNECTION_NOT_ESTABLISHED);
                    }
                }
            }
        };
        return recievePublicKeyThread;
    }

}