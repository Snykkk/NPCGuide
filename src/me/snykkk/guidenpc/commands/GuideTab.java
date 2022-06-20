package me.snykkk.guidenpc.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class GuideTab implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String a, String[] args) {
	
		if (sender.hasPermission("guidenpc.admin")) {
			
			if (args.length == 1) {
				return Arrays.asList("reload", "reset", "resetall");
			}
			
		}
		
		return null;
	}
	
}
