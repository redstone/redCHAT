package net.redstoneore.chat.util;

import java.util.Optional;

@FunctionalInterface
public interface RCallback<T> {

	public void then(T value, Optional<Throwable> e);
	
}
