package ch.romibi.irc.romibot.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.mapdb.Serializer;

import com.ircclouds.irc.api.domain.IRCUser;
import com.ircclouds.irc.api.domain.messages.ChannelPrivMsg;
import com.ircclouds.irc.api.domain.messages.UserPrivMsg;

import ch.romibi.irc.romibot.RomiBot;
import ch.romibi.irc.romibot.config.CfgProfile;

public class CommandListener extends AbstractRomiBotListener {

	public CommandListener(RomiBot bot, CfgProfile profile) {
		super(bot, profile);
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
		String[] textElements = text.split(" ");
		if(textElements[0].startsWith("!")) {
			handleAction(textElements, answerTo, source);
		}
		if(textElements[0].startsWith("?")) {
			handleFactRequest(textElements, answerTo, source);
		}
	}

	private void handleFactRequest(String[] textElements, String answerTo, IRCUser source) {
		if(textElements.length==1 || textElements.length==2) {
			ConcurrentMap<String, String> map = bot.db.hashMap("facts", Serializer.STRING, Serializer.STRING).createOrOpen();
			String text = map.get(textElements[0].substring(1));
			if(textElements.length==2) {
				text = textElements[1]+": "+text;
			}
			if(text!=null && text!="") {
				getIRC().message(answerTo, text); //TODO: handle outgoing parsing
			}
		}
	}

	private void handleAction(String[] textElements, String answerTo, IRCUser source) {
		switch (textElements[0]) {
		//TODO: access rules
		case "!set":
			List<String> list =  new LinkedList<String>(Arrays.asList(textElements));
			list.remove(0);
			list.remove(0);
			String text = String.join(" ", list);
			ConcurrentMap<String, String> map = bot.db.hashMap("facts", Serializer.STRING, Serializer.STRING).createOrOpen();
			map.put(textElements[1], text);
			break;
		case "!help":
			getIRC().message(answerTo, "Supported Commands: !set, !help");
			break;
		default:
			if(source.getNick().equals("romibi")) {
				getIRC().message(answerTo, "Unknown Command!");
			}
		}
	}
}
