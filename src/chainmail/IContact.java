package chainmail;

import java.io.Serializable;
import java.security.PublicKey;

public interface IContact extends Serializable {
	
	String getName();
	String getIPAddress();
	PublicKey getPublicKey();
	void setPublicKey(PublicKey publicKey);
	


}
