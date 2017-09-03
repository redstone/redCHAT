package net.redstoneore.chat.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.redstoneore.chat.Speaker;

public abstract class RCommand<T> {

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
	
	abstract void exec(Speaker speaker, Arguments arguments, RCommandCallback<Boolean> next);
	
}
