package me.snykkk.guidenpc.placeholders;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.snykkk.guidenpc.GuideNPC;
import me.snykkk.guidenpc.libs.FNum;
import net.citizensnpcs.api.CitizensAPI;

public class PlaceholderHook extends PlaceholderExpansion {
	

	private final String VERSION = getClass().getPackage().getImplementationVersion();

    /**
     * Defines the name of the expansion that is also used in the
     * placeholder itself.
     * 
     * @return {@code example} as String
     */
    @Override
    public String getIdentifier() {
        return "guide";
    }

    /**
     * The author of the expansion.
     * 
     * @return {@code everyone} as String
     */
    @Override
    public String getAuthor() {
        return "Snykkk";
    }

    /**
     * Returns the version of the expansion as String.
     *
     * @return The VERSION String
     */
    @Override
    public String getVersion() {
        return VERSION;
    }

    /**
     * Returns the name of the required plugin.
     *
     */
    @Override
    public String getRequiredPlugin() {
        return "GuideNPC";
    }

    /**
     * Used to check if the expansion is able to register.
     * 
     * @return true or false depending on if the required plugin is installed
     */
    @Override
    public boolean canRegister() {
        if (!Bukkit.getPluginManager().isPluginEnabled(getRequiredPlugin())) { return false; }
        
        return true;
    }

    private final GuideNPC plugin = GuideNPC.getPlugin();
    
    /**
     * This method is called when a placeholder is used and maches the set
     * {@link #getIdentifier() identifier}
     *
     * @param  offlinePlayer
     *         The player to parse placeholders for
     * @param  params
     *         The part after the identifier ({@code %identifier_params%})
     *
     * @return Possible-null String
     */
    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String params) {
        if (offlinePlayer == null || !offlinePlayer.isOnline()) { return "Player is not online"; }

        Player p = offlinePlayer.getPlayer();
        
        String data = plugin.getPlayerData().get(p.getUniqueId().toString());
        
        String talked = "";
        String talking = "";
        
        for (String id : plugin.getDefaultConfig().GUIDE_NPCS()) {
        	if (plugin.getPlayerData().get(p.getUniqueId().toString()).contains("npc_" + id + "_id")) {
        		talked = talked + " " + CitizensAPI.getNPCRegistry().getById(FNum.ri(id)).getFullName();
        	} else {
        		talking = talking + " " + CitizensAPI.getNPCRegistry().getById(FNum.ri(id)).getFullName();
        	}
        }
        
        switch (params) {
              
            case "progress_current":
            	return data.equalsIgnoreCase("NONE") ? "0" : String.valueOf((data.split("\\s+")).length);
            	
            case "progress_total":
            	return String.valueOf(plugin.getDefaultConfig().GUIDE_TOTAL());   	

            case "complete":
            	return talked;
            	
            case "incomplete":
            	return talking;
            	
            default:
                return null;
        }
    }
	

}
