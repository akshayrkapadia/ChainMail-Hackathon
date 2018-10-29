package model;

import java.io.Serializable;
import java.security.PublicKey;

public interface IContact extends Serializable {
	
	String getName();
	String getIPAddress();
	PublicKey getPublicKey();
	void setPublicKey(PublicKey publicKey);
	void setName(String name);
	void setIPAddress(String ipAddress);

}
