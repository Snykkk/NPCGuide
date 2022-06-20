package me.snykkk.guidenpc.database;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.snykkk.guidenpc.GuideNPC;
import me.snykkk.guidenpc.libs.FColor;
import me.snykkk.guidenpc.libs.FNum;
import me.snykkk.guidenpc.libs.FServer;

public class DefaultConfig {

	private File f;
	private FileConfiguration fc;
	
	private final GuideNPC plugin = GuideNPC.getPlugin();
	
	public DefaultConfig() {
		f = new File(plugin.getDataFolder(), "config.yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
			plugin.getLogger().info("Creating config.yml because it does not exist!");
			plugin.saveResource("config.yml", false);
		}
		
		this.fc = new YamlConfiguration();
		
		try {
			this.fc.load(f);
		} catch (InvalidConfigurationException|IOException ex) {
			ex.printStackTrace();
			FServer.consoleLog("Cannot load file config.yml!");
		}
	}
	
	public FileConfiguration getConfig() {
		return this.fc;
	}
	
	public void save() {
		File f = new File(plugin.getDataFolder(), "config.yml");
		try {fc.save(f);}
		catch (IOException ex) {
			ex.printStackTrace();
			FServer.consoleLog("Cannot save file config.yml!");
		}
	}
	
	public void reload() {
		save();
		fc = YamlConfiguration.loadConfiguration(f);
	}
	
	public String MESSAGE_PREFIX() {
		return FColor.msgColor(fc.getString("messages.prefix"));
	}
	public String MESSAGE_GUIDE_RESET() {
		return FColor.msgColor(fc.getString("messages.guide_reset"));
	}
	public String MESSAGE_GUIDE_RESETALL() {
		return FColor.msgColor(fc.getString("messages.guide_reset_all"));
	}
	public String MESSAGE_GUIDE_RESETPLAYER() {
		return FColor.msgColor(fc.getString("messages.guide_reset_player"));
	}
	public String MESSAGE_NO_PERMISSION() {
		return FColor.msgColor(fc.getString("messages.no_permission"));
	}
	public String MESSAGE_UNKNOWN_PLAYER() {
		return FColor.msgColor(fc.getString("messages.unknown_player"));
	}
	public String MESSAGE_RELOAD() {
		return FColor.msgColor(fc.getString("messages.reload"));
	}
	
	public int DELAY() {
		return fc.getInt("seconds_between_actions");
	}
	
	public int GUIDE_TOTAL() {
		int count = 0;
		for (String npc : fc.getConfigurationSection("guides").getKeys(false)) {
			if (GUIDE_PROGRESS(FNum.ri(npc))) {
				count++;
			}
		}
		return count;
	}
	
	public Set<String> GUIDE_NPCS() {
		return fc.getConfigurationSection("guides").getKeys(false);
	}

	public List<String> GUIDE_MESSAGES (int id) {
		return FColor.listColor(fc.getStringList("guides." + id + ".messages"));
	}
	public List<String> GUIDE_MESSAGES_AFTER (int id) {
		return FColor.listColor(fc.getStringList("guides." + id + ".messages-after"));
	}
	public String GUIDE_SOUND (int id) {
		return fc.getString("guides." + id + ".message-sound");
	}
	public String GUIDE_SOUND_AFTER (int id) {
		return fc.getString("guides." + id + ".message-sound-after");
	}
	public String GUIDE_EFFECT (int id) {
		return fc.getString("guides." + id + ".effect");
	}
	public String GUIDE_EFFECT_AFTER (int id) {
		return fc.getString("guides." + id + ".effect-after");
	}
	public String GUIDE_DISPLAY (int id) {
		return fc.getString("guides." + id + ".display");
	}
	public boolean GUIDE_PROGRESS (int id) {
		return fc.getBoolean("guides." + id + ".progress");
	}
	public List<String> GUIDE_COMMANDS (int id) {
		return FColor.listColor(fc.getStringList("guides." + id + ".commands"));
	}
	
	public boolean GUIDE_NPC (int id) {
		return fc.isSet("guides." + id);
	}
	
}
