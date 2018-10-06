package chainmail;

import java.util.Date;

public class Block implements IBlock {
	
	private int index;
	private String previousHash;
	private String message;
	private Date timestamp;
	private Block next;
	
	public Block(int index, String previousHash, String message, Block next) {
		this.index = index;
		this.previousHash = previousHash;
		this.message = message;
		this.timestamp = new Date();
		this.next = next;
	}

	@Override
	public String getPreviousHash() {
		return this.previousHash;
	}

	@Override
	public Date getTimestamp() {
		return this.timestamp;
	}

	@Override
	public Block getNext() {
		return this.next;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public int getIndex() {
		return this.index;
	}


}
