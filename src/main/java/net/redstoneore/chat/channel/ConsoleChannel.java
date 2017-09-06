package net.redstoneore.chat.channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.google.common.collect.Lists;

import net.redstoneore.chat.Channel;
import net.redstoneore.chat.ChannelFlag;
import net.redstoneore.chat.Speaker;
import net.redstoneore.chat.config.parts.PartChannelFormat;
import net.redstoneore.chat.plugin.RedChat;

public class ConsoleChannel implements Channel {

	// -------------------------------------------------- //
	// INSTANCE
	// -------------------------------------------------- //
	
	private static ConsoleChannel instance = new ConsoleChannel();
	public static ConsoleChannel get() { return instance; }
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	public Long createdAt = System.currentTimeMillis();
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	@Override
	public String getName() {
		return "redCHAT Console";
	}

	@Override
	public String getDescription() {
		return "Console for red chat";
	}

	@Override
	public List<PartChannelFormat> getFormat() {
		return Lists.newArrayList(PartChannelFormat.create().text("%redchat_message%"));
	}

	@Override
	public void setName(String name) {
		
	}

	@Override
	public void setDescription(String description) {
		
	}

	@Override
	public void setFormat(List<PartChannelFormat> format) {
		
	}

	@Override
	public void flagAdd(ChannelFlag flag) {
		
	}

	@Override
	public void flagRemove(ChannelFlag flag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean flagExists(ChannelFlag flag) {
		return false;
	}

	@Override
	public void setHearDistance(double distance) {
		
	}

	@Override
	public double getHearDistance() {
		return 0;
	}

	@Override
	public void setMumbleDistance(double distance) {
		
	}

	@Override
	public double getMumbleDistance() {
		return 0;
	}

	@Override
	public UUID sendMessage(Speaker speaker, String message) {
		RedChat.get().log(message);
		return null;
	}

	@Override
	public int compareTo(Channel o) {
		return 1;
	}

	@Override
	public Long getCreatedAt() {
		return this.createdAt;
	}

	@Override
	public Optional<String> permission() {
		return Optional.of("redchat.console");
	}

	@Override
	public boolean isDefaultSpeaking() {
		return false;
	}

	@Override
	public boolean isDefaultListening() {
		return false;
	}
	
}
