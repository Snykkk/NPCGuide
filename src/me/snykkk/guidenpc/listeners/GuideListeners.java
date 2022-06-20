package me.snykkk.guidenpc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.snykkk.guidenpc.GuideNPC;
import me.snykkk.guidenpc.libs.FServer;
import net.citizensnpcs.api.event.NPCRightClickEvent;

public class GuideListeners implements Listener {

	private final GuideNPC plugin = GuideNPC.getPlugin();
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			plugin.getMySQL().insertPlayer(p);
			plugin.getPlayerData().put(p.getUniqueId().toString(), plugin.getMySQL().getPlayerGuide(p));
		});
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		String uid = p.getUniqueId().toString();
		plugin.getMySQL().setPlayerGuide(uid, plugin.getPlayerData().get(uid));
		plugin.getPlayerData().remove(uid);
	}
	
	@EventHandler
	public void clickNPC(NPCRightClickEvent e) {
		
		if (e.getClicker().isSneaking()) return;
		
		try {
			
			Player p = e.getClicker();
			
			if (plugin.getDefaultConfig().GUIDE_NPC(e.getNPC().getId())) {
				if (!plugin.getPlayerData().get(p.getUniqueId().toString()).contains(plugin.getNPCString(e.getNPC().getId()))) {
					if (plugin.isTalking().containsKey(p.getUniqueId().toString())) {
						if (!plugin.isTalking().get(p.getUniqueId().toString())) {
							plugin.isTalking().put(p.getUniqueId().toString(), true);
							talkNPC(p, e.getNPC().getId());
						}
					} else {
						plugin.isTalking().put(p.getUniqueId().toString(), true);
						talkNPC(p, e.getNPC().getId());
					}
				} else {
					if (plugin.isTalking().containsKey(p.getUniqueId().toString())) {
						if (!plugin.isTalking().get(p.getUniqueId().toString())) {
							plugin.isTalking().put(p.getUniqueId().toString(), true);
							talkNPC_after(p, e.getNPC().getId());
						}
					} else {
						plugin.isTalking().put(p.getUniqueId().toString(), true);
						talkNPC_after(p, e.getNPC().getId());
					}
				}
			}
			
		} catch (Exception ex) {
			FServer.consoleLog("Cannot load this guide npc, please check your configuration!");
		}
		
	}
	
	public void talkNPC_after (Player p, int id) {
		new BukkitRunnable() {
			int t = 0;
			public void run() {
		        
				try {
					p.sendMessage(plugin.getDefaultConfig().GUIDE_MESSAGES_AFTER(id).get(t).replace("%player_name%", p.getName()));
				} catch (Exception ex) {
					FServer.consoleLog("Talk message list is empty!");
				}
				
				try {
					p.playSound(p.getLocation(), Sound.valueOf(plugin.getDefaultConfig().GUIDE_SOUND_AFTER(id)), 1, 1);
				} catch (Exception ex) {
					FServer.consoleLog("Sound id not found!");
				}
				
				if (t == (plugin.getDefaultConfig().GUIDE_MESSAGES_AFTER(id).size() - 1)) {
					this.cancel();
					
					plugin.isTalking().put(p.getUniqueId().toString(), false);
					for (String command : plugin.getDefaultConfig().GUIDE_COMMANDS(id)) {
						if (command.split(";")[0].equalsIgnoreCase("console")) {
							Bukkit.getScheduler().runTask(plugin, () -> {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.split(";")[1].replace("%player_name%", p.getName()));
							});
						}
						if (command.split(";")[0].equalsIgnoreCase("player")) {
							Bukkit.getScheduler().runTask(plugin, () -> {
								Bukkit.dispatchCommand(p, command.split(";")[1].replace("%player_name%", p.getName()));
							});
						}
						if (command.split(";")[0].equalsIgnoreCase("broadcast")) {
							Bukkit.broadcastMessage(command.split(";")[1].replace("%player_name%", p.getName()));
						}
						if (command.split(";")[0].equalsIgnoreCase("message")) {
							p.sendMessage(command.split(";")[1].replace("%player_name%", p.getName()));
						}
					}
				}
				
				t += 1;
				
			}
		}.runTaskTimerAsynchronously(plugin, 0, plugin.getDefaultConfig().DELAY() * 20);
	}
	
	public void talkNPC (Player p, int id) {
		new BukkitRunnable() {
			int t = 0;
			public void run() {
		        
				try {
					p.sendMessage(plugin.getDefaultConfig().GUIDE_MESSAGES(id).get(t).replace("%player_name%", p.getName()));
				} catch (Exception ex) {
					FServer.consoleLog("Talk message list is empty!");
				}
				
				try {
					p.playSound(p.getLocation(), Sound.valueOf(plugin.getDefaultConfig().GUIDE_SOUND(id)), 1, 1);
				} catch (Exception ex) {
					FServer.consoleLog("Sound id not found!");
				}
				
				if (t == (plugin.getDefaultConfig().GUIDE_MESSAGES(id).size() - 1)) {
					this.cancel();
					String data = plugin.getPlayerData().get(p.getUniqueId().toString());
					
					if (data.equalsIgnoreCase("NONE")) {
						plugin.getPlayerData().put(p.getUniqueId().toString(), plugin.getNPCString(id));
					} else {
						data = data + " " + plugin.getNPCString(id);
						plugin.getPlayerData().put(p.getUniqueId().toString(), data);
					}
					
					plugin.isTalking().put(p.getUniqueId().toString(), false);
				}
				
				t += 1;
				
			}
		}.runTaskTimerAsynchronously(plugin, 0, plugin.getDefaultConfig().DELAY() * 20);
	}
	
}
