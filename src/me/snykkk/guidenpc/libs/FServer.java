package me.snykkk.guidenpc.libs;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;

import me.snykkk.guidenpc.GuideNPC;

public class FServer {

	public static void consoleLog(String message){
		Bukkit.getConsoleSender().sendMessage(message);
	}
	
	public static void broadcast(String message){
		Bukkit.broadcastMessage(message);
	}
	
	public static boolean isPlugin(String plugin) {
		return Bukkit.getPluginManager().isPluginEnabled(plugin);
	}
	
	public static void regCommand(String command, CommandExecutor executor, TabCompleter completer) {
		GuideNPC.getPlugin().getCommand(command).setExecutor(executor);
		GuideNPC.getPlugin().getCommand(command).setTabCompleter(completer);
	}
	
	public static void regEvent(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, GuideNPC.getPlugin());
	}
	
}
