package chainmail;

public class Server implements IServer {
	
	private String serverID;
	
	public Server(Client client) {
		this.serverID = this.createServerID(client.getName(), client.getIPAddress());
	}

	@Override
	public String getServerID() {
		return this.serverID;
	}

}
