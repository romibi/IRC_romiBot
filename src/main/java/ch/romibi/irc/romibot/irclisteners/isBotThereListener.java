package ch.romibi.irc.romibot.irclisteners;

import com.ircclouds.irc.api.domain.messages.ChannelPrivMsg;

import ch.romibi.irc.romibot.RomiBot;
import ch.romibi.irc.romibot.config.CfgProfile;
import ch.romibi.irc.romibot.irclisteners.AbstractRomiBotListener;

public class isBotThereListener extends AbstractRomiBotListener {
	public isBotThereListener(RomiBot bot, CfgProfile profile) {
		super(bot, profile);
	}

	@Override
	public void onChannelMessage(ChannelPrivMsg aMsg) {
		if(aMsg.getText().contains(profile.getNick()+" here?")) {
			queueAnswer(aMsg.getChannelName(), "yes "+aMsg.getSource().getNick()+", how can we help you today? (You may have to wait for some time until someone can answer.)");
		} else if(aMsg.getText().contains(profile.getNick())) {
			//queueAnswer(aMsg.getChannelName(), "hi");
		}
	}
}
