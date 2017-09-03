package net.redstoneore.chat;

import java.util.Optional;
import java.util.Set;

import net.redstoneore.chat.plugin.RedChat;

public interface Channels {

	/**
	 * Get the {@link Channels} instance
	 * @return {@link Channels} instance
	 */
	public static Channels get() {
		return RedChat.get().getChannels();
	}
	
	/**
	 * Get channel by it's name
	 * @param name Channel name.
	 * @return an {@link Optional}
	 */
	Optional<Channel> get(String name);
	
	/**
	 * Add a channel
	 * @param channel Channel to add.
	 */
	void add(Channel channel);
	
	/**
	 * Remove a channel
	 * @param channel Channel to remove.
	 */
	void remove(Channel channel);
	
	/**
	 * Get all channels
	 * @return an unmodifiable list of all channels
	 */
	Set<Channel> getAll();
	
	
	/**
	 * Count of channels
	 * @return a count of channels
	 */
	int count();
	
	/**
	 * Load static channels.
	 */
	public void load();
	
}
