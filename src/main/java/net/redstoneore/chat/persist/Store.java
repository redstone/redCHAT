package net.redstoneore.chat.persist;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

import net.redstoneore.chat.plugin.RedChat;

public interface Store<T> {
	
	static <T>T load(Class<T> what, Path path) {
		String rawJson = null;
		try {
				
			if (!Files.exists(path)) return null;
			
			SeekableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.READ);
			ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
			channel.read(buffer);
			
			rawJson = new String((byte[]) buffer.flip().array(), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		if (rawJson == null) return null;
		
		return RedChat.get().getGson().fromJson(rawJson, what);
	}
	
	Path path();
	
	default void save() {
		try {
			byte[] writeBytes = RedChat.get().getGson().toJson(this).getBytes();
			FileChannel channel = FileChannel.open(this.path(), EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE));
			ByteBuffer buffer = ByteBuffer.wrap(writeBytes);
			
			channel.force(false);
			channel.write(buffer);
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
