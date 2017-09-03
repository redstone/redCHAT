package net.redstoneore.chat;

import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.redstoneore.chat.plugin.RedChat;

public interface Speakers {

	// -------------------------------------------------- //
	// INSTANCE
	// -------------------------------------------------- //
	
	public static Speakers get() {
		return RedChat.get().getSpeakers();
	}
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	/**
	 * Get speaker by id.
	 * @param uuid Id of player.
	 * @return the speaker object for this player.
	 */
	Speaker get(UUID uuid);
	
	/**
	 * Get speaker by player.
	 * @param player Player.
	 * @return the speaker object for this player.
	 */
	Speaker get(Player player);
	
	/**
	 * Get all speakers by their online status.
	 * @param onlineOnly Should this be online only?
	 * @return a set containing all speakers.
	 */
	Set<Speaker> getAll(boolean onlineOnly);
	
	/**
	 * Get all speakers listening to a channel.
	 * @param channel Channel .
	 * @return a set containing all speakers.
	 */
	Set<Speaker> getListening(Channel channel);
	
	/**
	 * Load the speakers.
	 */
	void load();
	
}
