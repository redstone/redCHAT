package net.redstoneore.chat.config.parts;

import java.util.ArrayList;
import java.util.List;

public class PartChannel {

	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	private String channelName = null;
	private String channelDescription = null;
	private String permission = null;
	private List<PartChannelFormat> format = new ArrayList<>();
	private double hearDistance = -1d;
	private double mumbleDistance = -1d;
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	public String channelName() {
		return this.channelName;
	}
	
	public void channelName(String channelName) {
		this.channelName = channelName;
	}
	
	public String channelDescription() {
		return this.channelDescription;
	}
	
	public void channelDescription(String channelDescription) {
		this.channelDescription = channelDescription;
	}
	
	public boolean permissionExists() {
		return this.permission != null;
	}
	
	public String permission() {
		return this.permission;
	}
	
	public void permission(String permission) {
		this.permission = permission;
	}
	
	public List<PartChannelFormat> format() {
		return this.format;
	}
	
	public double hearDistance() {
		return this.hearDistance;
	}
	
	public void hearDistance(double hearDistance) {
		this.hearDistance = hearDistance;
	}
	
	public double mumbleDistance() {
		return this.mumbleDistance;
	}
	
	public void mumbleDistance(double mumbleDistance) {
		this.mumbleDistance = mumbleDistance;
	}
	
}
