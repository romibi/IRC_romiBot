package ch.romibi.irc.romibot.config;

import java.util.List;

import org.simpleframework.xml.*;

@Element (name="network")
public class CfgNetwork {
	@Attribute
	private String name;
	@Attribute (required=false)
	private boolean enabled;
	@Element
	private CfgProfile profile;
	@ElementList
	private List<CfgServer> servers;
	@ElementList
	private List<CfgChannel> channels;
	@Element (required=false)
	private String customPassword;
	
	public String getName() {
		return name;
	}
	
	public CfgProfile getProfile() {
		return profile;
	}
	
	public List<CfgServer> getServers() {
		return servers;
	}
	
	public List<CfgChannel> getChannels() {
		return channels;
 	}
	
	public String getProfilePassword() {
		if(customPassword!=null) {
			return customPassword;
		}
		return profile.getPassword();
	}

	public boolean isEnabled() {
		return enabled;
	}
}
