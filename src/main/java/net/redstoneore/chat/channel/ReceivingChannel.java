package net.redstoneore.chat.channel;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import mkremins.fanciful.FancyMessage;

import net.redstoneore.chat.Channel;
import net.redstoneore.chat.Speaker;
import net.redstoneore.chat.SpeakerFlag;
import net.redstoneore.chat.Speakers;
import net.redstoneore.chat.config.parts.PartChannelFormat;
import net.redstoneore.chat.event.ChannelChatInitiateEvent;
import net.redstoneore.chat.event.ChannelChatMissEvent;
import net.redstoneore.chat.event.ChannelChatListenAllEvent;
import net.redstoneore.chat.plugin.RedChat;
import net.redstoneore.chat.util.PlaceholderUtil;

public abstract class ReceivingChannel implements Channel {

	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	public Long createdAt = System.currentTimeMillis();
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- // 
	
	@Override
	public UUID sendMessage(Speaker sender, String message) {		
		if (sender.flag(SpeakerFlag.MUTE)) {
			sender.sendMessage(RedChat.get().getLocale().getMutedMessage(sender));
			return null;
		}
		
		final Set<Speaker> recipients = Collections.newSetFromMap(new ConcurrentHashMap<Speaker, Boolean>());
		final Set<Speaker> missers = new HashSet<Speaker>();
		
		// Get all listening to this channel
		Speakers.get().getListening(this).forEach(receiver -> {
			if (receiver.canHear(sender.getSpeakingChannel(), sender)) {
				recipients.add(receiver);
			} else {
				missers.add(sender);
			}
		});
		
		ChannelChatInitiateEvent event = ChannelChatInitiateEvent.create(sender.getSpeakingChannel(), sender, message, sender.getSpeakingChannel().getFormat(), recipients).call();
		if (event.isCancelled()) return null;

		String determinedMessage = event.getMessage();
		List<PartChannelFormat> determinedFormat = event.getFormat();
		Set<Speaker> determinedRecipients = event.getRecipients();
		
		// Let's send the miss event.
		missers.forEach(misser -> {			
			// Double check, another plugin may have added them back.
			if (!determinedRecipients.contains(misser)) {
				// Send the event.
				if (!ChannelChatMissEvent.create(this, misser, sender, message).call().miss()) {
					// The event doesn't want us to miss them anymore, add them back.
					determinedRecipients.add(misser);
				}
			}
		});
		
		final Set<Speaker> spying = Collections.newSetFromMap(new ConcurrentHashMap<Speaker, Boolean>());
		
		// Find players with the LISTEN_ALL flag that aren't in this channel - make them spy on this channel
		Speakers.get().getAll(true).forEach(receiver -> {
			if (!receiver.getListeningChannels().contains(this) && receiver.flag(SpeakerFlag.LISTEN_ALL)) {
				if (!ChannelChatListenAllEvent.create(event, receiver).call().isCancelled()) {
					spying.add(receiver);
				}
			}
		});
		
		// Add the spying players to the determined listeners
		determinedRecipients.addAll(spying);
		
		// Generate a message id
		UUID messageId = UUID.randomUUID();
		
		// Finally, tell the players
		determinedRecipients.forEach(receiver -> {
			FancyMessage createdMessage = new FancyMessage("");
			
			determinedFormat.forEach(part -> 
				part.append(sender, receiver, createdMessage, PlaceholderUtil.parse(sender, receiver, PartChannelFormat.format(part.text, sender, determinedMessage)))
			);
			
			receiver.sendMessage(messageId, createdMessage);
		});
		
		return messageId;
	}
	
	@Override
	public Long getCreatedAt() {
		return this.createdAt;
	}
	
	@Override
	public int compareTo(Channel o) {
		if (o.getName() == this.getName()) {
			return 0;
		}
		
		return this.getCreatedAt().compareTo(o.getCreatedAt());
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ReceivingChannel)) return false;
		
		ReceivingChannel channel = (ReceivingChannel) other;
		
		return channel.getName() == this.getName();
	}
	
}
