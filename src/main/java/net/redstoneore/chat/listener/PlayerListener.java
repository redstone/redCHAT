package net.redstoneore.chat.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import net.redstoneore.chat.RLocation;
import net.redstoneore.chat.Speakers;
import net.redstoneore.chat.plugin.RedChat;
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
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(RedChat.get(), () -> 
			speaker.setLastLocation(RLocation.create(event.getPlayer().getLocation()))
		);
	}
	
	@EventHandler
	public void playerMove(PlayerMoveEvent event) {
		if (event.getTo().equals(event.getFrom())) return;
		
		RLocation to = RLocation.create(event.getTo());
		
		((PlayerSpeaker)Speakers.get().get(event.getPlayer())).setLastLocation(to);
	}
}
