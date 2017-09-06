package net.redstoneore.chat.event;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import net.redstoneore.chat.Channel;
import net.redstoneore.chat.Speaker;

public class ChannelListenEvent extends RedChatEvent<ChannelListenEvent> implements Cancellable {

	// -------------------------------------------------- //
	// STATIC FIELDS
	// -------------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	
	public static ChannelListenEvent create(Speaker speaker, Channel channel) {
		return new ChannelListenEvent(speaker, channel);
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
	
	public ChannelListenEvent(Speaker speaker, Channel channel) {
		this.speaker = speaker;
		this.channel = channel;
	}
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	private final Speaker speaker;
	private final Channel channel;
	private final AtomicBoolean cancelled = new AtomicBoolean(false);
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	public Speaker getSpeaker() {
		return this.speaker;
	}
	
	public Channel getChannel() {
		return this.channel;
	}
	
	@Override
	public boolean isCancelled() {
		return this.cancelled.get();
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled.set(cancel);
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
		
}
