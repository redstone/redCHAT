package net.redstoneore.chat.locale;

import net.redstoneore.chat.Speaker;

public interface Locale {

	String getMutedMessage(Speaker speaker);
	
	String getNotChannelMessage(String channel);
	
	String getChannelSet(String channel);
	
	String getCantJoinNoPermission(String channel);
	
}
