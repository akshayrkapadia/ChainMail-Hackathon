package chainmail;

public class Main {
	
	public static void main(String[] args) {
		Client client = new Client("Akshay");
		Contact ubuntu = new Contact("Ubuntu", "152.23.16.223");
		Contact saumil = new Contact("Saumil", "152.23.237.180");
		client.addContact(ubuntu);
		client.addContact(saumil);
		
		
//		System.out.println(client.getIPAddress());
		
		client.createServerThread(ubuntu, client).start();
		client.createClientThread(ubuntu, client).start();
	}

}
