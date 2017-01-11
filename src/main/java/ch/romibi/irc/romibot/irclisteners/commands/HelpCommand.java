package ch.romibi.irc.romibot.irclisteners.commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ircclouds.irc.api.IRCApi;
import com.ircclouds.irc.api.domain.IRCUser;

import ch.romibi.irc.romibot.irclisteners.CommandListener;
import ch.romibi.irc.romibot.irclisteners.commands.data.CommandInfo;
import ch.romibi.irc.romibot.irclisteners.commands.data.SimpleCommandInfo;
import ch.romibi.irc.romibot.listeners.romibotEventListener;

public class HelpCommand extends Command implements romibotEventListener {
	Set<CommandInfo> knownCommands = new HashSet<CommandInfo>(); 
	
	public HelpCommand(CommandListener listener) {
		super(listener);
		listener.registerForEvent(romibotEventListener.type.onRegister, this);
	}

	@Override
	public Set<CommandInfo> getCommands() {
		Set<CommandInfo> set = new HashSet<CommandInfo>();
		set.add(new SimpleCommandInfo("!help", "Gives help to commands. You are using it right now!"));
		return set;
	}
	
	@Override
	public void onRegisterCommand(Command command) {
		knownCommands.addAll(command.getCommands());
	}

	@Override
	public Set<String> getTypes() {
		return new HashSet<String>(Arrays.asList("!"));
	}

	@Override
	public boolean issueCommand(String command, List<String> parameters, String answerTo, IRCUser source, IRCApi irc) {
		String text = "";
		if(parameters.isEmpty()) {
			text = "Supported Commands: ";
			boolean pre = false;
			for (CommandInfo commandInfo : knownCommands) {
				if(pre) text += ", ";
				text += commandInfo.getName();
				pre=true;
			}
		} else {
			for (CommandInfo commandInfo : knownCommands) {
				String name = commandInfo.getName();
				String search = parameters.get(0);
				
				if(name.equals(search)) {
					text = commandInfo.getHelp();
				}
				
				boolean pre = false;
				if(name.contains(search)) {
					if(pre) text += "\n";
					text += commandInfo.getHelp();
					pre=true;
				}
			}
		}
		if(!text.equals("")) {
			irc.message(answerTo, text);
		}
		return false;
	}
}
