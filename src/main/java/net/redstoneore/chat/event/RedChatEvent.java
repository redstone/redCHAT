package net.redstoneore.chat.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public abstract class RedChatEvent<T> extends Event {

	@SuppressWarnings("unchecked")
	public T call() {
		Bukkit.getServer().getPluginManager().callEvent(this);
		return (T) this;
	}
	
}
