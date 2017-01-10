package ch.romibi.irc.romibot.listeners;

import com.ircclouds.irc.api.domain.messages.ChannelPrivMsg;

import ch.romibi.irc.romibot.RomiBot;
import ch.romibi.irc.romibot.config.CfgProfile;

public class isBotThereListener extends AbstractRomiBotListener {
	public isBotThereListener(RomiBot bot, CfgProfile profile) {
		super(bot, profile);
	}

	@Override
	public void onChannelMessage(ChannelPrivMsg aMsg) {
		if(aMsg.getText().contains(profile.getNick()+" here?")) {
			queueAnswer(aMsg.getChannelName(), "yes "+aMsg.getSource().getNick()+", how can we help you today?");
		} else if(aMsg.getText().contains(profile.getNick())) {
			queueAnswer(aMsg.getChannelName(), "hi");
		}
	}
}
