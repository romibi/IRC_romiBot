package ch.romibi.irc.romibot.listeners;

import ch.romibi.irc.romibot.irclisteners.commands.Command;

public interface romibotEventListener {
	
	public enum type {
		onRegister,
		unknown
	}

	public default void onRegisterCommand(Command command) {}
}
