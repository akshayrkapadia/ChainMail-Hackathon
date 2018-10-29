package model;

import java.io.Serializable;

public interface IBlockChain extends Serializable {
	
	Block getHead();
	void setHead(Block block);
	Contact getContact();
	int getLength();
	void increaseLength();
	
	default void addBlock(Block block) {
		this.setHead(block);
		this.increaseLength();
	}
	
	default Block createGenesisBlock() {
		Block genesisBlock = new Block(0, "New Chat Started".getBytes(), this.getContact(), null, "Gensis Block".getBytes());
		return genesisBlock;
	}

}