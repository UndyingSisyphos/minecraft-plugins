package prison.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

@SuppressWarnings({"unused"})
public class Utils {
	
	private static Utils instance = null;
	private Map<Integer,String> alphabet = null;
	
	public Utils() {
		alphabet = new HashMap<Integer,String>();
		initAlphabet();
	}
	
	public static Utils getInstance() {
		if(instance == null) {
			instance = new Utils();
		}
		return instance;
	}
	
	public String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	// Overload
	public ItemStack createItem(Material m, int amount, String displayName, String... loreInput) {
		ItemStack item = new ItemStack(m, amount);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		if(displayName != null) {
			meta.setDisplayName(displayName);
		}
		for(String s: loreInput) {
			lore.add(chat(s));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	// Overload
	public ItemStack createItem(Material m, int amount, int color, String displayName, String... loreInput) {
		ItemStack item = new ItemStack(m, amount, (short) color);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		meta.setDisplayName(displayName);
		for(String s: loreInput) {
			lore.add(chat(s));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	// Overload
	public ItemStack createItem(Material m, int amount, String displayName, boolean hideFlags, String... loreInput) {
		ItemStack item = new ItemStack(m, amount);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		meta.setDisplayName(displayName);
		for(String s: loreInput) {
			lore.add(chat(s));
		}
		meta.setLore(lore);
		if(hideFlags) {
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		}
		item.setItemMeta(meta);
		return item;
	}
	
	public long timeDifference(GregorianCalendar lastClaim) {
		GregorianCalendar cal = new GregorianCalendar();
		long diff = cal.getTimeInMillis() - lastClaim.getTimeInMillis();
		return diff;
	}
	
	public String getRoman(int n) {
		switch(n) {
		case(1):
			return "I";
		case(2):
			return "II";
		case(3):
			return "III";
		case(4):
			return "IV";
		case(5):
			return "V";
		case(6):
			return "VI";
		case(7):
			return "VII";
		case(8):
			return "VIII";
		case(9):
			return "IX";
		case(10):
			return "X";
		default:
			return null;
		}
	}
	
	private void initAlphabet() {
		alphabet.put(1, "a");
		alphabet.put(2, "b");
		alphabet.put(3, "c");
		alphabet.put(4, "d");
		alphabet.put(5, "e");
		alphabet.put(6, "f");
		alphabet.put(7, "g");
		alphabet.put(8, "h");
		alphabet.put(9, "i");
		alphabet.put(10, "j");
		alphabet.put(11, "k");
		alphabet.put(12, "l");
		alphabet.put(13, "m");
		alphabet.put(14, "n");
		alphabet.put(15, "o");
		alphabet.put(16, "p");
		alphabet.put(17, "q");
		alphabet.put(18, "r");
		alphabet.put(19, "s");
		alphabet.put(20, "t");
		alphabet.put(21, "u");
		alphabet.put(22, "v");
		alphabet.put(23, "w");
		alphabet.put(24, "x");
		alphabet.put(25, "y");
		alphabet.put(26, "z");
	}
	
	public String getLetter(int pos) {
		if(pos > 0 && pos <= 26) {
			return alphabet.get(pos);
		}
		return null;
	}
}
