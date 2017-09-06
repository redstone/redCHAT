package net.redstoneore.chat.channel;

import java.util.List;
import java.util.Optional;

import net.redstoneore.chat.Channel;
import net.redstoneore.chat.config.parts.PartChannel;
import net.redstoneore.chat.config.parts.PartChannelFormat;

public class StaticChannel extends FlaggableChannel implements Channel {

	// -------------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------------- //

	public StaticChannel(String name) {
		this.configuration.channelName(name);
	}
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	public PartChannel configuration = new PartChannel();
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	@Override
	public String getName() {
		return this.configuration.channelName();
	}

	@Override
	public String getDescription() {
		return this.configuration.channelDescription();
	}

	@Override
	public List<PartChannelFormat> getFormat() {
		return this.configuration.format();
	}

	@Override
	public void setName(String name) {
		this.configuration.channelName(name);
	}

	@Override
	public void setDescription(String description) {
		this.configuration.channelDescription(description);
	}

	@Override
	public void setFormat(List<PartChannelFormat> format) {
		this.configuration.format().clear();
		this.configuration.format().addAll(format);
	}


	@Override
	public Optional<String> permission() {
		if (!this.configuration.permissionExists()) return Optional.empty();
		return Optional.of(configuration.permission());
	}
	
	@Override
	public void setHearDistance(double distance) {
		this.configuration.hearDistance(distance);
	}

	@Override
	public double getHearDistance() {
		return this.configuration.hearDistance();
	}

	@Override
	public void setMumbleDistance(double distance) {
		this.configuration.mumbleDistance(distance);
	}

	@Override
	public double getMumbleDistance() {
		return this.configuration.mumbleDistance();
	}
	
	@Override
	public boolean isDefaultSpeaking() {
		return this.configuration.isDefaultJoin();
	}
	
	@Override
	public boolean isDefaultListening() {
		return this.configuration.isDefaultListen();
	}
		
}
