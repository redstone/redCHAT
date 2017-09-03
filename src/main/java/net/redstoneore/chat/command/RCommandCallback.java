package net.redstoneore.chat.command;

import java.util.Optional;

@FunctionalInterface
public interface RCommandCallback<T> {

	public void then(T value, Optional<Throwable> e);
	
}
