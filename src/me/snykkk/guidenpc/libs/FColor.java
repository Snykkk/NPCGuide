package me.snykkk.guidenpc.libs;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Color;

import net.md_5.bungee.api.ChatColor;

public class FColor {

	public static Color getRandom() {
		Color c = Color.WHITE;
		c = c.setRed(FNum.randomInt(0, 255));
		c = c.setGreen(FNum.randomInt(0, 255));
		c = c.setBlue(FNum.randomInt(0, 255));
		return c;
	}
	
	public static String randomChatColor() {
		String s = "0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f";
		return "§" + s.split(",")[FNum.randomInt(0, s.split(",").length - 1)];
	}
	
	public static String msgColor(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public static List<String> listColor(List<String> msg) {
		List<String> texts = new LinkedList<String>();
		for (String s : msg) {
			texts.add(msgColor(s));
		}
		
		return texts;
	}
}
