package chainmail;

public interface IBlockchain {
	
	Block getHead();
	void setHead(Block block);
	
	default void addBlock(Block block) {
		this.setHead(block);
	}
	
	default Block createGenesisBlock() {
		Block genesisBlock = new Block(0, null, "Genesis Block", null);
		return genesisBlock;
	}

}
