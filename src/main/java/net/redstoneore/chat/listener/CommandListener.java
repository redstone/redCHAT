package net.redstoneore.chat.listener;

import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import net.redstoneore.chat.Speaker;
import net.redstoneore.chat.Speakers;
import net.redstoneore.chat.command.RCommandHandler;
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
	
	@EventHandler
	public void commandByPlayer(PlayerCommandPreprocessEvent event) { 
		this.commandBySpeaker(Speakers.get().get(event.getPlayer()), event.getMessage(), event);
	}
	
	@EventHandler
	public void commandByConsole(ServerCommandEvent event) {
		this.commandBySpeaker(ConsoleSpeaker.get(), event.getCommand(), event);
	}
	
	public void commandBySpeaker(Speaker speaker, String command, Cancellable event) {
		RCommandHandler.process(speaker, command, event);
	}
	
}
