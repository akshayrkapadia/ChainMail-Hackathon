package controller;

import model.Block;
import model.BlockChain;
import model.Contact;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Client implements IClient {

    private String name;
    private ArrayList<Contact> contacts;
    private ArrayList<BlockChain> chats;
    private String ipAddr;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private Status status;
    private String newMessage;
    private Contact me;

    public Client() {
        try {
            FileInputStream file = new FileInputStream("ChainMail.ser");
            ObjectInputStream object = new ObjectInputStream(file);
            Client client = (Client) object.readObject();
            object.close();
            file.close();
            this.name = client.getName();
            this.contacts = client.getContacts();
            this.chats = client.getChats();
            this.ipAddr = client.getIPAddr();
            this.publicKey = client.getPublicKey();
            this.privateKey = client.getPrivateKey();
            this.me = client.me;
        } catch (Exception e) {
            while (true) {
                String name = JOptionPane.showInputDialog("Name");
                this.name = name;
                break;
            }
            this.contacts = new ArrayList<Contact>();
            this.chats = new ArrayList<BlockChain>();
            this.ipAddr = this.findIPAddr();
            this.generateKeys();
            this.me = new Contact(this.name, this.ipAddr);
            this.me.setPublicKey(this.getPublicKey());
            this.save();
        }
        this.status = Status.IDLE;
        this.newMessage = "";
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getIPAddr() {
        return this.ipAddr;
    }

    @Override
    public ArrayList<Contact> getContacts() {
        return this.contacts;
    }

    @Override
    public ArrayList<BlockChain> getChats() {
        return this.chats;
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
    public Contact getMe() {
        return null;
    }

    @Override
    public Status getStatus() {
        return this.status;
    }

    @Override
    public String getNewMessage() {
        return this.newMessage;
    }

    @Override
    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }

    @Override
    public void setStatus(Status newStatus) {
        this.status = newStatus;
    }

    @Override
    public void setKeys(PrivateKey privateKey, PublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }
}
