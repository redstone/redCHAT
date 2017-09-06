package net.redstoneore.chat.plugin;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.redstoneore.chat.Channels;
import net.redstoneore.chat.Speakers;
import net.redstoneore.chat.channel.ChannelsInstance;
import net.redstoneore.chat.channel.StaticChannel;
import net.redstoneore.chat.command.CmdChannel;
import net.redstoneore.chat.command.CmdChannelList;
import net.redstoneore.chat.command.RCommandHandler;
import net.redstoneore.chat.config.Config;
import net.redstoneore.chat.config.parts.PartChannelFormat;
import net.redstoneore.chat.config.parts.PartChannelFormatVisiblityConditions;
import net.redstoneore.chat.listener.AbstractListener;
import net.redstoneore.chat.listener.ChatListener;
import net.redstoneore.chat.listener.CommandListener;
import net.redstoneore.chat.listener.PlayerListener;
import net.redstoneore.chat.locale.Locale;
import net.redstoneore.chat.locale.LocaleEnglish;
import net.redstoneore.chat.speaker.SpeakersInstance;

public class RedChat extends JavaPlugin {
	
	// -------------------------------------------------- //
	// STATIC METHODS
	// -------------------------------------------------- // 
	
	public static RedChat get() {
		return RedChat.getPlugin(RedChat.class);
	}
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- // 
	
	private Channels channelsInstance = null;
	private Speakers speakersInstance = null;
	private Locale locale = new LocaleEnglish();
	private Gson gson = null;
	
	// -------------------------------------------------- //
	// ENABLE
	// -------------------------------------------------- // 
	
	@Override
	public void onEnable() {
		try {
			Files.createDirectories(this.getDataPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// load config and save it 
		Config.reload().save();
		
		// if there is no console id we have to setup the defaults
		if (Config.get().consoleId == null) {
			this.initiateDefaultConfig();
		}
		
		// Our or channels 
		Channels.get().load();
		
		// Register our listeners
		this.addListeners(
			ChatListener.get(),
			PlayerListener.get(),
			CommandListener.get()
		);
		
		// Add our commands
		RCommandHandler.add(CmdChannel.get());
		RCommandHandler.add(CmdChannelList.get());
		
		Bukkit.getOnlinePlayers().forEach(Speakers.get()::get);
		
		this.showBanner();
	}
	
	@Override
	public void onDisable() {
		Config.get().save();
	}
	
	// -------------------------------------------------- //
	// UTIL METHODS
	// -------------------------------------------------- // 
	
	/**
	 * Add listeners to RedChat
	 * @param listeners Listeners to add.
	 */
	public void addListeners(AbstractListener<?>... listeners) {
		for (AbstractListener<?> listener : listeners) {
			listener.enable();
		}
	}
	
	/**
	 * Get channels instance
	 * @return {@link Channels}
	 */
	public Channels getChannels() {
		if (this.channelsInstance == null) {
			this.channelsInstance = new ChannelsInstance();
		}
		return this.channelsInstance;
	}
	
	/**
	 * Get speakers instance
	 * @return {@link Speakers}
	 */
	public Speakers getSpeakers() {
		if (this.speakersInstance == null) {
			this.speakersInstance = new SpeakersInstance();
		}
		return this.speakersInstance;
	}
	
	/**
	 * Get the current locale 
	 * @return {@link Locale}
	 */
	public Locale getLocale() {
		return this.locale;
	}
	
	/**
	 * Create the default config
	 */
	public void initiateDefaultConfig() {
		Config.get().consoleId = UUID.randomUUID();
		
		// Add global channel
		StaticChannel globalChannel = new StaticChannel("global");
		globalChannel.setDescription("Default channel!");
		globalChannel.getFormat().addAll(Lists.newArrayList(
			PartChannelFormat.create("[%redchat_channel_name%] ").colour(ChatColor.GOLD),
			PartChannelFormat.create("<%redchat_player_name%> ").colour(ChatColor.WHITE).style(ChatColor.BOLD).tooltip(
				PartChannelFormat.create("Level %player_level%"),
				PartChannelFormat.create("Premium Member!").condition(
					PartChannelFormatVisiblityConditions.create()
						.permission("myserver.premium")
				)	
			),
			PartChannelFormat.create("%redchat_player_message%").colour(ChatColor.WHITE)
		));
		
		Config.get().channels.add(globalChannel);
		
		// Add nearby channel
		StaticChannel nearbyChannel = new StaticChannel("nearby");
		nearbyChannel.setDescription("Nearby players - players nearby in a distance of 50 blocks.");
		nearbyChannel.getFormat().addAll(Lists.newArrayList(
			PartChannelFormat.create("[%redchat_channel_name%] ").colour(ChatColor.GRAY),
			PartChannelFormat.create("<%redchat_player_name%> ").colour(ChatColor.WHITE).style(ChatColor.BOLD).tooltip(
				PartChannelFormat.create("Level %player_level%"),
				PartChannelFormat.create("Premium Member!").condition(
					PartChannelFormatVisiblityConditions.create()
						.permission("myserver.premium")
				)	
			),
			PartChannelFormat.create("%redchat_player_message%").colour(ChatColor.WHITE)
		));
		
		nearbyChannel.setHearDistance(40);
		nearbyChannel.setMumbleDistance(60);
		
		Config.get().channels.add(nearbyChannel);
		
		Config.get().save();
	}
	
	/**
	 * Sends the redCHAT banner to the console
	 */
	public void showBanner() {
		this.logPlain(ChatColor.RED + "                  ___________  _____  ______ ");
		this.logPlain(ChatColor.RED + "   ________  ____/ // ____/ / / /   |/_  __/ ");
		this.logPlain(ChatColor.RED + "  / ___/ _ \\/ __  // /   / /_/ / /| | / /    ");
		this.logPlain(ChatColor.RED + " / /  /  __/ /_/ // /___/ __  / ___ |/ /     ");
		this.logPlain(ChatColor.RED + "/_/   \\___/\\__,_//\\____/_/ /_/_/  |_/_/      ");
		if (this.getChannels().getAll().size() == 1) {
			this.log(this.getChannels().getAll().size() + " channel ready");
		} else {
			this.log(this.getChannels().getAll().size() + " channels ready");
		}
	}
	
	/**
	 * Log to console without prefix or colours.
	 * @param message Message to log.
	 */
	public void logPlain(String message) {
		Bukkit.getConsoleSender().sendMessage(message);
	}
	
	/**
	 * Log to console
	 * @param message Message to log.
	 */
	public void log(String message) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[redCHAT] " + ChatColor.WHITE + message);
	}
	
	/**
	 * Returns the data folder as a path.
	 * @return Data folder.
	 */
	public Path getDataPath() {
		return Paths.get(this.getDataFolder().getAbsolutePath());
	}
	
	/**
	 * Get Gson instance for RedChat
	 * @return {@link Gson} instance
	 */
	public Gson getGson() {
		if (this.gson == null) {
			GsonBuilder gsonbuilder = new GsonBuilder();
			
			try {
				gsonbuilder.setLenient();
			} catch (Exception e) {
				
			}
			
			this.gson = gsonbuilder.disableHtmlEscaping().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE).create();
			
		}
		return this.gson;
	}
	
}
