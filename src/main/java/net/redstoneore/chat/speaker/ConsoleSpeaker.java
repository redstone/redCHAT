package net.redstoneore.chat.speaker;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import mkremins.fanciful.FancyMessage;
import net.redstoneore.chat.Channel;
import net.redstoneore.chat.Channels;
import net.redstoneore.chat.Speaker;
import net.redstoneore.chat.SpeakerFlag;
import net.redstoneore.chat.channel.ConsoleChannel;
import net.redstoneore.chat.config.Config;

public class ConsoleSpeaker implements Speaker {
	
	// -------------------------------------------------- //
	// INSTANCE
	// -------------------------------------------------- //
	
	private static ConsoleSpeaker instance = new ConsoleSpeaker();
	public static ConsoleSpeaker get() { return instance; }
	
	// -------------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------------- //
	
	private ConsoleSpeaker() { } 
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	// ..
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //

	@Override
	public UUID getId() {
		return Config.get().consoleId;
	}

	@Override
	public String getDisplayName() {
		return "@console";
	}

	@Override
	public boolean hasPermission(String permission) {
		return Bukkit.getConsoleSender().hasPermission(permission);
	}

	@Override
	public void sendMessage(UUID messageId, String message) {
		Bukkit.getConsoleSender().sendMessage(message);
	}
	
	@Override
	public void sendMessage(UUID messageId, FancyMessage message) {
		message.send(Bukkit.getConsoleSender());
	}
	
	@Override
	public void flag(SpeakerFlag flag, boolean value) {
		
	}

	@Override
	public boolean flag(SpeakerFlag flag) {
		return flag.getConsoleDefault();
	}

	@Override
	public Channel getSpeakingChannel() {
		return ConsoleChannel.get();
	}

	@Override
	public void setSpeakingChannel(Channel channel) {
		
	}
	
	@Override
	public List<Channel> getListeningChannels() {
		return Channels.get().getAll().stream().collect(Collectors.toList());
	}
	
	@Override
	public boolean isListening(Channel channel) {
		return true;
	}

	@Override
	public boolean canHear(Channel channel, Speaker speaker) {
		return true;
	}

	@Override
	public double getDistance(Speaker speaker) {
		return 1;
	}

	@Override
	public Location getLocation() {
		return null;
	}

	@Override
	public void ignore(Speaker speaker, Boolean ignore) {
		
	}

	@Override
	public boolean ignoring(Speaker speaker) {
		return false;
	}

	@Override
	public void addListening(Channel channel) {
		
	}
	
}
