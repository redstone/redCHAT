package net.redstoneore.chat.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;

import net.redstoneore.chat.Speaker;
import net.redstoneore.chat.channel.StaticChannel;
import net.redstoneore.chat.persist.Store;
import net.redstoneore.chat.plugin.RedChat;

public class Config implements Store<Config> {

	// -------------------------------------------------- //
	// STATIC FIELDS
	// -------------------------------------------------- //
	
	private static Config instance = null;
	
	// -------------------------------------------------- //
	// STATIC METHODS
	// -------------------------------------------------- //
	
	public static Config get() {
		if (instance == null) return reload();
		return instance;
	}
	
	public static Config reload() {
		instance = Store.load(Config.class, getPath());
		if (instance == null) instance = new Config();
		
		return instance;
	}
	
	public static Path getPath() {
		return Paths.get(RedChat.get().getDataPath().toString(), "redchat.json");
	}
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	public UUID consoleId = null;
	
	public boolean discord;
	
	public String mumbleMessagePrefix = ChatColor.GRAY.toString();

	public String defaultChannel = "global";
		
	public Set<StaticChannel> channels = Collections.newSetFromMap(new ConcurrentHashMap<StaticChannel, Boolean>());
	
	public Map<UUID, Speaker> speakers = new ConcurrentHashMap<UUID, Speaker>();
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	@Override
	public Path path() {
		return getPath();
	}

}
