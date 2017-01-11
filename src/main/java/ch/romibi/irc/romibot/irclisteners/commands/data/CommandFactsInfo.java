package ch.romibi.irc.romibot.irclisteners.commands.data;

public class CommandFactsInfo extends CommandInfo {

	@Override
	public boolean matches(String command) {
		return command.startsWith("?");
	}

	@Override
	public String getName() {
		return "?{factname}";
	}

	@Override
	public String getHelp() {
		return "Returns a previously stored fact (via !set)";
	}

}
