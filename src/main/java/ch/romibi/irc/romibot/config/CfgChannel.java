package ch.romibi.irc.romibot.config;

import org.simpleframework.xml.*;

@Element (name="channel")
public class CfgChannel {
	@Element
	private String name;
	
	public String getName() {
		return name;
	}
}
