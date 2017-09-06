package net.redstoneore.chat.speaker;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mkremins.fanciful.FancyMessage;
import net.redstoneore.chat.Channel;
import net.redstoneore.chat.Channels;
import net.redstoneore.chat.RLocation;
import net.redstoneore.chat.Speaker;
import net.redstoneore.chat.SpeakerFlag;
import net.redstoneore.chat.config.Config;
import net.redstoneore.chat.plugin.RedChat;
import net.redstoneore.chat.struct.HearType;

public class PlayerSpeaker implements Speaker {
	
	// -------------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------------- //
	
	public PlayerSpeaker(PlayerSpeaker other) {
		this.id = other.id;
		this.name = other.name;
	}
	
	public PlayerSpeaker(Player other) {
		this.id = other.getUniqueId();
		this.name = other.getName();
	}
	
	public PlayerSpeaker(UUID id) {
		this.id = id;
	}
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //

	private UUID id;
	private String name;
	private Map<String, Boolean> flags = new ConcurrentHashMap<String, Boolean>();
	private String speakingChannel = "global";
	private Set<String> listeningChannels = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	
	private Set<String> ignores = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	
	private transient Player player = null;
	private transient RLocation lastKnownLocation = null;
	
	private AtomicBoolean firstJoin = new AtomicBoolean(true);
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	@Override
	public UUID getId() {
		return this.id;
	}

	@Override
	public String getDisplayName() {
		if (player == null) return this.name;
		return player.getDisplayName();
	}

	@Override
	public boolean hasPermission(String permission) {
		if (this.player == null) return false;
		return this.player.hasPermission(permission);
	}

	@Override
	public void sendMessage(UUID messageId, String message) {
		if (this.player == null) return;
		
		if (!Bukkit.isPrimaryThread()) {
			Bukkit.getScheduler().runTask(RedChat.get(), () -> this.player.sendMessage(message));
		} else {
			this.player.sendMessage(message);
		}
	}
	
	@Override
	public void sendMessage(UUID messageId, FancyMessage message) {
		if (!Bukkit.isPrimaryThread()) {
			Bukkit.getScheduler().runTask(RedChat.get(), () -> message.send(this.player));
		} else {
			message.send(this.player);
		}
	}
	
	@Override
	public void flag(SpeakerFlag flag, boolean value) {
		this.flags.put(flag.getStoreName(), value);
	}

	@Override
	public boolean flag(SpeakerFlag flag) {
		if (!this.flags.containsKey(flag.getStoreName())) return flag.getPlayerDefault();
		return this.flags.get(flag.getStoreName());
	}

	@Override
	public Channel getSpeakingChannel() {
		return Channels.get().get(this.speakingChannel).isPresent() ? Channels.get().get(this.speakingChannel).get() : Channels.get().get("global").get();
	}
	
	@Override
	public void setSpeakingChannel(Channel channel) {
		this.speakingChannel = channel.getName();
	}

	@Override
	public List<Channel> getListeningChannels() {
		return this.listeningChannels.stream()
			.map(name -> Channels.get().get(name).isPresent() ? Channels.get().get(name).get() : null)
			.filter(channel -> channel != null)
			.collect(Collectors.toList());
	}
	
	@Override
	public boolean isListening(Channel channel) {
		return this.listeningChannels.stream()
			.filter(listening -> listening == channel.getName())
			.findFirst()
			.isPresent();
	}
	
	@Override
	public void addListening(Channel channel) {
		this.listeningChannels.add(channel.getName());
	}
	
	@Override
	public void removeListening(Channel channel) {
		this.listeningChannels.remove(channel.getName());
	}
	
	@Override
	public void ignore(Speaker speaker, Boolean ignore) {
		if (ignore && !this.ignores.contains(speaker.getId().toString())) {
			this.ignores.add(speaker.getId().toString());
		}
		if (!ignore && this.ignores.contains(speaker.getId().toString())) {
			this.ignores.remove(speaker.getId().toString());
		}
	}
	
	public boolean ignoring(Speaker speaker) {
		return this.ignores.contains(speaker.getId().toString());
	}

	@Override
	public HearType canHear(Channel channel, Speaker speaker) {
		// Check the player is here
		if (this.player == null || !this.player.isOnline()) return HearType.NONE;
		
		if (speaker.getId() == this.getId()) return HearType.CLEAR;
				
		if (this.ignoring(speaker)) return HearType.NONE;
		
		if (this.flag(SpeakerFlag.LISTEN_ALL)) return HearType.CLEAR;

		if (!this.isListening(channel)) return HearType.NONE;
		
		if (channel.getHearDistance() > 0 || channel.getMumbleDistance() > 0) {
			if (channel.getHearDistance() > 0 && speaker.getDistance(this) <= channel.getHearDistance()) {
				// in hear distance
				return HearType.CLEAR;
			}
			
			if (channel.getMumbleDistance() > 0 && speaker.getDistance(this) <= channel.getMumbleDistance()) {
				// in mumble distance
				return HearType.MUMBLE;
			} 
			
			return HearType.NONE;
		}
		
		return HearType.CLEAR;
	}

	@Override
	public double getDistance(Speaker speaker) {
		if (speaker.getLocation() == null) return 1d;
		
		return speaker.getLocation().distance(this.getLocation());
	}

	@Override
	public RLocation getLocation() {
		if (this.lastKnownLocation == null) {
			if (Bukkit.isPrimaryThread() && this.player != null) {
				this.lastKnownLocation = RLocation.create(this.player.getLocation());
			}
			
			return null;
		}
		
		return this.lastKnownLocation;
	}
	
	/**
	 * Player only method to populate this object with the players information.
	 * @param player Player to populate.
	 */
	public void onLogin(Player player) {
		this.id = player.getUniqueId();
		this.name = player.getName();
		this.player = player;
		
		if (Channels.get().get(Config.get().defaultChannel).isPresent()) {
			Channel defaultChannel = Channels.get().get(Config.get().defaultChannel).get();
			
			if (!this.isListening(defaultChannel)) {
				this.addListening(defaultChannel);
			}
		}
		
		if (this.firstJoin.get()) {
			this.firstJoin.set(false);
			
			// First join, add to defaults.
			Channels.get().getAll().forEach(channel -> {
				if (channel.isDefaultSpeaking() || channel.isDefaultListening()) {
					// This is a default speaking or listening, so listen here.
					this.addListening(channel);
				}
				if (channel.isDefaultSpeaking()) {
					// This is a default speaking channel, so speak here.
					this.setSpeakingChannel(channel);
				}
			});
		}
	}
	
	/**
	 * To make it safer for async methods we store the last location in the safe {@link RLocation}.
	 * @param lastLocation
	 */
	public void setLastLocation(RLocation lastLocation) {
		this.lastKnownLocation = lastLocation;
	}
	
}
