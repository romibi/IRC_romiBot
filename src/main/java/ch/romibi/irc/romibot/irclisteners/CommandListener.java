package ch.romibi.irc.romibot.irclisteners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ircclouds.irc.api.domain.IRCUser;
import com.ircclouds.irc.api.domain.messages.ChannelPrivMsg;
import com.ircclouds.irc.api.domain.messages.UserPrivMsg;

import ch.romibi.irc.romibot.RomiBot;
import ch.romibi.irc.romibot.config.CfgProfile;
import ch.romibi.irc.romibot.irclisteners.commands.Command;
import ch.romibi.irc.romibot.irclisteners.commands.FactCommands;
import ch.romibi.irc.romibot.irclisteners.commands.HelpCommand;
import ch.romibi.irc.romibot.listeners.romibotEventListener;
import ch.romibi.irc.romibot.listeners.romibotEventListener.type;

public class CommandListener extends AbstractRomiBotListener {
	List<Command> commands = new ArrayList<Command>();
	Set<String> commandTypes = new HashSet<String>();
	Map<type, List<romibotEventListener>> eventlisteners = new HashMap<type, List<romibotEventListener>>();
	
	public CommandListener(RomiBot bot, CfgProfile profile) {
		super(bot, profile);
		new HelpCommand(this);
		new FactCommands(this);
	}
	
	@Override
	public void onChannelMessage(ChannelPrivMsg aMsg) {
		parseCommand(aMsg.getText(), aMsg.getChannelName(), aMsg.getSource());
	}
	
	@Override
	public void onUserPrivMessage(UserPrivMsg aMsg) {
		parseCommand(aMsg.getText(), aMsg.getToUser(), aMsg.getSource());
	}
	
	private void parseCommand(String text, String answerTo, IRCUser source) {
		if(mayBeCommand(text)) {
			for (Command command : commands) {
				if(command.accept(text, answerTo, source, getIRC())) break;
			}
		}
	}

	private boolean mayBeCommand(String text) {
		for (String string : commandTypes) {
			if(text.startsWith(string)) return true;
		}
		return false;
	}

	public void register(Command command) {
		if(eventlisteners.get(type.onRegister)!=null) {
			for (romibotEventListener romibotEventListener : eventlisteners.get(type.onRegister)) {
				romibotEventListener.onRegisterCommand(command);
			}
		}
		commandTypes.addAll(command.getTypes());
		commands.add(command);
	}

	public void registerForEvent(type eventtype, romibotEventListener eventlistener) {
		List<romibotEventListener> list = eventlisteners.get(eventtype); 
		if(list==null) {
			list = new ArrayList<romibotEventListener>();
		}
		list.add(eventlistener);
		eventlisteners.put(eventtype, list);
	}
}