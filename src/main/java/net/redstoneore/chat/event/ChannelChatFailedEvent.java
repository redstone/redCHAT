package net.redstoneore.chat.event;

import org.bukkit.event.HandlerList;

import net.redstoneore.chat.Channel;
import net.redstoneore.chat.Speaker;

public class ChannelChatFailedEvent extends RedChatEvent<ChannelChatFailedEvent> {

	// -------------------------------------------------- //
	// STATIC FIELDS
	// -------------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	
	public static ChannelChatFailedEvent create(Reason reason, Channel channel, Speaker player, String message) {
		return new ChannelChatFailedEvent(reason, channel, player, message);
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
	
	public ChannelChatFailedEvent(Reason reason, Channel channel, Speaker player, String message) {
		this.reason = reason;
		this.channel = channel;
		this.player = player;
		this.message = message;
	}
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	private final Reason reason;
	private final Channel channel;
	private final Speaker player;
	private final String message;
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	public Reason getReason() {
		return this.reason;
	}
	
	public Channel getChannel() {
		return this.channel;
	}
	
	public Speaker getPlayer() {
		return this.player;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	// -------------------------------------------------- //
	// ENUM
	// -------------------------------------------------- //
	
	public enum Reason {
		
		/**
		 * Chat failed because the player was muted.
		 */
		MUTED,
		
		/**
		 * Chat failed because another plugin stopped it.
		 */
		PLUGIN,
		
		;
	}
	
}
