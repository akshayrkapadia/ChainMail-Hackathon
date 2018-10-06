package chainmail;

import java.security.PublicKey;

public interface IContact {
	
	String getName();
	String getIPAddress();
	PublicKey getPublicKey();
	void setPublicKey(PublicKey publicKey);
	


}
