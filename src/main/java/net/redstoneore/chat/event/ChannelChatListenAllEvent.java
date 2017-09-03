package net.redstoneore.chat.event;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import net.redstoneore.chat.Speaker;

public class ChannelChatListenAllEvent  extends RedChatEvent<ChannelChatListenAllEvent> implements Cancellable {

	// -------------------------------------------------- //
	// STATIC FIELDS
	// -------------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	
	public static ChannelChatListenAllEvent create(ChannelChatInitiateEvent initiateEvent, Speaker spy) {
		return new ChannelChatListenAllEvent(initiateEvent, spy);
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
	
	public ChannelChatListenAllEvent(ChannelChatInitiateEvent initiateEvent, Speaker spy) {
		this.initiateEvent = initiateEvent;
		this.spy = spy;
	}
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	private final ChannelChatInitiateEvent initiateEvent;
	private final Speaker spy;
	private final AtomicBoolean cancelled = new AtomicBoolean(false);
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	public ChannelChatInitiateEvent getInitiateEvent() {
		return this.initiateEvent;
	}
	
	public Speaker getSpy() {
		return this.spy;
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
