package net.redstoneore.chat.listener;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.redstoneore.chat.Speaker;
import net.redstoneore.chat.SpeakerFlag;
import net.redstoneore.chat.event.ChannelChatFailedEvent;
import net.redstoneore.chat.event.ChannelChatFailedEvent.Reason;
import net.redstoneore.chat.plugin.RedChat;

public class ChatListener extends AbstractListener<ChatListener> {

	// -------------------------------------------------- //
	// INSTANCE
	// -------------------------------------------------- //
	
	private static ChatListener instance = new ChatListener();
	public static ChatListener get() { return instance; }
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		// Store variables
		String message = event.getMessage();
		Speaker speaker = RedChat.get().getSpeakers().get(event.getPlayer());
		
		// Cancel event
		event.setCancelled(true);
		
		// Check if muted
		if (speaker.flag(SpeakerFlag.MUTE)) {
			// Muted, call event to notify
			ChannelChatFailedEvent.create(Reason.MUTED, speaker.getSpeakingChannel(), speaker, message).call();
			
			// Send message
			speaker.sendMessage(UUID.randomUUID(), RedChat.get().getLocale().getMutedMessage(speaker));
			return;
		}
		
		// Send to channel
		speaker.getSpeakingChannel().sendMessage(speaker, message);
	}
	
}
