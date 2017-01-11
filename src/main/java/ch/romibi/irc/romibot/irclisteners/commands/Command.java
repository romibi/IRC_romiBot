package ch.romibi.irc.romibot.irclisteners.commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.ircclouds.irc.api.IRCApi;
import com.ircclouds.irc.api.domain.IRCUser;

import ch.romibi.irc.romibot.irclisteners.CommandListener;
import ch.romibi.irc.romibot.irclisteners.commands.data.CommandInfo;

abstract public class Command {
	CommandListener commandlistener;
	
	public Command(CommandListener listener) {
		listener.register(this);
		commandlistener = listener;
	}
	
	public boolean accept(String text, String answerTo, IRCUser source, IRCApi api) {
		List<String> words = new LinkedList<String>(Arrays.asList(text.split(" ")));
		String command = words.get(0);
		words.remove(0);
		if(matches(command)) {
			return issueCommand(command, words, answerTo, source, api);
		}
		return false;
	}
	
	private boolean matches(String command) {
		for (CommandInfo commandinfo : getCommands()) {
			if(commandinfo.matches(command)) return true;
		}
		return false;
	}
	
	public CommandInfo getCommand() {
		return getCommand("");
	}

	public CommandInfo getCommand(String name) {
		Set<CommandInfo> list = getCommands();
		if(list.size()==0 && name.equals("")) return list.iterator().next();
		for (CommandInfo commandInfo : list) {
			if(commandInfo.getName().equals(name)) return commandInfo;
		}
		return null;
	}
	
	public abstract boolean issueCommand(String command, List<String> parameters, String answerTo, IRCUser source, IRCApi irc);
	public abstract Set<CommandInfo> getCommands();
	public abstract Set<String> getTypes();
}
