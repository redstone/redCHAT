package net.redstoneore.chat.command;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;

import net.redstoneore.chat.plugin.RedChat;

/**
 * This class represents a queue of commands for the players. 
 */
public class RCommandQueue {

	// -------------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------------- //
	
	/**
	 * The maximum amount of commands to have in the queue at one time. This realistically could
	 * take more than 5 but to stop exploiting the queue we set a small limit.
	 */
	public static final int QUEUE_MAX = 5;
	
	// -------------------------------------------------- //
	// INSTANCE
	// -------------------------------------------------- //

	private static RCommandQueue instance = new RCommandQueue();
	public static RCommandQueue get() { return instance; }
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	private Map<UUID, Map<Long, RCommandExec>> commandQueue = new ConcurrentHashMap<>();
	
	private Map<UUID, Set<UUID>> executing = new ConcurrentHashMap<>();

	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	/**
	 * Add a command to the command queue.
	 * @param player Player to add for.
	 * @param commandExec Command to execute.
	 */
	public void add(UUID player, RCommandExec commandExec) {
		if (!this.commandQueue.containsKey(player)) {
			this.commandQueue.put(player, new ConcurrentHashMap<>());
		}
		
		// Don't add any more if we're already over the limit.
		if (this.commandQueue.get(player).size() >= QUEUE_MAX) return;
		
		// Add to the queue
		this.commandQueue.get(player).put(System.currentTimeMillis(), commandExec);
		
		// Poll
		this.poll(player);
	}
	
	/**
	 * Poll the queue for this player.
	 * @param player Player to poll.
	 */
	public void poll(UUID player) {
		Bukkit.getScheduler().runTaskAsynchronously(RedChat.get(), () -> {
			// Check for items in the queue, sort by timestamp
			Optional<Entry<Long, RCommandExec>> found = this.commandQueue.get(player).entrySet().stream()
				.sorted((a,b) -> a.getKey().compareTo(b.getKey()))
				.findFirst();
			
			// Nothing?
			if (!found.isPresent()) return;
			
			final Long timestamp = found.get().getKey();
			RCommandExec commandExec = found.get().getValue();
			
			// Are we executing this command already?
			if (!this.executing.containsKey(player)) {
				this.executing.put(player, Collections.newSetFromMap(new ConcurrentHashMap<>()));
			} else {
				if (this.executing.get(player).contains(commandExec.getId())) {
					return;
				}
			}
			
			// No, add it as executing now.
			this.executing.get(player).add(commandExec.getId());
			
			commandExec.getCommand().exec(commandExec.getSender(), commandExec.getArguments(), (continueQueue, e) -> {
				this.commandQueue.get(player).remove(timestamp);
				
				if (continueQueue) {
					this.poll(player);
				}
			});
			
		});
	}
	
	/**
	 * Clear the command queue for a player.
	 * @param player Player to clear for.
	 */
	public void clear(UUID player) {
		if (!this.executing.containsKey(player)) return;
		this.executing.get(player).clear();
	}
	
}
