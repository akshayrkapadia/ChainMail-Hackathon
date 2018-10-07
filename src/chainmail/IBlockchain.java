package chainmail;

public interface IBlockchain {
	
	Block getHead();
	void setHead(Block block);
	Contact getContact();
	
	default void addBlock(Block block) {
		block.setNext(this.getHead());
		block.setPreviousHash(this.getHead().hash());
		this.setHead(block);
	}
	
	default Block createGenesisBlock() {
		Block genesisBlock = new Block(0, "".getBytes(), null);
		return genesisBlock;
	}
	
	default int length() {
		if (this.getHead().getNext() == null) {
			return 1;
		} else {
			return 1 + this.length(this.getHead().getNext());
		}
	}
	
	default int length(Block block) {
		if (block.getNext() == null) {
			return 1;
		} else {
			return 1 + this.length(block.getNext());
		}
	}

}
