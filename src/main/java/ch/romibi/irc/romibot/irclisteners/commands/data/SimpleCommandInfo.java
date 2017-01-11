package ch.romibi.irc.romibot.irclisteners.commands.data;

public class SimpleCommandInfo extends CommandInfo {

	private String name;
	private String help;
	private String match;

	public SimpleCommandInfo(String commandname, String helptext) {
		this.name = commandname;
		this.help = helptext;
		this.match = commandname;
	}

	public SimpleCommandInfo(String commandname, String helptext, String match) {
		this.name = commandname;
		this.help = helptext;
		this.match = match;
	}

	@Override
	public boolean matches(String command) {
		return command.equals(match);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getHelp() {
		return help;
	}
}
