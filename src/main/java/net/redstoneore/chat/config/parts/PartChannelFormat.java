package net.redstoneore.chat.config.parts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import mkremins.fanciful.FancyMessage;
import net.redstoneore.chat.Speaker;
import net.redstoneore.chat.util.PlaceholderUtil;

public class PartChannelFormat {

	// -------------------------------------------------- //
	// STATIC METHODS
	// -------------------------------------------------- //
	
	public static PartChannelFormat create() {
		return new PartChannelFormat();
	}
	
	public static PartChannelFormat create(String message) {
		return new PartChannelFormat().text(message);
	}
	
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	public String text = "";
	public ChatColor colour = null;
	public List<ChatColor> style = new ArrayList<>();
	public String command = null;
	public List<PartChannelFormat> tooltip = new ArrayList<>();
	public List<PartChannelFormatVisiblityConditions> displayConditions = new ArrayList<>();
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	public FancyMessage getFancyMessage(Speaker sender, Speaker receiver, String text, Boolean isTooltip) {
		FancyMessage message = new FancyMessage(text);
		return this.addStyle(sender, receiver, message, isTooltip);
	}
	
	public FancyMessage getFancyMessage(Speaker sender, Speaker receiver, String text) {
		return this.getFancyMessage(sender, receiver, text, false);
	}
	
	public FancyMessage append(Speaker sender, Speaker receiver, FancyMessage message, String text) {
		message.then(text);
		return this.addStyle(sender, receiver, message);
	}
	
	public FancyMessage addStyle(Speaker sender, Speaker receiver, FancyMessage message) {
		return this.addStyle(sender, receiver, message, false);
	}
	
	public FancyMessage addStyle(Speaker sender, Speaker receiver, FancyMessage message, Boolean isTooltip) {
		if (this.colour != null) {
			message.color(this.colour);
		}
		if (this.style != null && !this.style.isEmpty()) {
			this.style.forEach(style -> message.style(style));
		}
		if (this.command != null) {
			message.command(command);
		}
		
		if (this.tooltip != null && this.tooltip.size() > 0 && !isTooltip) {
			List<FancyMessage> tooltipLines = new ArrayList<>();
			
			this.tooltip.forEach(tooltip -> {
				tooltipLines.add(tooltip.getFancyMessage(sender, receiver, PlaceholderUtil.parse(sender, receiver, tooltip.text), true));
			});
		}
		return message;

	}
	
	public FancyMessage getFancyMessage(Speaker sender, Speaker receiver) {
		return this.getFancyMessage(sender, receiver, this.text);
	}
	
	public List<PartChannelFormat> getTooltip() {
		return this.tooltip;
	}
	
	public PartChannelFormat text(String text) {
		this.text = text;
		return this;
	}
	
	public PartChannelFormat colour(ChatColor colour) {
		this.colour = colour;
		return this;
	}
	
	public PartChannelFormat style(ChatColor style) {
		this.style.add(style);
		return this;
	}
	
	public PartChannelFormat command(String command) {
		this.command = command;
		return this;
	}
	
	public PartChannelFormat tooltip(PartChannelFormat... tooltips) {
		for (PartChannelFormat tooltip : tooltips) {
			this.tooltip.add(tooltip);
		}
		return this;
	}
	
	public PartChannelFormat condition(PartChannelFormatVisiblityConditions condition) {
		this.displayConditions.add(condition);
		return this;
	}
	
	public static String format(String string, Speaker sender, String message) {
		return string
			.replace("%redchat_channel_name%", sender.getSpeakingChannel().getName())
			.replace("%redchat_player_name%", sender.getDisplayName())
			.replace("%redchat_player_message%", message)
			;
	}
	
}
