package chainmail;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Date;

public interface IBlock extends Serializable {
	
	byte[] getPreviousHash();
	LocalDateTime getTimestamp();
	Block getNext();
	byte[] getMessage();
	int getIndex();
	Contact getRecipient();
	void setNext(Block block);
	
	default byte[] hash() {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] blockBytes = (((Integer)this.getIndex()).toString() + new String(this.getMessage()) + this.getTimestamp().toString() + this.getPreviousHash()).getBytes();
			byte[] hashedBlock = digest.digest(blockBytes);
			return hashedBlock;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
