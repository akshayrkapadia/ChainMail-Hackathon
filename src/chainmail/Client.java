package chainmail;

import java.util.ArrayList;

public class Client implements IClient {
	
	private String name;
	private ArrayList<Blockchain> chats;
	private String ipAddress;
	
	public Client(String name) {
		this.name = name;
		this.chats = new ArrayList<Blockchain>();
		this.ipAddress = this.findIPAddress();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public ArrayList<Blockchain> getChats() {
		// TODO Auto-generated method stub
		return this.chats;
	}

	@Override
	public String getIPAddress() {
		// TODO Auto-generated method stub
		return this.ipAddress;
	}

}
