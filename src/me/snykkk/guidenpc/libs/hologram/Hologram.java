package me.snykkk.guidenpc.libs.hologram;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.FixedMetadataValue;

import me.snykkk.guidenpc.GuideNPC;
import me.snykkk.guidenpc.libs.FColor;

public class Hologram {
	
	private ArmorStand as;
	private Location loc;
	private List<ArmorStand> standlist = new LinkedList<>();	

	private final GuideNPC plugin = GuideNPC.getPlugin();
	
	public Hologram(String text, Location loc) {
		this.loc = loc.add(0, -2, 0);
		this.as = loc.getWorld().spawn(this.loc, ArmorStand.class);
		this.as.setCustomName(FColor.msgColor(text));
		this.as.setCustomNameVisible(true);
		this.as.setArms(false);
		this.as.setGravity(false);
		this.as.setVisible(false);
		this.as.setSmall(true);
		this.as.setMetadata("NPCGUIDE_ARMORSTAND", new FixedMetadataValue(plugin, "NPCGUIDE_ID_ARMORSTAND"));
		standlist.add(as);
	}
	
	public void addText(String text) {
		ArmorStand stand = loc.getWorld().spawn(loc.add(0, -0.25, 0), ArmorStand.class);
		stand.setCustomName(FColor.msgColor(text));
		stand.setCustomNameVisible(true);
		stand.setArms(false);
		stand.setGravity(false);
		stand.setVisible(false);
		stand.setSmall(true);
		stand.setMetadata("NPCGUIDE_ARMORSTAND", new FixedMetadataValue(plugin, "NPCGUIDE_ID_ARMORSTAND"));
		standlist.add(stand);
	}
	
	public String getText() {
		return this.as.getName();
	}
	
	public void setText(String text) {
		this.as.setCustomName(text);
	}
	
	public Location getLocation() {
		return this.loc;
	}
	
	public List<ArmorStand> getHolograms() {
		return standlist;
	}
	
	public ArmorStand getLine(int line) {
		if (standlist.get(line) == null) {
			return null;
		}
		return standlist.get(line);
	}
	
	public void setText(String text, int line) {
		if (standlist.get(line) == null) {
			return;
		}
		standlist.get(line).setCustomName(text);
	}
	
	public void removeLine(int line) {
		if (standlist.get(line) == null) {
			return;
		}
		standlist.get(line).remove();
	}
	
	public void delete() {
		for (ArmorStand stand : standlist) {
			stand.remove();
		}
	}

}
