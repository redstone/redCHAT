package net.redstoneore.chat;

import java.util.List;
import java.util.UUID;

import mkremins.fanciful.FancyMessage;
import net.redstoneore.chat.struct.HearType;

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
	
	/**
	 * Set a flag
	 * @param flag Flag to set.
	 * @param value Value of flag.
	 */
	void flag(SpeakerFlag flag, boolean value);
	
	/**
	 * Get a flag.
	 * @param flag Flag to get.
	 * @return The value of this flag, if it is not set it will return the default for this speaker
	 *         type.
	 */
	boolean flag(SpeakerFlag flag);
	
	/**
	 * Get the current speaking channel for this {@link Speaker}.
	 * @return The current speaking channel.
	 */
	Channel getSpeakingChannel();
	
	/**
	 * Set the current speaking channel for this {@link Speaker}.
	 * @param channel The channel to speak on.
	 */
	void setSpeakingChannel(Channel channel);
	
	/**
	 * Get the current listening channels for this {@link Speaker}. Can be none.
	 * @return A list with current speaking channels. Can be empty (none).
	 */
	List<Channel> getListeningChannels();
	
	/**
	 * Confirm if this speaker is listening to a channel.
	 * @param channel Channel to check.
	 * @return true if they are listening to this channel.
	 */
	boolean isListening(Channel channel);
	
	/**
	 * Add this speaker as listening to a channel.
	 * @param channel Channel it listen on.
	 */
	void addListening(Channel channel);
	
	/**
	 * Removes this speaker as a listening to a channel.
	 * @param channel Channel to stop listening on.
	 */
	void removeListening(Channel channel);
	
	/**
	 * Can this speaker hear an other speaker on a channel.
	 * @param channel Channel to compare for.
	 * @param speaker Speaker to check for.
	 * @return true if this speaker can hear other speaker.
	 */
	HearType canHear(Channel channel, Speaker speaker);
	
	/**
	 * Get the distance to other speaker. If it is a console speaker, it is considered at the same
	 * spot.
	 * @param speaker Speaker to compare to.
	 * @return distance
	 */
	double getDistance(Speaker speaker);
	
	/**
	 * Get the location of this speaker.
	 * @return The location. If it is a console speaker, it is considered at spawn of this first
	 *         world.
	 */
	RLocation getLocation();
	
	/**
	 * Toggle ignore on another speaker.
	 * @param speaker Speaker to ignore, or un-ignore.
	 * @param ignore Ignore status. true to ignore, false to un-ignore.
	 */
	void ignore(Speaker speaker, Boolean ignore);
	
	/**
	 * Confirm if a speaker is ignoring another speaker.
	 * @param speaker Speaker to check against.
	 * @return true if we're ignoring speaker.
	 */
	boolean ignoring(Speaker speaker);
	
	/**
	 * Speak on the current speaking channel!
	 * @param message Message to speak.
	 * @return
	 */
	default UUID speak(String message) {
		return this.getSpeakingChannel().sendMessage(this, message);
	}
	
}
