package net.redstoneore.chat.struct;

/**
 * Listening can be obfuscated with distances. So there is the option for hearing to be set to
 * NONE (can not hear), HEAR (can hear clearly), SPY (spying on the channel) or MUMBLE (deliver an
 * obfuscated message).
 */
public enum HearType {

	NONE,
	CLEAR,
	SPY,
	MUMBLE,
	
	;
	
}
