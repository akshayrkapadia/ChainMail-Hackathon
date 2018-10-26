package model;

import java.io.Serializable;

public interface IBlockChain extends Serializable {

    Block getHead();
    Contact getContact();
    int getLength();
    void setHead(Block newHead);
    void increaseLength();

    default Block createGenesisBlock(Contact contact) {
        return new Block(0, "Genesis Block".getBytes(), null, contact);
    }

    default void addBlock(Block newBlock) {
        this.setHead(newBlock);
        this.increaseLength();
    }


}
