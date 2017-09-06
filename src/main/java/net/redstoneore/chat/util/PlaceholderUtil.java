package net.redstoneore.chat.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.redstoneore.chat.Speaker;

public class PlaceholderUtil {

	public static String parse(Speaker sender, Speaker receiver, String message) {
		Player playerSender = Bukkit.getPlayer(sender.getId());
		Player playerReceiver = Bukkit.getPlayer(receiver.getId());
		
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			message = parsePlaceholderAPI(playerSender, playerReceiver, message);
		}
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
			message = parseMVdWPlaceholderAPI(playerSender, playerReceiver, message);
		}
		
		return message;
	}
	
	private static String parsePlaceholderAPI(Player sender, Player receiver, String message) {
		message =  me.clip.placeholderapi.PlaceholderAPI.setRelationalPlaceholders(sender, receiver, message);
		message =  me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(sender, message);
		return message;
	}
	
	private static String parseMVdWPlaceholderAPI(Player sender, Player receiver, String message) {
		return be.maximvdw.placeholderapi.PlaceholderAPI.replacePlaceholders(sender, message);
	}
	
}
