package net.redstoneore.chat.exception;

public class ChannelAlreadyExistsException extends Exception {
	
	// -------------------------------------------------- //
	// STATIC FIELDS
	// -------------------------------------------------- //
	
	private static final long serialVersionUID = 7025712998449201966L;

	// -------------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------------- //
	
	public ChannelAlreadyExistsException(String requestedName) {
		this.requestedName = requestedName;
	}

	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	private final String requestedName;

	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //

	public String getRequestedName() {
		return this.requestedName;
	}

}
