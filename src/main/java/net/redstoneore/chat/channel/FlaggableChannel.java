package net.redstoneore.chat.channel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.redstoneore.chat.ChannelFlag;

public abstract class FlaggableChannel extends ReceivingChannel  {
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	public List<ChannelFlag> existingFlags = new ArrayList<>();

	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	@Override
	public void flagAdd(ChannelFlag flag) {
		// Remove any flags in the same group
		Iterator<ChannelFlag> iterator = this.existingFlags.iterator();
		iterator.forEachRemaining(existingFlag -> {
			if (existingFlag.getGroup() == flag.getGroup()) {
				iterator.remove();
			}
		});
		// Add this flag
		this.existingFlags.add(flag);
	}
	
	@Override
	public void flagRemove(ChannelFlag flag) {
		this.existingFlags.remove(flag);
	}

	@Override
	public boolean flagExists(ChannelFlag flag) {
		return this.existingFlags.contains(flag);
	}

}
