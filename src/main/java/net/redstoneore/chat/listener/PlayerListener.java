package net.redstoneore.chat.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;

import net.redstoneore.chat.Speakers;
import net.redstoneore.chat.speaker.PlayerSpeaker;

public class PlayerListener extends AbstractListener<PlayerListener> {

	// -------------------------------------------------- //
	// INSTANCE
	// -------------------------------------------------- //

	private static PlayerListener instance = new PlayerListener();
	public static PlayerListener get() { return instance ;}
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	@EventHandler
	public void playerJoin(PlayerLoginEvent event) {
		PlayerSpeaker speaker = ((PlayerSpeaker) Speakers.get().get(event.getPlayer()));
		speaker.onLogin(event.getPlayer());
	}
	
}
