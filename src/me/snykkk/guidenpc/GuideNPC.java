package me.snykkk.guidenpc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.snykkk.guidenpc.commands.GuideCommand;
import me.snykkk.guidenpc.commands.GuideTab;
import me.snykkk.guidenpc.database.DefaultConfig;
import me.snykkk.guidenpc.database.MySQL;
import me.snykkk.guidenpc.libs.FNum;
import me.snykkk.guidenpc.libs.FScheduler;
import me.snykkk.guidenpc.libs.FServer;
import me.snykkk.guidenpc.libs.hologram.Hologram;
import me.snykkk.guidenpc.listeners.GuideListeners;
import me.snykkk.guidenpc.placeholders.PlaceholderHook;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class GuideNPC extends JavaPlugin {
	
	private static GuideNPC plugin;
	
	private DefaultConfig defaultConfig;
	private MySQL mysql;
	
	private Map<String, String> playerdata = new HashMap<>();
	private Map<String, Boolean> isTalking = new HashMap<>();
	private List<Hologram> holograms = new LinkedList<>();
	
	@Override
	public void onEnable() {
		plugin = this;
		
		load();
		FServer.consoleLog("§bNPCGuide loading database ...");
		FServer.consoleLog("§bNPCGuide loading configuration ...");
		
		FServer.regEvent(new GuideListeners());
		
		FServer.regCommand("guide", new GuideCommand(), new GuideTab());
		
		loadPlayerData();
		
		if(FServer.isPlugin("PlaceholderAPI")){
			new PlaceholderHook().register();
		}
		
		FScheduler.runTimerAsync(this::onParticles, 0, 20);

			
		FServer.consoleLog("§b");
		FServer.consoleLog("§b");
		FServer.consoleLog("§bNPCGuide Plugin");
		FServer.consoleLog("§bVersion: " + this.getDescription().getVersion());
		FServer.consoleLog("§bVersion: " + this.getDescription().getAuthors());
		FServer.consoleLog("§b");
		FServer.consoleLog("§b");
	}
	
	public void load() {
		this.defaultConfig = new DefaultConfig();
		this.mysql = new MySQL();
	}
	
	public void onDisable() {
		for (Hologram h : getHolograms()) {
			h.delete();
		}
		if (!getPlayerData().isEmpty()) {
			for (String uid : getPlayerData().keySet()) {
				getMySQL().setPlayerGuide(uid, getPlayerData().get(uid));
			}
		}
		FServer.consoleLog("§bNPCGuide saved database!");
		FServer.consoleLog("§bNPCGuide removed holograms!");
		FServer.consoleLog("§bNPCGuide disabling ...");
	}
	
	private void onParticles() {
		if (Bukkit.getOnlinePlayers().isEmpty()) {
			return;
		}
		if (getDefaultConfig().GUIDE_TOTAL() == 0) { return; }

		for (Player p : Bukkit.getOnlinePlayers()) {
			for (String id : plugin.getDefaultConfig().GUIDE_NPCS()) {
				if (!plugin.getPlayerData().get(p.getUniqueId().toString()).contains(getNPCString(FNum.ri(id)))) {
					NPC npc = CitizensAPI.getNPCRegistry().getById(FNum.ri(id));
					Bukkit.getScheduler().runTask(this, () -> {
						if (getNearbyPlayer(npc.getStoredLocation(), 25).isEmpty()) {
							return;
						}
						try {
							p.spawnParticle(Particle.valueOf(getDefaultConfig().GUIDE_EFFECT(FNum.ri(id))), npc.getStoredLocation().clone().add(0, 2.3, 0), 4, 0, 0, 0, 0);
						} catch (Exception ex) {
							FServer.consoleLog("Cannot set particle for " + id + " because that particle id wrong or not found!");
						}
					});
				} else {
					NPC npc = CitizensAPI.getNPCRegistry().getById(FNum.ri(id));
					Bukkit.getScheduler().runTask(this, () -> {
						if (getNearbyPlayer(npc.getStoredLocation(), 25).isEmpty()) {
							return;
						}
						try {
							p.spawnParticle(Particle.valueOf(getDefaultConfig().GUIDE_EFFECT_AFTER(FNum.ri(id))), npc.getStoredLocation().clone().add(0, 2.3, 0), 4, 0, 0, 0, 0);
						} catch (Exception ex) {
							FServer.consoleLog("Cannot set particle for " + id + " because that particle id wrong or not found!");
						}
					});
				}
			}
		}
	}
	
	public void loadPlayerData() {
		if (!getPlayerData().isEmpty()) {
			for (String uid : getPlayerData().keySet()) {
				getMySQL().setPlayerGuide(uid, getPlayerData().get(uid));
			}
		}
		getPlayerData().clear();
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			getMySQL().insertPlayer(p);
			getPlayerData().put(p.getUniqueId().toString(), getMySQL().getPlayerGuide(p));
		}
		
		for (String id : getDefaultConfig().GUIDE_NPCS()) {
			if (!getDefaultConfig().GUIDE_DISPLAY(FNum.ri(id)).equals("")) {
				try {
					NPC npc = CitizensAPI.getNPCRegistry().getById(FNum.ri(id));
									
					Hologram h = new Hologram(getDefaultConfig().GUIDE_DISPLAY(FNum.ri(id)), npc.getStoredLocation().clone().add(0, npc.getEntity().getHeight() + 0.8, 0));
					
					plugin.getHolograms().add(h);
				}
				catch (Exception ex) {
					plugin.getLogger().warning("Cannot set hologram above npc, id: " + id);
				}
			}
		}
	}
	
	public static GuideNPC getPlugin() {
		return plugin;
	}
	
	public Map<String, String> getPlayerData() {
		return playerdata;
	}
	
	public Map<String, Boolean> isTalking() {
		return isTalking;
	}
	
	public List<Hologram> getHolograms() {
		return holograms;
	}
	
	public DefaultConfig getDefaultConfig() {
		return defaultConfig;
	}
	
	public MySQL getMySQL() {
		return mysql;
	}

	private List<Player> getNearbyPlayer(Location loc, double range) {

		List<Player> l = new LinkedList<Player>();

		for (Entity en : loc.getWorld().getNearbyEntities(loc, range, range, range)) {
			if (en.getWorld() != loc.getWorld()) {
				continue;
			}
			if (en instanceof LivingEntity && !(en instanceof ArmorStand || en instanceof FallingBlock)) {
				if (!en.hasMetadata("NPC")) {
					if (en instanceof Player) {
						if (en.getLocation().distance(loc) <= range) {
							l.add((Player) en);
						}
					}
				}
			}
		}
		return l;
	}
	
	public String getNPCString (int id) {
		return "npc_" + id + "_id";
	}
}
