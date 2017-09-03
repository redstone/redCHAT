package net.redstoneore.chat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.redstoneore.chat.config.parts.PartChannelFormat;

public interface Channel extends Comparable<Channel> {

	/**
	 * Get the channel name.
	 * @return The name of this channel.
	 */
	String getName();
	
	/**
	 * Get the channel description.
	 * @return The description of this channel.
	 */
	String getDescription();
	
	/**
	 * Get the channel format.
	 * @return This channel format for this channel.
	 */
	List<PartChannelFormat> getFormat();
	
	/**
	 * Set the channel name.
	 * @param name New name.
	 */
	void setName(String name);
	
	/**
	 * Set the channel description.
	 * @param description New description.
	 */
	void setDescription(String description);
	
	/**
	 * Set the chat format for this channel.
	 * @param format New format.
	 */
	void setFormat(List<PartChannelFormat> format);
	
	/**
	 * Add a flag to this channel, this removes any that are in the same flag group.
	 * @param flag Flag to add.
	 */
	void flagAdd(ChannelFlag flag);
	
	/**
	 * Remove a flag from this channel.
	 * @param flag Flag to remove.
	 */
	void flagRemove(ChannelFlag flag);
	
	/**
	 * Confirm if a flag is present in this channel.
	 * @param flag Flag to check.
	 * @return true if the flag is present.
	 */
	boolean flagExists(ChannelFlag flag);
	
	/**
	 * Returns an option which will contain the permission for the channel, if it has a permission.
	 * @return {@link Optional} with permission, if it has one.
	 */
	Optional<String> permission();
	
	/**
	 * Set the hearing distance for this channel.
	 * @param distance Hearing distance.
	 */
	void setHearDistance(double distance);
	
	/**
	 * Get the hearing distance.
	 * @return Hearing distance.
	 */
	double getHearDistance();
	
	/**
	 * Set the mumble distance for this channel, hear distance takes priority.
	 * @param distance Mumble distance.
	 */
	void setMumbleDistance(double distance);
	
	/**
	 * Get the mumble distance.
	 * @return Mumble distance.
	 */
	double getMumbleDistance();
		
	/**
	 * Send a message to this channel.
	 * @param speaker {@link Speaker} that is sending the message.
	 * @param message The message to send.
	 */
	UUID sendMessage(Speaker speaker, String message);
	
	/**
	 * A timestamp of when this channel was created.
	 * @return timestamp of when this channel was created.
	 */
	Long getCreatedAt();
	
}
