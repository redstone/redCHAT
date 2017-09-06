package net.redstoneore.chat.util;

import java.util.Random;

import org.bukkit.ChatColor;

/**
 * This class provides some utilities for text.
 */
public class TextUtil {

	/**
	 * Parse <colours> into {@link ChatColor}
	 * @param message Message to parse
	 * @return the parsed string
	 */
	public static String parse(String message) {
		for (ChatColor colour : ChatColor.values()) {
			message = message.replace("<" + colour.name().toLowerCase() + ">", colour+"");
		}
		return message;
	}
	
	/**
	 * Obfuscated a string.
	 * @param message Message to obfuscate.
	 * @return the string, obfuscated.
	 */
	public static String obfuscate(String message) {
		// Small messages are easy to decode, lets complicate it
		if (message.length() < 6) {
			return ".." + message.substring(0, 1+new Random().nextInt(2)) + ".?";
		}
		
		// Replace all existing dots with a question mark
		message = message.replace(".", "?");
		
		// Now obfuscate these existing symbols
		message = message
			.replace(",", ".")
			.replace(")", ".")
			.replace("(", "?")
			.replace(";", ".")
			.replace(":", "?")
			.replace("!", "?")
			.replace("#", "?")
			.replace("$", "?")
			.replace("*", "?")
			.replace("-", ".")
			.replace("'", ".");
		
		StringBuilder sb = new StringBuilder(message);
		
		double attempts = 0;
		while (attempts < message.length() / 2) {
			int next = new Random().nextInt(sb.length());
			sb.replace(next, next+1, ".");
			attempts++;
		}
		
		return sb.toString();
	}
	
}
