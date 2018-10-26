package controller;

import model.BlockChain;
import model.Contact;

public class Main {
    public static void main(String[] args) {

        Thread thread1 = new Thread() {
            public void run() {
                Client client = new Client();
                Contact contact = new Contact("Akshay", client.getIPAddr());
                BlockChain chat = new BlockChain(contact);
                client.addChat(chat);
                client.createServerThread(contact, client, chat);
                client.createClientThread(contact, client, chat);
                if (client.getStatus() == IClient.Status.CONNECTION_ESTABLISHED) {
                    client.setNewMessage("Hello");
                }
            }
        };

        Thread thread2 = new Thread() {
            public void run() {
                Client client = new Client();
                Contact contact = new Contact("Akshay", client.getIPAddr());
                BlockChain chat = new BlockChain(contact);
                client.addChat(chat);
                client.createServerThread(contact, client, chat);
                client.createClientThread(contact, client, chat);
            }
        };

        thread1.start();
        thread2.start();

    }
}
