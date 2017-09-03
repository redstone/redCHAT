package net.redstoneore.chat.listener;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import net.redstoneore.chat.plugin.RedChat;

public class AbstractListener<T> implements Listener {

	/**
	 * Enable this listener.
	 */
	public void enable() {
		RedChat.get().getServer().getPluginManager().registerEvents(this, RedChat.get());
	}
	
	/**
	 * Disable this listener.
	 */
	public void disable() {
		HandlerList.unregisterAll(this);
	}
	
}
