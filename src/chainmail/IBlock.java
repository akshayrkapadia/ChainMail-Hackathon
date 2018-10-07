package chainmail;

import java.io.Serializable;
import java.security.MessageDigest;
import java.time.LocalDateTime;

public interface IBlock extends Serializable {
	
	byte[] getPreviousHash();
	LocalDateTime getTimestamp();
	Block getNext();
	byte[] getMessage();
	int getIndex();
	Contact getRecipient();
	void setNext(Block block);
	void setPreviousHash(byte[] previousHash);
	
	default byte[] hash() {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			if (this.getMessage() != null && this.getTimestamp() != null && this.getPreviousHash() != null) {
				byte[] blockBytes = (((Integer)this.getIndex()).toString() + new String(this.getMessage()) + this.getTimestamp().toString() + new String(this.getPreviousHash())).getBytes();
				byte[] hashedBlock = digest.digest(blockBytes);
				return hashedBlock;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
