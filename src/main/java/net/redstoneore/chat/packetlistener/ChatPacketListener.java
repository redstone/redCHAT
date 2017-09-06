package net.redstoneore.chat.packetlistener;

import java.util.UUID;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.redstoneore.chat.Speakers;
import net.redstoneore.chat.plugin.RedChat;
import net.redstoneore.chat.speaker.PlayerSpeaker;

public class ChatPacketListener extends AbstractPacketListener<ChatPacketListener> {

	// -------------------------------------------------- //
	// INSTANCE
	// -------------------------------------------------- //
	
	private static ChatPacketListener instance = new ChatPacketListener();
	public static ChatPacketListener get() { return instance; }
	
	// -------------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------------- //
	
	private ChatPacketListener() {
		super(ListenerPriority.MONITOR, new PacketType[] { PacketType.Play.Server.CHAT });
	}
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	@Override
	public void onPacketSending(PacketEvent event) {
		if (event.isCancelled() || event.getPacketType() != PacketType.Play.Server.CHAT) {
			return;
		}
		
		PacketContainer packet = event.getPacket();
		
		StructureModifier<WrappedChatComponent> chatComponents = packet.getChatComponents();
		WrappedChatComponent chatComponent = chatComponents.read(0);
		
		// TODO: is there ever a case this would be null?
		if (chatComponent == null) return;
		
		String message = chatComponent.getJson();
		
		if (message == null) return;
		
		String text = null;
		String colour = null;
		
		JsonObject json = RedChat.get().getGson().fromJson(message, JsonObject.class);
		JsonArray jsonArray = null;
		JsonObject redChatElement = null;
		
		// this part can fail silently, as it means it isn't a redCHAT meta message.
		try {
			jsonArray = json.getAsJsonArray("extra");
			redChatElement = jsonArray.get(jsonArray.size()-1).getAsJsonObject();
					
			text = redChatElement.getAsJsonPrimitive("text").getAsString();
			colour = redChatElement.getAsJsonPrimitive("color").getAsString();
		} catch (Exception e) {
			
		}
				
		PlayerSpeaker speaker = (PlayerSpeaker) Speakers.get().get(event.getPlayer());
		
		final UUID messageid;
		
		if (colour != null && colour.equalsIgnoreCase("red") && text != null && text.trim().startsWith(RedChat.REDCHAT_MSGID)) {
			// Is a redCHAT meta message identifier, this means it has an id attached to it already.
			messageid = UUID.fromString(text.split("/")[2]);
						
			// Okay lets remove our meta data
			jsonArray.remove(jsonArray.size()-1);
			json.remove("extra");
			json.add("extra", jsonArray);
			
			String newJson = RedChat.get().getGson().toJson(json);
								
			// Build it back into an array and set it as our chat component 
			chatComponent.setJson(newJson);
			
			// Write it back
			event.getPacket().getChatComponents().write(0, chatComponent);
			
			// Store the new message
			message = newJson;
			
		} else {
			// Not a redCHAT text message, this is still to be added to message history but because
			// it wasn't from RedChat we simply generated a random UUID for it.
			messageid = UUID.randomUUID();
		}
		
		speaker.addToMessageLog(messageid, message);
	}

}
