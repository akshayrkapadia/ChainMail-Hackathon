package chainmail;

public class Contact implements IContact {
	
	private String name;
	private String ipAddress;
	
	public Contact(String name, String ipAddress) {
		this.name = name;
		this.ipAddress = ipAddress;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getIPAddress() {
		return this.ipAddress;
	}
	
	

}
