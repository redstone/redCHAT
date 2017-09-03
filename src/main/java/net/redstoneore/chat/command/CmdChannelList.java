package net.redstoneore.chat.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.ChatColor;

import mkremins.fanciful.FancyMessage;
import net.redstoneore.chat.Channel;
import net.redstoneore.chat.Channels;
import net.redstoneore.chat.Speaker;

public class CmdChannelList extends RCommand<CmdChannel> {

	private static CmdChannelList instance = new CmdChannelList();
	public static CmdChannelList get() { return instance;} 
	
	private CmdChannelList() {
		this.aliases("chlist", "channellist");
		this.description("List all channels");
	}
	
	@Override
	void exec(Speaker speaker, Arguments arguments, RCommandCallback<Boolean> next) {
		List<Channel> in = new ArrayList<>();
		List<Channel> avab = new ArrayList<>();
		
		Channels.get().getAll().forEach(channel -> {
			if (channel.permission().isPresent() && !speaker.hasPermission(channel.permission().get())) {
				
			} else if (speaker.isListening(channel)) {
				in.add(channel);
			} else {
				avab.add(channel);
			}
		});
		
		if (in.isEmpty() && avab.isEmpty()) {
			speaker.sendMessage(ChatColor.GOLD + "There are no channels available for you.");
			next.then(true, Optional.empty());
			return;
		}
		
		speaker.sendMessage(ChatColor.GOLD + "Channels:");
			
		in.forEach(channel -> {
			FancyMessage fancyMessage = new FancyMessage("[leave] ").color(ChatColor.GREEN).command("/chleave " + channel.getName()).tooltip("Leave this channel")
				.then(channel.getName());
			
			speaker.sendMessage(fancyMessage);
		});
		
		avab.forEach(channel -> {
			FancyMessage fancyMessage = new FancyMessage("[listen] ").color(ChatColor.GREEN).command("/ch " + channel.getName()).tooltip("Listen to this channel")
				.then(channel.getName());
			
			speaker.sendMessage(fancyMessage);
		});
		
		next.then(true, Optional.empty());
	}

}
