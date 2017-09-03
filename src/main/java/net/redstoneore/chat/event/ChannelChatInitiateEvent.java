package net.redstoneore.chat.event;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import net.redstoneore.chat.Channel;
import net.redstoneore.chat.Speaker;
import net.redstoneore.chat.config.parts.PartChannelFormat;

public class ChannelChatInitiateEvent extends RedChatEvent<ChannelChatInitiateEvent> implements Cancellable {

	// -------------------------------------------------- //
	// STATIC FIELDS
	// -------------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	
	public static ChannelChatInitiateEvent create(Channel channel, Speaker player, String message, List<PartChannelFormat> format, Set<Speaker> recipients) {
		return new ChannelChatInitiateEvent(channel, player, message, format, recipients);
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
	
	public ChannelChatInitiateEvent(Channel channel, Speaker player, String message, List<PartChannelFormat> format, Set<Speaker> recipients) {
		this.channel = channel;
		this.player = player;
		this.message = message;
		this.format = format;
		this.recipients = recipients;
	}
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	private final Channel channel;
	private final Speaker player;
	private String message;
	private List<PartChannelFormat> format;
	private final Set<Speaker> recipients;
	private final AtomicBoolean cancelled = new AtomicBoolean(false);
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	public Channel getChannel() {
		return this.channel;
	}
	
	public Speaker getPlayer() {
		return this.player;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public List<PartChannelFormat> getFormat() {
		return this.format;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setFormat(List<PartChannelFormat> format) {
		this.format = format;
	}
	
	public Set<Speaker> getRecipients() {
		return this.recipients;
	}
	
	public void recipientAdd(Speaker speaker) {
		this.recipients.add(speaker);
	}
	
	public void recipientRemove(Speaker speaker) {
		this.recipients.remove(speaker);
	}
	
	public void recipientRemoveAll() {
		this.recipients.clear();
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
