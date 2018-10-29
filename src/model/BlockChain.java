package model;

public class BlockChain implements IBlockChain {
	
	private Block head;
	private Contact contact;
	private int length;
	
	public BlockChain(Contact contact) {
		this.head = this.createGenesisBlock();
		this.contact = contact;
		this.length = 0;
	}

	@Override
	public Block getHead() {
		return this.head;
	}

	@Override
	public void setHead(Block block) {
		this.head = block;
	}

	@Override
	public Contact getContact() {
		return this.contact;
	}

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return this.length;
	}

	@Override
	public void increaseLength() {
		// TODO Auto-generated method stub
		this.length++;
	}

}
