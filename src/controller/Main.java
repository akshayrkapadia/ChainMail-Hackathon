package controller;

import model.BlockChain;
import model.Contact;

public class Main {
    public static void main(String[] args) {
        Client client = new Client();
        Contact contact = new Contact("Ubuntu", "152.23.60.170");
        BlockChain chat = new BlockChain(contact);
        client.addContact(contact);
        client.addChat(chat);
        client.createServerThread(contact, client, chat).start();;
        client.createClientThread(contact, client, chat).start();

    }
}
