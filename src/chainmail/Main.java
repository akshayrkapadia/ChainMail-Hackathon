package chainmail;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		Client client = new Client("Akshay");
		
		System.out.println(client.getIPAddress());
		
		client.createServerThread().start();
		client.createClientThread("152.23.237.180").start();
	}

}
