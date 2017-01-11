package ch.romibi.irc.romibot.irclisteners.commands.data;

public abstract class CommandInfo {
	public abstract String getName();
	public abstract String getHelp();
	public abstract boolean matches(String command);
}
