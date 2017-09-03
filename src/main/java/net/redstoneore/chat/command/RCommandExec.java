package net.redstoneore.chat.command;

import java.util.UUID;

import net.redstoneore.chat.Speaker;

public class RCommandExec {

	public RCommandExec(RCommand<?> command, Speaker sender, Arguments arguments) {
		this.command = command;
		this.sender = sender;
		this.arguments = arguments;
	}
	
	private RCommand<?> command;
	private Speaker sender;
	private Arguments arguments;
	private UUID id = UUID.randomUUID();
	
	public RCommand<?> getCommand() {
		return this.command;
	}
	
	public Speaker getSender() {
		return this.sender;
	}
	
	public Arguments getArguments() {
		return this.arguments;
	}
	
	public UUID getId() {
		return this.id;
	}
	
}
