package chainmail;

public class Blockchain implements IBlockchain {
	
	private Block head;
	
	public Blockchain(Block head) {
		this.head = this.createGenesisBlock();
	}

	@Override
	public Block getHead() {
		return this.head;
	}

	@Override
	public void setHead(Block block) {
		this.head = block;
	}

}
