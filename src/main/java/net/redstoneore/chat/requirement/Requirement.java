package net.redstoneore.chat.requirement;

import java.util.Optional;

import net.redstoneore.chat.Speaker;

public interface Requirement {
	
	Optional<RequirementType> getType();
	
	boolean has(Speaker speaker);
	
}
