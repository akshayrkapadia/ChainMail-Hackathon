package model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.time.LocalDateTime;

public interface IBlock extends Serializable {
	
	byte[] getPreviousHash();
	byte[] getCurrentHash();
	LocalDateTime getTimestamp();
	Block getNext();
	byte[] getMessage();
	int getIndex();
	Contact getRecipient();
	void setNext(Block block);
	void setPreviousHash(byte[] previousHash);
	
	default byte[] hash() {
		byte[] hashedBlock = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			if (this.getMessage() != null && this.getPreviousHash() != null) {
				byte[] blockBytes = (((Integer)this.getIndex()).toString() + new String(this.getMessage()) + new String(this.getPreviousHash())).getBytes();
				hashedBlock = digest.digest(blockBytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hashedBlock;
	}

}
