package net.redstoneore.chat.requirement;

import java.util.Optional;

import net.redstoneore.chat.Speaker;

public class RequirementPermission implements Requirement {
	
	// -------------------------------------------------- //
	// STATIC METHODS
	// -------------------------------------------------- //
	
	public static RequirementPermission create(String permission) {
		return new RequirementPermission(permission);
	}
	
	// -------------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------------- //
	
	private RequirementPermission(String permission) {
		this.permission = permission;
	}
	
	// -------------------------------------------------- //
	// FIELDS METHODS
	// -------------------------------------------------- //
	
	private final String permission;
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //

	@Override
	public Optional<RequirementType> getType() {
		return Optional.of(RequirementType.PERMISSION);
	}
	
	@Override
	public boolean has(Speaker speaker) {
		return speaker.hasPermission(this.permission);
	}
	
	public String getPermission() {
		return this.permission;
	}

}
