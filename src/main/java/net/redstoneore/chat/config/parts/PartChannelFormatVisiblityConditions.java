package net.redstoneore.chat.config.parts;

import net.redstoneore.chat.Speaker;

public class PartChannelFormatVisiblityConditions {

	public static PartChannelFormatVisiblityConditions create() {
		return new PartChannelFormatVisiblityConditions();
	}
	
	public String permission = "";
	
	public PartChannelFormatVisiblityConditions permission(String permission) {
		this.permission = permission;
		return this;
	}
	
	public boolean isMet(Speaker speaker) {
		if (this.permission == null && this.permission != "" && !speaker.hasPermission(permission)) {
			return false;
		}
		
		return true;
	}
			
}
