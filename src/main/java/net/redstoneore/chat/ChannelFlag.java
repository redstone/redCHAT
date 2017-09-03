package net.redstoneore.chat;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.plugin.java.JavaPlugin;

import net.redstoneore.chat.plugin.RedChat;

public class ChannelFlag {

	// -------------------------------------------------- //
	// STATIC FIELDS
	// -------------------------------------------------- //

	public static Queue<ChannelFlag> registeredFlags = new ConcurrentLinkedQueue<>();
	
	// -------------------------------------------------- //
	// STATIC METHODS
	// -------------------------------------------------- //

	public static ChannelFlag create(JavaPlugin provider, long id, long priority, String name, Group group) {
		return new ChannelFlag(provider, id, priority, name, group);
	}
	
	public static ChannelFlag register(ChannelFlag flag) {
		registeredFlags.add(flag);
		return flag;
	}
	
	public static boolean unregister(ChannelFlag flag) {
		return registeredFlags.remove(flag);
	}
	
	public static Iterator<ChannelFlag> getAll() {
		return registeredFlags.iterator();
	}
	
	// -------------------------------------------------- //
	// DEFAULTS
	// -------------------------------------------------- //
	
	public static final ChannelFlag LOCAL = register(create(RedChat.get(), 100, 100, "local", Group.NETWORK));
	public static final ChannelFlag GLOBAL = register(create(RedChat.get(), 100, 100, "global", Group.NETWORK));
	
	public static final ChannelFlag DISCORD = register(create(RedChat.get(), 100, 100, "discord", Group.DISCORD));
	
	public static final ChannelFlag IRC = register(create(RedChat.get(), 100, 100, "irc", Group.IRC));
		
	// -------------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------------- //

	protected ChannelFlag(JavaPlugin provider, long id, long priority, String name, Group group) {
		this.provider = provider;
		this.id = id;
		this.priority = priority;
		this.group = group;
	}
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	private final JavaPlugin provider;
	private final long id;
	private final long priority;
	private final Group group;
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	public JavaPlugin getProvider() {
		return this.provider;
	}
	
	public long getId() {
		return this.id;
	}
	
	public long priority() {
		return this.priority;
	}
	
	public Group getGroup() {
		return this.group;
	}
	
	public String getStoreName() {
		return this.getProvider().getName().toLowerCase() + "_" + this.getId();
	}
	
}
