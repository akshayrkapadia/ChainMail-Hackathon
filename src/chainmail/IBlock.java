package chainmail;

import java.io.Serializable;
import java.util.Date;

public interface IBlock extends Serializable {
	
	String getPreviousHash();
	Date getTimestamp();
	Block getNext();
	byte[] getMessage();
	int getIndex();

}
