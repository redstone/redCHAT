package net.redstoneore.chat;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;

import mkremins.fanciful.FancyMessage;

public interface Speaker {

	/**
	 * Get UUID of this speaker
	 * @return {@link UUID} of this player
	 */
	UUID getId();
	
	/**
	 * Get display name of this player
	 * @return The players display name.
	 */
	String getDisplayName();
	
	/**
	 * Check if a player has a permission
	 * @param permission Permission to check
	 * @return true if the player has this permission.
	 */
	boolean hasPermission(String permission);
	
	/**
	 * Send a text message to the speaker.
	 * @param messageID The id of this message.
	 * @param message Message to send.
	 */
	void sendMessage(UUID messageID, String message);
	
	/**
	 * Send a fancy message to the speaker.
	 * @param messageID The id of this message.
	 * @param message Message to send.
	 */
	void sendMessage(UUID messageID, FancyMessage message);
	
	/**
	 * Send a message to the speaker. The id is randomly created.
	 * @param message Message to send.
	 * @return {@link UUID} created for this message.
	 */
	default UUID sendMessage(String message) {
		UUID randomId = UUID.randomUUID();
		this.sendMessage(randomId, message);
		return randomId;
	}
	
	/**
	 * Send a message to the speaker. The id is randomly created.
	 * @param message Message to send.
	 * @return {@link UUID} created for this message.
	 */
	default UUID sendMessage(FancyMessage message) {
		UUID randomId = UUID.randomUUID();
		this.sendMessage(randomId, message);
		return randomId;
	}
	
	void flag(SpeakerFlag flag, boolean value);
	boolean flag(SpeakerFlag flag);
	
	Channel getSpeakingChannel();
	void setSpeakingChannel(Channel channel);
	
	List<Channel> getListeningChannels();
	boolean isListening(Channel channel);
	void addListening(Channel channel);
	
	boolean canHear(Channel channel, Speaker speaker);
	
	double getDistance(Speaker speaker);
	
	Location getLocation();
	
	void ignore(Speaker speaker, Boolean ignore);
	
	boolean ignoring(Speaker speaker);
	
	default UUID speak(String message) {
		return this.getSpeakingChannel().sendMessage(this, message);
	}
	
}
