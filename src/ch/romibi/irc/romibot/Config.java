package ch.romibi.irc.romibot;

import java.io.File;
import java.util.List;

import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.CycleStrategy;
import org.simpleframework.xml.strategy.Strategy;

import ch.romibi.irc.romibot.config.CfgNetwork;
import ch.romibi.irc.romibot.config.CfgProfile;

@Root (name="config")
public class Config {
	private File file;
	
	@ElementList
	private List<CfgProfile> profiles;
	@ElementList
	private List<CfgNetwork> networks;
	
	private Config() {
	}

	private Config(File file) {
		this.file = file;
	}
	
	public List<CfgProfile> getProfiles() {
		return profiles;
	}
	
	public List<CfgNetwork> getNetworks() {
		return networks;
	}
	
	public CfgProfile getProfileByName(String name) {
		for (CfgProfile p : profiles) {
			if(p.getProfileName()==name) {
				return p;
			}
		}
		return null;
	}
	
	public CfgNetwork getNetworkByName(String name) {
		for (CfgNetwork net : networks) {
			if(net.getName()==name) {
				return net;
			}
		}
		return null;
	}

	public static Config load(File file) {
		Strategy strategy = new CycleStrategy("id", "ref");
		Serializer serializer = new Persister(strategy);
		try {
			Config config = serializer.read(Config.class, file);
			config.file = file;
			return config;
		} catch (Exception e) {
			return new Config(file);
		}
	}

	private void save() {
		System.out.println("WARNING: saving config not yet tested!");
		Serializer serializer = new Persister();
		try {
			serializer.write(this, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
