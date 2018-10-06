package chainmail;

public class Main {
	
	public static void main(String[] args) {
		Client client = new Client("Akshay");
		client.addContact("Saumil", "152.23.237.180");
		client.addContact("Ubuntu", "152.23.16.223");
		
		System.out.println(client.getIPAddress());
		
		client.createServerThread().start();
		client.createClientThread(client.findContact("Ubuntu")).start();
	}

}
