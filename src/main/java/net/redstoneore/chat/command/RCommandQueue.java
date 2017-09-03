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

public class RCommandQueue {

	// -------------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------------- //
	
	public static final int QUEUE_MAX = 5;
	
	// -------------------------------------------------- //
	// INSTANCE
	// -------------------------------------------------- //

	private static RCommandQueue instance = new RCommandQueue();
	public static RCommandQueue get() { return instance; }
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	private Map<UUID, Map<Long, RCommandExec>> commandQueue = new ConcurrentHashMap<UUID, Map<Long, RCommandExec>>();
	
	private Map<UUID, Set<UUID>> executing = new ConcurrentHashMap<UUID, Set<UUID>>();

	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	public void add(UUID player, RCommandExec commandExec) {
		if (!this.commandQueue.containsKey(player)) {
			this.commandQueue.put(player, new ConcurrentHashMap<Long, RCommandExec>());
		}
		
		this.commandQueue.get(player).put(System.currentTimeMillis(), commandExec);
		
		this.poll(player);
	}
	
	/**
	 * Poll the queue for this player
	 * @param player
	 */
	public void poll(UUID player) {
		Bukkit.getScheduler().runTaskAsynchronously(RedChat.get(), () -> {
			Optional<Entry<Long, RCommandExec>> found = this.commandQueue.get(player).entrySet().stream()
				.sorted((a,b) -> a.getKey().compareTo(b.getKey()))
				.findFirst();
			
			if (!found.isPresent()) return;
			
			final Long timestamp = found.get().getKey();
			RCommandExec commandExec = found.get().getValue();
			
			// Are we executing this command already?
			if (!this.executing.containsKey(player)) {
				this.executing.put(player, Collections.newSetFromMap(new ConcurrentHashMap<UUID, Boolean>()));
			} else {
				if (this.executing.get(player).contains(commandExec.getId())) {
					return;
				}
			}
			
			// No, add it as executing now.
			this.executing.get(player).add(commandExec.getId());
			
			commandExec.getCommand().exec(commandExec.getSender(), commandExec.getArguments(), (pass, e) -> {
				this.commandQueue.get(player).remove(timestamp);
				this.poll(player);
			});
			
		});
	}
	
}
