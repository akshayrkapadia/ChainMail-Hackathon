package model;

public class BlockChain implements IBlockChain {

    private Block head;
    private Contact contact;
    private int length;

    public BlockChain(Contact contact) {
        this.contact = contact;
        this.head = this.createGenesisBlock(contact);
        this.length = 1;
    }

    @Override
    public Block getHead() {
        return this.head;
    }

    @Override
    public Contact getContact() {
        return this.contact;
    }

    @Override
    public int getLength() {
        return this.length;
    }

    @Override
    public void setHead(Block newHead) {
        this.head = newHead;
    }

    @Override
    public void increaseLength() {
        this.length++;
    }
}
