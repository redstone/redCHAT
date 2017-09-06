package net.redstoneore.chat.command;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.redstoneore.chat.Channel;
import net.redstoneore.chat.Channels;
import net.redstoneore.chat.exception.UnknownChannelException;
import net.redstoneore.chat.plugin.RedChat;
import net.redstoneore.chat.util.RCallback;
import net.redstoneore.chat.util.uuid.MojangUuidResolver;
import net.redstoneore.chat.util.uuid.UuidDisplayName;

public class Arguments {

	// -------------------------------------------------- //
	// STATIC METHODS
	// -------------------------------------------------- //
	
	public static Arguments of(List<String> arguments) {
		return new Arguments(arguments);
	}
	
	// -------------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------------- //
	
	public Arguments(List<String> arguments) {
		this.arguments = arguments;
	}
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	private final List<String> arguments;
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	/**
	 * Grab an argument as a type. This method accepts any (supported) type.
	 * @param index Index at, starts at 0.
	 * @param type Type to parse as.
	 * @param then Callback method with the result, or null, or null and an exception.
	 */
	@SuppressWarnings("unchecked")
	public <T>void get(int index, Class<T> type, RCallback<T> then) {
		if (this.arguments.size() < index + 1) {
			then.then(null, Optional.of(new NullPointerException("Argument not available")));
			return;
		}
		
		String stringValue = this.arguments.get(index);
		
		if (type == String.class) {
			then.then((T) stringValue, Optional.empty());
			return;
		}
		
		if (type == Double.class) {
			then.then((T) Double.valueOf(stringValue), Optional.empty());
			return;
		}
		
		if (type == Channel.class) {
			if (Channels.get().get(stringValue).isPresent()) {
				then.then((T) Channels.get().get(stringValue).get(), Optional.empty());
			} else {
				then.then(null, Optional.of(new UnknownChannelException(stringValue)));				
			}
			return;
		}
		
		if (type == Player.class) {
			Player found = null;
			
			found = Bukkit.getPlayer(stringValue);
			
			if (found != null) {
				then.then((T) found, Optional.empty());
				return;
			}
			
			Bukkit.getScheduler().runTaskAsynchronously(RedChat.get(), () -> {
				UuidDisplayName uuidDisplayName = MojangUuidResolver.get().resolve(stringValue);
				if (uuidDisplayName == null) {
					then.then(null, Optional.empty());
					return;
				}
				Player lookedUpPlayer = Bukkit.getPlayer(uuidDisplayName.getUuid());
				if (lookedUpPlayer == null) {
					then.then(null, Optional.empty());
					return;
				}
				
				then.then((T) lookedUpPlayer, Optional.empty());
			});
			
			return;
		}
		
		then.then(null, Optional.of(new NoSuchElementException("Type is not supported")));
		
	}
	
	public int size() {
		return this.arguments.size();
	}
	
}
