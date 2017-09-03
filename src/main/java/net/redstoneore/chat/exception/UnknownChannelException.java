package net.redstoneore.chat.exception;

public class UnknownChannelException extends Exception {
	
	// -------------------------------------------------- //
	// STATIC FIELDS
	// -------------------------------------------------- //
	
	private static final long serialVersionUID = 3970943616003551950L;

	// -------------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------------- //
	
	public UnknownChannelException(String channelName) {
		this.channelName = channelName;
	}

	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	private final String channelName;

	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //

	public String getRequestedChannel() {
		return this.channelName;
	}

}
