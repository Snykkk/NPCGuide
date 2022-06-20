package me.snykkk.guidenpc.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.snykkk.guidenpc.GuideNPC;

public class GuideCommand implements CommandExecutor {
	
	private final GuideNPC plugin = GuideNPC.getPlugin();

	@Override
	public boolean onCommand(final CommandSender sender, Command command, String s, final String[] args) {
		
		if (args.length == 0) {
			if (sender.hasPermission("guidenpc.reset")) {
				sender.sendMessage("§e/guide reset §7- §aReset your the data");
			}
			if (sender.hasPermission("guidenpc.admin")) {
				sender.sendMessage("§e/guide reset <player> §7- §aReset the data of a player");
				sender.sendMessage("§e/guide resetall §7- §aReset the data of all players");
				sender.sendMessage("§e/guide reload §7- §aReload the config");
			}
		}
		
		if (args.length == 1 && args[0].equalsIgnoreCase("reset") && sender.hasPermission("guidenpc.reset")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				plugin.getPlayerData().put(p.getUniqueId().toString(), "NONE");
				p.sendMessage(plugin.getDefaultConfig().MESSAGE_PREFIX() + plugin.getDefaultConfig().MESSAGE_GUIDE_RESET());
			}
			return true;
		}
		
		if (!sender.hasPermission("guidenpc.admin")) {
			sender.sendMessage(plugin.getDefaultConfig().MESSAGE_PREFIX() + plugin.getDefaultConfig().MESSAGE_NO_PERMISSION());
			return true;
		}
		
		if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			plugin.load();
			plugin.loadPlayerData();
			sender.sendMessage(plugin.getDefaultConfig().MESSAGE_PREFIX() + plugin.getDefaultConfig().MESSAGE_RELOAD());
		}
		
		if (args.length == 1 && args[0].equalsIgnoreCase("resetall")) {
			plugin.getMySQL().resetAll();
			for (String uid : plugin.getPlayerData().keySet()) {
				plugin.getPlayerData().put(uid, "NONE");
			}
			sender.sendMessage(plugin.getDefaultConfig().MESSAGE_PREFIX() + plugin.getDefaultConfig().MESSAGE_GUIDE_RESETALL());
		}
		
		if (args.length == 2 && args[0].equalsIgnoreCase("reset")) {
			Player p = Bukkit.getPlayer(args[1]);
			if (p != null) {
				plugin.getPlayerData().put(p.getUniqueId().toString(), "NONE");
				sender.sendMessage(plugin.getDefaultConfig().MESSAGE_PREFIX() + plugin.getDefaultConfig().MESSAGE_GUIDE_RESETPLAYER().replace("%player_name%", p.getName()));
				p.sendMessage(plugin.getDefaultConfig().MESSAGE_PREFIX() + plugin.getDefaultConfig().MESSAGE_GUIDE_RESET());
			} else {
				sender.sendMessage(plugin.getDefaultConfig().MESSAGE_PREFIX() + plugin.getDefaultConfig().MESSAGE_UNKNOWN_PLAYER());
			}
		}
		
		return true;
	}
	
}
