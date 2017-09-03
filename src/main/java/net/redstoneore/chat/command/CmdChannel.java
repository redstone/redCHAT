package net.redstoneore.chat.command;

import java.util.Optional;

import net.redstoneore.chat.Channel;
import net.redstoneore.chat.Speaker;
import net.redstoneore.chat.plugin.RedChat;
import net.redstoneore.chat.util.TextUtil;

public class CmdChannel extends RCommand<CmdChannel> {

	private static CmdChannel instance = new CmdChannel();
	public static CmdChannel get() { return instance;} 
	
	private CmdChannel() {
		this.aliases("ch", "channel");
		this.description("Switch the channel to speak on");
		
		this.args("name");
	}
	
	@Override
	void exec(Speaker speaker, Arguments arguments, RCommandCallback<Boolean> next) {
		if (arguments.size() == 0) {
			speaker.sendMessage(TextUtil.parse(RedChat.get().getLocale().getNotChannelMessage("nothing")));
			next.then(true, Optional.empty());
			return;
		}
		
		arguments.get(0, Channel.class, (channel, e) -> {
			arguments.get(0, String.class, (channelName, e2) -> {
				if (channel == null) {
					speaker.sendMessage(TextUtil.parse(RedChat.get().getLocale().getNotChannelMessage(channelName)));
				} else {
					if (!speaker.isListening(channel)) {
						if (channel.permission().isPresent() && !speaker.hasPermission(channel.permission().get())) {
							next.then(true, Optional.empty());
							return;
						}
					}
					speaker.addListening(channel);
					speaker.setSpeakingChannel(channel);
					speaker.sendMessage(TextUtil.parse(RedChat.get().getLocale().getChannelSet(channelName)));
				}
				next.then(true, Optional.empty());
			});
		});
	}

}
