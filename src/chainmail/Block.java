package chainmail;

import java.time.LocalDateTime;
import java.util.Date;

public class Block implements IBlock {
	
	private int index;
	private byte[] previousHash;
	private byte[] message;
	private LocalDateTime timestamp;
	private Block next;
	private Contact recipient;
	
	public Block(int index, byte[] message, Contact recipient) {
		this.index = index;
		if (this.index == 0) {
			this.previousHash = this.hash();
		} else {
			this.previousHash = next.hash();
		}
		this.message = message;
		this.timestamp = LocalDateTime.now();
		this.recipient = recipient;
	}
	
	public Block(int index, byte[] message, Contact recipient, LocalDateTime timestamp) {
		this(index, message, recipient);
		this.timestamp = timestamp;
	}

	@Override
	public byte[] getPreviousHash() {
		return this.previousHash;
	}

	@Override
	public LocalDateTime getTimestamp() {
		return this.timestamp;
	}

	@Override
	public Block getNext() {
		return this.next;
	}

	@Override
	public byte[] getMessage() {
		return this.message;
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	@Override
	public Contact getRecipient() {
		return this.recipient;
	}

	@Override
	public void setNext(Block block) {
		this.next = block;
	}
	
	


}
