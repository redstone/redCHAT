package net.redstoneore.chat.event;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.event.HandlerList;

import net.redstoneore.chat.Channel;
import net.redstoneore.chat.Speaker;

/**
 * A player can sometimes be in a channel but because of the distance they miss a message. This
 * event is fired when that happens. It can not be cancelled.
 *
 */
public class ChannelChatMissEvent extends RedChatEvent<ChannelChatMissEvent> {

	// -------------------------------------------------- //
	// STATIC FIELDS
	// -------------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	
	public static ChannelChatMissEvent create(Channel channel, Speaker player, Speaker sender, String message) {
		return new ChannelChatMissEvent(channel, player, sender, message);
	}
	
	// -------------------------------------------------- //
	// STATIC METHODS
	// -------------------------------------------------- //
		
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	// -------------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------------- //
	
	public ChannelChatMissEvent(Channel channel, Speaker player, Speaker sender, String message) {
		this.channel = channel;
		this.player = player;
		this.sender = sender;
		this.message = message;
	}
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	private final Channel channel;
	private final Speaker player;
	private final Speaker sender;
	private final String message;
	private final AtomicBoolean miss = new AtomicBoolean(true);
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	public Channel getChannel() {
		return this.channel;
	}
	
	public Speaker getPlayer() {
		return this.player;
	}
	
	public Speaker getSender() {
		return this.sender;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public boolean miss() {
		return this.miss.get();
	}
	
	public void miss(boolean miss){
		this.miss.set(miss);
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
		
}
