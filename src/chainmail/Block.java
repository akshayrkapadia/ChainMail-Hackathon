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
	
	public Block(int index, byte[] message, Contact recipient, Block block) {
		this.index = index;
		this.message = message;
		this.next = block;
		this.previousHash = block.hash();
		this.timestamp = LocalDateTime.now();
		this.recipient = recipient;
	}
	
	public Block(int index, byte[] message, Contact recipient, Block block, LocalDateTime timestamp) {
		this(index, message, recipient, block);
		this.timestamp = timestamp;
	}
	
	public Block(int index, byte[] message, Contact recipient, Block block, byte[] previousHash) {
		this.index = index;
		this.message = message;
		this.recipient = recipient;
		this.next = block;
		this.previousHash = previousHash;
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

	@Override
	public void setPreviousHash(byte[] previousHash) {
		this.previousHash = previousHash;
	}
	
	


}
