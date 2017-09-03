package net.redstoneore.chat.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.redstoneore.chat.Channel;
import net.redstoneore.chat.config.parts.PartChannelFormat;
import net.redstoneore.chat.exception.ChannelAlreadyExistsException;
import net.redstoneore.chat.plugin.RedChat;

public class TemporaryChannel extends FlaggableChannel implements Channel {

	// -------------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------------- //

	protected TemporaryChannel() { }
	
	public TemporaryChannel(String name) throws ChannelAlreadyExistsException {
		if (RedChat.get().getChannels().get(name).isPresent()) {
			throw new ChannelAlreadyExistsException(name);
		}
		this.channelName = name;
	}
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //

	private String channelName;
	private String channelDescription;
	private List<PartChannelFormat> channelFormat = new ArrayList<>();
	private double hearDistance = -1d;
	private double mumbleDistance = -1d;
	private String permission = null;
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	@Override
	public String getName() {
		return this.channelName;
	}

	@Override
	public String getDescription() {
		return this.channelDescription;
	}

	@Override
	public List<PartChannelFormat> getFormat() {
		return this.channelFormat;
	}

	@Override
	public void setName(String name) {
		this.channelDescription = name;
	}

	@Override
	public void setDescription(String description) {
		this.channelDescription = description;
	}

	@Override
	public void setFormat(List<PartChannelFormat> format) {
		this.channelFormat = format;
	}

	@Override
	public void setHearDistance(double distance) {
		this.hearDistance = distance;
	}

	@Override
	public double getHearDistance() {
		return this.hearDistance;
	}

	@Override
	public void setMumbleDistance(double distance) {
		this.mumbleDistance = distance;
	}

	@Override
	public double getMumbleDistance() {
		return this.mumbleDistance;
	}

	@Override
	public Optional<String> permission() {
		return Optional.ofNullable(this.permission);
	}
	
	public void permission(String permission) {
		this.permission = permission;
	}
	
}
