package net.redstoneore.chat.channel;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.redstoneore.chat.Channel;
import net.redstoneore.chat.Channels;
import net.redstoneore.chat.config.Config;

public class ChannelsInstance implements Channels {

	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	public Set<Channel> channels = Collections.newSetFromMap(new ConcurrentHashMap<Channel, Boolean>());
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	@Override
	public Optional<Channel> get(String name) {
		return channels.stream().filter(channel -> channel.getName().equalsIgnoreCase(name)).findFirst();
	}

	@Override
	public void add(Channel channel) {
		if (this.channels.contains(channel)) return;
		this.channels.add(channel);
		
		// Static channels are added to the config and trigger a save
		if (channel instanceof StaticChannel) {
			Config.get().channels.add((StaticChannel) channel);
			Config.get().save();
		}
	}
	
	@Override
	public void remove(Channel channel) {
		if (!this.channels.contains(channel)) return;
		this.channels.remove(channel);
	}

	@Override
	public Set<Channel> getAll() {
		return Collections.unmodifiableSet(this.channels);
	}
	
	@Override
	public int count() {
		return this.channels.size();
	}

	@Override
	public void load() {
		// Iterate over all static channels and remove them
		Iterator<Channel> iterator = this.channels.iterator();
		while (iterator.hasNext()) {
			Channel channel = iterator.next();
			if (channel instanceof StaticChannel) {
				iterator.remove();
			}
		}
		
		// Now reload from the config
		Config.get().channels.forEach(channel -> this.channels.add(channel));
	}

}
