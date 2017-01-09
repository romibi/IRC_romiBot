package ch.romibi.irc.romibot.config;

import org.simpleframework.xml.*;

@Element (name="server")
public class CfgServer {
	@Element
	private String address;
	@Element
	private int port;
	@Element
	private boolean ssl;
	
	public String getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}
	
	public boolean useSSL() {
		return ssl;
	}
}
