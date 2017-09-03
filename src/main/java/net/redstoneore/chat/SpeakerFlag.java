package net.redstoneore.chat;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.plugin.java.JavaPlugin;

import net.redstoneore.chat.plugin.RedChat;

public class SpeakerFlag {


	// -------------------------------------------------- //
	// STATIC FIELDS
	// -------------------------------------------------- //

	public static Queue<SpeakerFlag> registeredFlags = new ConcurrentLinkedQueue<>();
	
	// -------------------------------------------------- //
	// STATIC METHODS
	// -------------------------------------------------- //

	public static SpeakerFlag create(JavaPlugin provider, long id, long priority, String name, boolean playerDefault, boolean consoleDefault) {
		return new SpeakerFlag(provider, id, priority, name, playerDefault, consoleDefault);
	}
	
	public static SpeakerFlag register(SpeakerFlag flag) {
		registeredFlags.add(flag);
		return flag;
	}
	
	public static boolean unregister(SpeakerFlag flag) {
		return registeredFlags.remove(flag);
	}
	
	public static Iterator<SpeakerFlag> getAll() {
		return registeredFlags.iterator();
	}
	
	// -------------------------------------------------- //
	// DEFAULTS
	// -------------------------------------------------- //
	
	public static final SpeakerFlag MUTE = register(create(RedChat.get(), 100, 100, "mute", false, false));
	public static final SpeakerFlag LISTEN_ALL = register(create(RedChat.get(), 100, 100, "listen_all", false , true));
	
	// -------------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------------- //

	protected SpeakerFlag(JavaPlugin provider, long id, long priority, String name, boolean playerDefault, boolean consoleDefault) {
		this.provider = provider;
		this.id = id;
		this.priority = priority;
		this.playerDefault = playerDefault;
		this.consoleDefault = consoleDefault;
	}
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	private final JavaPlugin provider;
	private final long id;
	private final long priority;
	private final boolean playerDefault;
	private final boolean consoleDefault;
	
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
	
	public boolean getPlayerDefault() {
		return this.playerDefault;
	}
	
	public boolean getConsoleDefault() {
		return this.consoleDefault;
	}
	
	public String getStoreName() {
		return this.getProvider().getName().toLowerCase() + "_" + this.getId();
	}
	
}
