package net.redstoneore.chat.command;

import java.util.Optional;

import net.redstoneore.chat.Channel;
import net.redstoneore.chat.Speaker;
import net.redstoneore.chat.event.ChannelListenEvent;
import net.redstoneore.chat.plugin.RedChat;
import net.redstoneore.chat.util.RCallback;
import net.redstoneore.chat.util.TextUtil;

public class CmdChannel extends RCommand<CmdChannel> {

	// -------------------------------------------------- //
	// INSTANCE
	// -------------------------------------------------- //
	
	private static CmdChannel instance = new CmdChannel();
	public static CmdChannel get() { return instance;} 
	
	private CmdChannel() {
		this.aliases("ch", "channel");
		this.description("Switch the channel to speak on");
		
		this.args("name");
	}
	
	@Override
	void exec(final Speaker speaker, final Arguments arguments, final RCallback<Boolean> next) {
		// check for arguments! 
		if (arguments.size() == 0) {
			// TODO: better error message
			speaker.sendMessage(TextUtil.parse(RedChat.get().getLocale().getNotChannelMessage("nothing")));
			next.then(true, Optional.empty());
			return;
		}
		
		// Grab the channel...
		arguments.get(0, Channel.class, (channel, e) -> {
			// ... and it's string form
			arguments.get(0, String.class, (channelName, e2) -> {
				if (channel == null) {
					// Channel does not exist
					speaker.sendMessage(TextUtil.parse(RedChat.get().getLocale().getNotChannelMessage(channelName)));
				} else {
					// See if we're already not listening to this channel
					if (!speaker.isListening(channel)) {
						// Check we have permission
						if (channel.permission().isPresent() && !speaker.hasPermission(channel.permission().get())) {
							// TODO: error message
							next.then(true, Optional.empty());
							return;
						}
					}
					
					// If we get this far we're allowed to switch to listen to this channel
					if (ChannelListenEvent.create(speaker, channel).call().isCancelled()) {
						next.then(true, Optional.empty());
						return;
					}
					
					// Lets switch.
					speaker.addListening(channel);
					speaker.setSpeakingChannel(channel);
					speaker.sendMessage(TextUtil.parse(RedChat.get().getLocale().getChannelSet(channelName)));
				}
				next.then(true, Optional.empty());
			});
		});
	}

}
