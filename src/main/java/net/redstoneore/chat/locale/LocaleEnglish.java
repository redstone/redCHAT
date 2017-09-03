package net.redstoneore.chat.locale;

import net.redstoneore.chat.Speaker;

public class LocaleEnglish implements Locale {

	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	@Override
	public String getMutedMessage(Speaker speaker) {
		return "<red>You have been muted and can't talk";
	}

	@Override
	public String getNotChannelMessage(String channel) {
		return "<dark_aqua>" + channel + "<red> is not a channel that exists.";
	}
	
	@Override
	public String getChannelSet(String channel) {
		return "<green>Your now speaking on <dark_aqua>" + channel;
	}

	@Override
	public String getCantJoinNoPermission(String channel) {
		return "<red>You don't have permission to join <dark_aqua>" + channel;
	}


}
