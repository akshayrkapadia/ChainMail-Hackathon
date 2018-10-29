package controller;

import java.util.Scanner;

import model.Contact;

public class Main {
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Your name: ");
		String name = in.next();
		Client client = new Client(name);
		System.out.println("Contact name: ");
		String contactName = in.next();
		System.out.println("Contact IP Address: ");
		String contactIP = in.next();
		in.close();
		Contact contact = new Contact(contactName, contactIP);
		client.startChat(contact);
		
	}

}
