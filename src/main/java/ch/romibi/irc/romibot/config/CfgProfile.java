package ch.romibi.irc.romibot.config;

import org.simpleframework.xml.*;

@Element (name="profile")
public class CfgProfile {
	@Attribute (name="name")
	private String pname;
	@Element
	private String nick;
	@Element (required=false)
	private String password;
	@Element (required=false)
	private String SASLusername;
	@Element (required=false)
	private String SASLpassword;
	
	public String getProfileName() {
		return pname;
	}
	
	public String getNick() {
		return nick;
	}
	
	public String getPassword() {
		return password==null ? "" : password;
	}
	
	public String getSASLusername() {
		return SASLusername==null ? "" : SASLusername;
	}
	
	public String getSASLpassword() {
		return SASLpassword==null ? "" : SASLpassword;
	}
	
	public boolean useSASL() {
		if(SASLusername==null || SASLpassword==null || SASLusername=="" || SASLpassword=="") return false;
		return true;
	}
}
