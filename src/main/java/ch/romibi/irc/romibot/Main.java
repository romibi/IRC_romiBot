package ch.romibi.irc.romibot;

import java.io.File;
import java.util.Scanner;

import ch.romibi.irc.romibot.irclisteners.CommandListener;
import ch.romibi.irc.romibot.irclisteners.isBotThereListener;

public class Main {
	private static final String CONFIG_FILE = "config.xml";

	private static RomiBot bot;
	private static boolean shutdown = false;

	private static Scanner scanner;

	public static void main(String[] args) {
		bot = new RomiBot(Config.load(new File(CONFIG_FILE)));
		bot.addListener(isBotThereListener.class);
		bot.addListener(CommandListener.class);
		
		bot.start();
		
		scanner = new Scanner(System.in);
		
		while(!shutdown) {
            String input = scanner.nextLine();
            if(input.equals("quit")) {
            	shutdown = true;
            }
		}
		
		bot.shutdown();
	}

}
