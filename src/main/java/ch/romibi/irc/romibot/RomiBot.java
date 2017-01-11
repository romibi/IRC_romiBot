package ch.romibi.irc.romibot;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import com.ircclouds.irc.api.Callback;
import com.ircclouds.irc.api.IRCApi;
import com.ircclouds.irc.api.IRCApiImpl;
import com.ircclouds.irc.api.IServerParameters;
import com.ircclouds.irc.api.domain.IRCServer;
import com.ircclouds.irc.api.negotiators.SaslNegotiator;
import com.ircclouds.irc.api.state.IIRCState;

import ch.romibi.irc.romibot.config.CfgChannel;
import ch.romibi.irc.romibot.config.CfgNetwork;
import ch.romibi.irc.romibot.config.CfgProfile;
import ch.romibi.irc.romibot.config.CfgServer;
import ch.romibi.irc.romibot.irclisteners.AbstractRomiBotListener;


public class RomiBot {
	
	private Config config;
	private Map<CfgProfile, IRCApi> irclist = new HashMap<CfgProfile, IRCApi>();	
	//private Map<Object, CfgNetwork> cfgSessionMap = new HashMap<Object, CfgNetwork>();
	public DB db;
	
	public RomiBot(Config pConfig) {
		config = pConfig;
	}
	
	public void start() {
		db = DBMaker.fileDB(new File("store.db")).make();
		for (final CfgNetwork net : config.getNetworks()) {
    		CfgProfile profile = net.getProfile();
    		CfgServer server = net.getServers().get(0);
    		
			if(!net.isEnabled()) { continue; }
			IServerParameters params = getServerParams(net.getProfile().getNick(), Arrays.asList(profile.getNick()), net.getProfilePassword(), "IRC Api", "romibot", server.getAddress(), server.getPort(), server.useSSL());
			Callback<IIRCState> callback = new Callback<IIRCState>()
	        {
	            @Override
	            public void onSuccess(final IIRCState aIRCState)
	            {
	                for (CfgChannel chan : net.getChannels()) {
						getIRCforProfile(profile).joinChannel(chan.getName());
					}
	            }

	            @Override
	            public void onFailure(Exception aErrorMessage)
	            {
	                throw new RuntimeException(aErrorMessage);
	            }
	        };
	        
			if(net.getProfile().useSASL()) {
            	SaslNegotiator sasl = new SaslNegotiator(profile.getSASLusername(), profile.getSASLpassword(), "");
            	getIRCforProfile(profile).connect(params, callback, sasl);
        	} else {
        		getIRCforProfile(profile).connect(params, callback);
        	}
		}
	}

	public void shutdown() {
		for (IRCApi irc : irclist.values()) {
			try {
				irc.rawMessage("QUIT");				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		db.close();
	}
	
	public IRCApi getIRCforProfile(CfgProfile profile) {
		IRCApi irc = irclist.get(profile);
		if(irc==null) {
			irc = new IRCApiImpl(true);
			irclist.put(profile, irc);
		}
		return irc;		
	}

	public void addListener(Class<? extends AbstractRomiBotListener> listenerClass) {
		try {
			for (CfgProfile profile : config.getProfiles()) {
				AbstractRomiBotListener listener = listenerClass.getConstructor(this.getClass(), CfgProfile.class).newInstance(this, profile);
				getIRCforProfile(profile).addListener(listener);
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private static IServerParameters getServerParams(final String aNickname, final List<String> aAlternativeNicks, final String aPassword, final String aRealname, final String aIdent,
            final String aServerName, final int aPort, final Boolean aIsSSLServer)
    {
        return new IServerParameters()
        {
            @Override
            public IRCServer getServer()
            {
                return new IRCServer(aServerName, aPort, aPassword, aIsSSLServer);
            }

            @Override
            public String getRealname()
            {
                return aRealname;
            }

            @Override
            public String getNickname()
            {
                return aNickname;
            }

            @Override
            public String getIdent()
            {
                return aIdent;
            }

            @Override
            public List<String> getAlternativeNicknames()
            {
                return aAlternativeNicks;
            }
        };
    }
}
