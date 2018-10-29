package model;

import java.time.LocalDateTime;

public class Block implements IBlock {
	
	private int index;
	private byte[] previousHash;
	private byte[] message;
	private LocalDateTime timestamp;
	private Block next;
	private Contact recipient;
	
	public Block(int index, byte[] message, Contact recipient, Block nextBlock) {
		this.index = index;
		this.message = message;
		this.next = nextBlock;
		this.recipient = recipient;
		this.timestamp = LocalDateTime.now();
		this.previousHash = nextBlock.hash();
	}
	
	public Block(int index, byte[] message, Contact recipient, Block nextBlock, LocalDateTime timestamp) {
		this(index, message, recipient, nextBlock);
		this.timestamp = timestamp;
	}
	
	public Block(int index, byte[] message, Contact recipient, Block nextBlock, byte[] previousHash) {
		this.index = index;
		this.message = message;
		this.next = nextBlock;
		this.recipient = recipient;
		this.timestamp = LocalDateTime.now();
		this.previousHash = previousHash;
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

	@Override
	public byte[] getCurrentHash() {
		// TODO Auto-generated method stub
		return this.hash();
	}
	
}