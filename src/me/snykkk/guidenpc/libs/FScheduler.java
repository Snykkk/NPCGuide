package me.snykkk.guidenpc.libs;

import org.bukkit.Bukkit;

import me.snykkk.guidenpc.GuideNPC;

public class FScheduler {

	public static void runLater(long delay, Runnable runnable)
    {
        Bukkit.getScheduler().runTaskLater(GuideNPC.getPlugin(), runnable, delay);
    }

    /**
     * Runs a task on another thread immediately.
     * @param runnable - Task to perform.
     */
    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(GuideNPC.getPlugin(), runnable);
    }
    
    public static void runTimerAsync(Runnable runnable, long delay, long period) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(GuideNPC.getPlugin(), runnable, delay, period);
    }
    
    public static void runTimer(Runnable runnable, long delay, long period) {
        Bukkit.getScheduler().runTaskTimer(GuideNPC.getPlugin(), runnable, delay, period);
    }

    /**
     * Runs a task on the main thread immediately
     * @param runnable - Task to perform
     */
    public static void run(Runnable runnable){
        Bukkit.getScheduler().runTask(GuideNPC.getPlugin(), runnable);
    }
	
}
