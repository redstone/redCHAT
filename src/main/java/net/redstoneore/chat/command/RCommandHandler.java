package net.redstoneore.chat.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.event.Cancellable;

import com.google.common.collect.Lists;

import net.redstoneore.chat.Speaker;

public class RCommandHandler {

	private static Set<RCommand<?>> commands = Collections.newSetFromMap(new ConcurrentHashMap<RCommand<?>, Boolean>());
	
	public static void add(RCommand<?> command) {
		commands.add(command);
	}
	
	public static void process(Speaker commandSender, String line, Cancellable event) {
		if (line.startsWith("/")) {
			line = line.substring(1);
		}
		
		line = line.trim();
		
		final String alias;
		final List<String> args;
		
		if (line.indexOf(" ") > -1) {
			args = Lists.newArrayList(line.split(" "));
			
			alias = args.remove(0);
		} else {
			args = new ArrayList<String>();
			alias = line;
		}
		
		commands.forEach(command -> {
			if (command.isAlias(alias)) {
				event.setCancelled(true);
				RCommandExec commandExec = new RCommandExec(command, commandSender, new Arguments(args));
				RCommandQueue.get().add(commandSender.getId(), commandExec);
			}
		});
	}
}
