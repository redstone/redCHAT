package net.redstoneore.chat.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bukkit.scheduler.BukkitRunnable;

import net.redstoneore.chat.Channel;
import net.redstoneore.chat.Channels;
import net.redstoneore.chat.Speaker;
import net.redstoneore.chat.Speakers;
import net.redstoneore.chat.config.parts.PartChannelFormat;
import net.redstoneore.chat.exception.ChannelAlreadyExistsException;
import net.redstoneore.chat.plugin.RedChat;

/**
 * A temporary channel does not save to storage and only exists on runtime. Great for integrations
 * with other command and conquer plugins or minigame plugins.
 */
public class TemporaryChannel extends FlaggableChannel implements Channel {

	/**
	 * Create a TemporaryChannel.
	 * @param name Name of temporary channel.
	 * @param format Format for the channel.
	 * @return {@link TemporaryChannel}
	 * @throws ChannelAlreadyExistsException Thrown if channel already exists.
	 */
	public static TemporaryChannel create(String name, List<PartChannelFormat> format) throws ChannelAlreadyExistsException {
		TemporaryChannel channel = new TemporaryChannel(name); 
		channel.setFormat(format);
		return channel;
	}
	
	/**
	 * Create a TemporaryChannel with a random name.
	 * @param format Format for this channel.
	 * @return {@link TemporaryChannel}
	 */
	public static TemporaryChannel createRandom(List<PartChannelFormat> format) {
		TemporaryChannel channel;
		try {
			channel = new TemporaryChannel("");
		} catch (ChannelAlreadyExistsException e) {
			// Oops, try again
			return createRandom(format);
		} 
		
		return channel;
	}
	
	/**
	 * Create a self destructing channel that removes itself when it has no players for 60 seconds.
	 * @param name Name of channel.
	 * @param format Format for this channel.
	 * @param initials Initial listening speakers to add.
	 * @return {@link TemporaryChannel}
	 * @throws ChannelAlreadyExistsException
	 */
	public static TemporaryChannel createSelfDestructingChannel(String name, List<PartChannelFormat> format, Set<Speaker> initials) throws ChannelAlreadyExistsException {
		final TemporaryChannel channel = new TemporaryChannel(name); 
		channel.setFormat(format);
		
		initials.forEach(speaker -> speaker.addListening(channel));
		
		new BukkitRunnable() {

			// -------------------------------------------------- //
			// FIELDS
			// -------------------------------------------------- //
			
			private int secondsWithoutPlayers = 0;
			
			// -------------------------------------------------- //
			// METHODS
			// -------------------------------------------------- //
			
			@Override
			public void run() {
				// Cancel this event if this channel no longer exists.
				if (!Channels.get().getAll().stream()
					.filter(existingChannel -> existingChannel == channel)
					.findFirst()
					.isPresent()) { this.cancel(); }
				
				if (Speakers.get().getListening(channel).size() == 0) {
					secondsWithoutPlayers = secondsWithoutPlayers+5;
				}
				
				if (secondsWithoutPlayers >= 60) {
					Speakers.get().getAll(false).stream()
						.filter(speaker -> speaker.isListening(channel))
						.forEach(speaker -> speaker.removeListening(channel));
					
					this.cancel();
					Channels.get().remove(channel);
				}
			}
			
		}.runTaskTimerAsynchronously(RedChat.get(), 20, 20 * 5);
		
		return channel;
	}
	
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
	private boolean isDefaultJoin = false;
	private boolean isDefaultListen = false;
	
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
	
	/**
	 * Set the permission for this channel.
	 * @param permission Permission for this channel.
	 */
	public void permission(String permission) {
		this.permission = permission;
	}
	
	@Override
	public boolean isDefaultSpeaking() {
		return this.isDefaultJoin;
	}
	
	/**
	 * Set this channel as the default joi channel.
	 * @param isDefault Is default?
	 */
	public void setDefaultJoin(boolean defaultJoin) {
		this.isDefaultJoin = defaultJoin;
	}
	
	@Override
	public boolean isDefaultListening() {
		return this.isDefaultListen;
	}
	
	/**
	 * Set this channel as the default listen channel.
	 * @param defaultListen Is default?
	 */
	public void setDefaultListen(boolean defaultListen) {
		this.isDefaultListen = defaultListen;
	}
	
}
