package chainmail;

import java.security.PublicKey;

public class Contact implements IContact {
	
	private String name;
	private String ipAddress;
	private PublicKey publicKey;
	
	public Contact(String name, String ipAddress) {
		this.name = name;
		this.ipAddress = ipAddress;
		this.publicKey = null;
	}
	
	public Contact(String name, String ipAddress, PublicKey publicKey) {
		this.name = name;
		this.ipAddress = ipAddress;
		this.publicKey = publicKey;
	}


	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getIPAddress() {
		return this.ipAddress;
	}

	@Override
	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	@Override
	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}
	

	
	

}
