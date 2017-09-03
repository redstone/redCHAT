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
		this.part.channelName(name);
	}
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	public PartChannel part = new PartChannel();
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	@Override
	public String getName() {
		return this.part.channelName();
	}

	@Override
	public String getDescription() {
		return this.part.channelDescription();
	}

	@Override
	public List<PartChannelFormat> getFormat() {
		return this.part.format();
	}

	@Override
	public void setName(String name) {
		this.part.channelName(name);
	}

	@Override
	public void setDescription(String description) {
		this.part.channelDescription(description);
	}

	@Override
	public void setFormat(List<PartChannelFormat> format) {
		this.part.format().clear();
		this.part.format().addAll(format);
	}


	@Override
	public Optional<String> permission() {
		if (!this.part.permissionExists()) return Optional.empty();
		return Optional.of(part.permission());
	}
	
	@Override
	public void setHearDistance(double distance) {
		this.part.hearDistance(distance);
	}

	@Override
	public double getHearDistance() {
		return this.part.hearDistance();
	}

	@Override
	public void setMumbleDistance(double distance) {
		this.part.mumbleDistance(distance);
	}

	@Override
	public double getMumbleDistance() {
		return this.part.mumbleDistance();
	}
		
}
