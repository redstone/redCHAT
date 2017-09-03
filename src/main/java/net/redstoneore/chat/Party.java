package net.redstoneore.chat;

import java.util.List;

public interface Party extends Channel {

	Speaker getOwner();
	
	List<Speaker> getMembers();
	
	void add(Speaker add);
	
	void remove(Speaker add);
	
}
