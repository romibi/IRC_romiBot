package ch.romibi.irc.romibot.listeners;

import java.util.Random;

import com.ircclouds.irc.api.Callback;
import com.ircclouds.irc.api.IRCApi;
import com.ircclouds.irc.api.listeners.VariousMessageListenerAdapter;

import ch.romibi.irc.romibot.RomiBot;
import ch.romibi.irc.romibot.config.CfgProfile;

public abstract class AbstractRomiBotListener extends VariousMessageListenerAdapter {
	protected RomiBot bot;
	protected CfgProfile profile;
	
	/* protected Callback<String> dummycallback= new Callback<String>() {
		@Override
		public void onSuccess(String aObject) {}
		@Override
		public void onFailure(Exception aExc) {}
	}; */
	
	
	public AbstractRomiBotListener(RomiBot bot, CfgProfile profile) {
		this.bot = bot;
		this.profile = profile;
	}
	
	protected IRCApi getIRC() {
		return bot.getIRCforProfile(profile);
	}
	
	public void queueAnswer(String target, String message) {
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		                getIRC().message(target, message);
		            }
		        }, 
		        new Random().nextInt(10000 - 1000 + 1) + 1000 
		);
	}
}
