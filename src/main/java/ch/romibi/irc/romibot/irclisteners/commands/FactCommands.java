package ch.romibi.irc.romibot.irclisteners.commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.mapdb.DB;
import org.mapdb.Serializer;

import com.ircclouds.irc.api.IRCApi;
import com.ircclouds.irc.api.domain.IRCUser;

import ch.romibi.irc.romibot.irclisteners.CommandListener;
import ch.romibi.irc.romibot.irclisteners.commands.data.CommandFactsInfo;
import ch.romibi.irc.romibot.irclisteners.commands.data.CommandInfo;
import ch.romibi.irc.romibot.irclisteners.commands.data.Fact;
import ch.romibi.irc.romibot.irclisteners.commands.data.SimpleCommandInfo;

public class FactCommands extends Command {
	public FactCommands(CommandListener listener) {
		super(listener);
	}

	@Override
	public Set<CommandInfo> getCommands() {
		Set<CommandInfo> set = new HashSet<CommandInfo>();
		set.add(new SimpleCommandInfo("!set", "!set {name} {string}, Example: !set answer 42, Creates or updates a Fact which can be requested with ?{name} (e.g. ?answer)"));
		set.add(new CommandFactsInfo());
		return set;
	}

	@Override
	public Set<String> getTypes() {
		return new HashSet<String>(Arrays.asList("!", "?"));
	}

	@Override
	public boolean issueCommand(String command, List<String> parameters, String answerTo, IRCUser source, IRCApi irc) {
		if (getCommand("!set").matches(command)) {
			setFact(parameters, source);
		} else if(getCommand("?{factname}").matches(command))  {
			getFact(command, parameters,answerTo, irc);
		}
		return false;
	}
	
	
	private void getFact(String factname, List<String> parameters, String answerTo, IRCApi irc) {
		if(parameters.size()==0 || parameters.size()==1) {
			ConcurrentMap<String, Fact> map = getDB().hashMap("facts", Serializer.STRING, Serializer.JAVA).createOrOpen();
			
			
			Fact fact = map.get(factname.substring(1));
			if(fact != null) {
				String text = fact.toString();
				if(parameters.size()==1) {
					text = parameters.get(0)+": "+text;
				}
				if(text!=null && text!="") {
					irc.message(answerTo, text); //TODO: handle outgoing parsing
				}
			}
		}
	}

	private void setFact(List<String> parameters, IRCUser source) {
		String factname = parameters.get(0);
		parameters.remove(0);
		String text = String.join(" ", parameters);
		ConcurrentMap<String, Fact> map = getDB().hashMap("facts", Serializer.STRING, Serializer.JAVA).createOrOpen();
		
		Fact fact = new Fact(factname, text, source.getNick());
		Fact oldFact = map.get(factname);
		if(oldFact==null || oldFact.getOwner().equals(source.getNick())) { // or source is romibot admin
			map.put(factname, fact);	
		}
	}
	
	private DB getDB() {
		return commandlistener.getDB();
	}
}
