package net.redstoneore.chat.speaker;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.redstoneore.chat.Channel;
import net.redstoneore.chat.Speaker;
import net.redstoneore.chat.Speakers;
import net.redstoneore.chat.config.Config;

public class SpeakersInstance implements Speakers {

	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	private Map<UUID, Speaker> speakers = new ConcurrentHashMap<UUID, Speaker>();
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	@Override
	public Speaker get(UUID uuid) {
		if (uuid == null) return null;
		
		if (!this.speakers.containsKey(uuid)) {
			this.speakers.put(uuid, new PlayerSpeaker(uuid));
		}
		
		return this.speakers.get(uuid);
	}
	
	@Override
	public Speaker get(Player player) {
		if (player == null) return null;
		return this.get(player.getUniqueId());
	}

	@Override
	public Set<Speaker> getAll(boolean onlineOnly) {
		Set<Speaker> result = new HashSet<>();
		
		result.add(ConsoleSpeaker.get());
		
		result.addAll(
			Bukkit.getOnlinePlayers().stream()
				.map(this::get)
				.collect(Collectors.toSet()));
		
		if (!onlineOnly) {
			result.addAll(
				Arrays.asList(Bukkit.getOfflinePlayers()).stream()
					.map(offlinePlayer -> this.get(offlinePlayer.getUniqueId()))
					.collect(Collectors.toSet()));
		}
		
		return result;
	}

	@Override
	public Set<Speaker> getListening(Channel channel) {
		return this.getAll(true).stream()
			.filter(speaker -> speaker.isListening(channel))
			.collect(Collectors.toSet());
	}
	
	@Override
	public void load() {
		this.speakers = Config.get().speakers;
	}

}
