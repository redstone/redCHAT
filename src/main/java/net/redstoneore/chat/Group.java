package net.redstoneore.chat;

import org.bukkit.plugin.java.JavaPlugin;

import net.redstoneore.chat.plugin.RedChat;

public class Group {
	
	// -------------------------------------------------- //
	// DEFAULT GROUPS
	// -------------------------------------------------- //
	
	public static final Group NETWORK = new Group(RedChat.get(), "NETWORK");
	public static final Group DISCORD = new Group(RedChat.get(), "DISCORD");
	public static final Group IRC = new Group(RedChat.get(), "IRC");
	
	// -------------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------------- //
	
	public Group(JavaPlugin provider, String name) {
		this.provider = provider;
		this.name = name;
	}
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	private JavaPlugin provider;
	private String name;
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //

	/**
	 * Returns the provider of this group.
	 * @return The {@link JavaPlugin} that provides this group.
	 */
	public JavaPlugin getProvider() {
		return this.provider;
	}
	
	/**
	 * Returns the group name.
	 * @return The group name.
	 */
	public String getName() {
		return this.name;
	}
	
}
