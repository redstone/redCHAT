package net.redstoneore.chat.util;

import org.bukkit.ChatColor;

public class TextUtil {

	public static String parse(String message) {
		for (ChatColor colour : ChatColor.values()) {
			message = message.replace("<" + colour.name().toLowerCase() + ">", colour+"");
		}
		return message;
	}
	
}
