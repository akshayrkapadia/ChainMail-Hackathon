package chainmail;

public class Main {
	
	public static void main(String[] args) {
		Client client = new Client("Akshay");
//		System.out.println(client.getName());
//		System.out.println(client.getIPAddress());
		Server server = new Server(client);
//		System.out.println(server.getServerID());
		
		server.createServerThread().start();
		client.createClientThread().start();
	}

}
