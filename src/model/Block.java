package model;

import java.time.LocalDateTime;

public class Block implements IBlock {

    private LocalDateTime timestamp;
    private byte[] message;
    private int index;
    private byte[] hash;
    private byte[] previousHash;
    private Block next;
    private Contact recipient;

    public Block(int index, byte[] message, Block nextBlock, Contact recipient) {
        this.index = index;
        this.message = message;
        this.next = nextBlock;
        this.recipient = recipient;
        this.timestamp = LocalDateTime.now();
        this.previousHash = (nextBlock == null) ? "Genesis Block".getBytes() : nextBlock.hash();
        this.hash = this.hash();
    }

    public Block(int index, byte[] message, Block nextBlock, Contact recipient, LocalDateTime timestamp) {
        this(index, message, nextBlock, recipient);
        this.timestamp = timestamp;
    }

    @Override
    public byte[] getPreviousHash() {
        return this.previousHash;
    }

    @Override
    public byte[] getHash() {
        return this.hash;
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
}
