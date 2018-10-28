package model;

import java.security.PublicKey;

public class Contact implements IContact {
	
	private String name;
	private String ipAddress;
	private PublicKey publicKey;
	
	public Contact(String name, String ipAddress) {
		this.name = name;
		this.ipAddress = ipAddress;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public String getIPAddress() {
		// TODO Auto-generated method stub
		return this.ipAddress;
	}

	@Override
	public PublicKey getPublicKey() {
		// TODO Auto-generated method stub
		return this.publicKey;
	}

	@Override
	public void setPublicKey(PublicKey publicKey) {
		// TODO Auto-generated method stub
		this.publicKey = publicKey;
	}

}
