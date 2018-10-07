package chainmail;

public class Blockchain implements IBlockchain {
	
	private Block head;
	private Contact contact;
	
	public Blockchain(Contact recipient) {
		this.head = this.createGenesisBlock();
		this.contact = contact;
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

}
