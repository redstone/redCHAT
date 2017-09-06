package net.redstoneore.chat.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.redstoneore.chat.Speaker;
import net.redstoneore.chat.util.RCallback;

/**
 * RCommand is a very useful utility for creating and executing commands. All commands are called
 * in async, so if you want to make calls the Bukkit API be sure to do so with a sync task.<br>
 * <br>
 * When the command is done executing call the {@link RCallback#then(Object, Optional)}
 * method. This will signal our command queue to call the next command. We do this to stop players
 * executing too many commands at one time. If a player goes offline the queue is cleared.<br>
 * <br>
 * To not call the next command in the queue simply pass false. This is not recommended unless you
 * are starting the queue up again later. 
 */
public abstract class RCommand<T extends RCommand<T>> {

	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	private List<String> aliases = new ArrayList<>();
	private String description = "";
	private String permission = null;
	private List<String> requiredArguments = new ArrayList<>();
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	public List<String> aliases() {
		return this.aliases;
	}
	
	public void aliases(String... aliases) {
		for (String alias : aliases) {
			this.aliases.add(alias.toLowerCase().trim());
		}
	}
	
	public boolean isAlias(String alias) {
		return this.aliases.contains(alias.toLowerCase().trim());
	}
	
	public String description() {
		return this.description;
	}
	
	public void description(String description) {
		this.description = description;
	}
	
	public void permission(String permission) {
		this.permission = permission;
	}
	
	public Optional<String> permission() {
		if (this.permission == null) return Optional.empty();
		return Optional.of(this.permission);
	}
	
	public void args(String...args) {
		for (String arg : args) {
			this.requiredArguments.add(arg);
		}
	}
	
	abstract void exec(final Speaker speaker, final Arguments arguments, final RCallback<Boolean> next);
	
}
