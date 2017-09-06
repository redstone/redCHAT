package net.redstoneore.chat.packetlistener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;

import net.redstoneore.chat.plugin.RedChat;

public abstract class AbstractPacketListener<T> extends PacketAdapter {

	// -------------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------------- //
	
	public AbstractPacketListener(ListenerPriority monitor, PacketType[] packetTypes) {
		super(RedChat.get(), monitor, packetTypes);
	}
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	/**
	 * Enable this packet listener.
	 * @return this instance, for chaining.
	 */
	@SuppressWarnings("unchecked")
	public T enable() {
		ProtocolLibrary.getProtocolManager().addPacketListener(this);
		return (T) this;
	}

}
