package net.redstoneore.chat.listener;

import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;

import net.redstoneore.chat.Speaker;
import net.redstoneore.chat.Speakers;
import net.redstoneore.chat.command.RCommandHandler;
import net.redstoneore.chat.command.RCommandQueue;
import net.redstoneore.chat.speaker.ConsoleSpeaker;

public class CommandListener extends AbstractListener<CommandListener> {
	
	// -------------------------------------------------- //
	// INSTANCE
	// -------------------------------------------------- //
	
	private static CommandListener instance = new CommandListener();
	public static CommandListener get() { return instance; }
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	/**
	 * Process a player command through RComand.
	 * @param event {@link PlayerCommandPreprocessEvent}.
	 */
	@EventHandler
	public void commandByPlayer(PlayerCommandPreprocessEvent event) { 
		this.commandBySpeaker(Speakers.get().get(event.getPlayer()), event.getMessage(), event);
	}
	
	/**
	 * Process a server command through RCommand.
	 * @param event {@link ServerCommandEvent}.
	 */
	@EventHandler
	public void commandByConsole(ServerCommandEvent event) {
		this.commandBySpeaker(ConsoleSpeaker.get(), event.getCommand(), event);
	}
	
	/**
	 * Process a command through RCommand.
	 * @param speaker Speaker.
	 * @param command Command to execute.
	 * @param event The {@link Cancellable} event. Can be null.
	 */
	public void commandBySpeaker(Speaker speaker, String command, Cancellable event) {
		RCommandHandler.process(speaker, command, event);
	}
	
	/**
	 * Clear the command queue for a player when they quit the game.
	 * @param event
	 */
	@EventHandler
	public void commandQueueClear(PlayerQuitEvent event) {
		Speaker speaker = Speakers.get().get(event.getPlayer());
		RCommandQueue.get().clear(speaker.getId());
	}
	
}
