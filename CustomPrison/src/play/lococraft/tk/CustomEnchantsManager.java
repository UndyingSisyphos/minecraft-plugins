package play.lococraft.tk;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import enchantments.*;


public class CustomEnchantsManager {
	
	Luck luck = null;
	Explosive explosive = null;
	
	public CustomEnchantsManager() {
		initEnchants();
		loadEnchants();
	}
	
	public void initEnchants() {
		luck = new Luck(101);
		explosive = new Explosive(102);
	}
	
	public void loadEnchants() {
		Field f = null;
		try {
			f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Enchantment.registerEnchantment(luck);
	}
	
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	public void unloadEnchants() {
		try {
			Field byIdField = Enchantment.class.getDeclaredField("byId");
			Field byNameField = Enchantment.class.getDeclaredField("byName");
			byIdField.setAccessible(true);
			byNameField.setAccessible(true);
			Map<Integer,Enchantment> byId = (HashMap<Integer,Enchantment>) byIdField.get(null);
			Map<Integer,Enchantment> byName = (HashMap<Integer,Enchantment>) byNameField.get(null);
			if(byId.containsKey(luck.getId())) {
				byId.remove(luck.getId());
			}
			if(byId.containsKey(luck.getName())) {
				byName.remove(luck.getName());
			}
		} catch(Exception ignored) {
			
		}
	}

	public void addEnchantment(Player p, String enchName) {
		ItemStack item = p.getInventory().getItemInMainHand();
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = null;
		if(meta.hasLore()) {
			lore = (ArrayList<String>) meta.getLore();
		} else {
			lore = new ArrayList<String>();
		}
		
		if(enchName.equals("luck")) {
			int level = p.getInventory().getItemInMainHand().getEnchantmentLevel(luck) + 1;
			if(item.containsEnchantment(luck)) {
				lore.set(lore.indexOf(luck.getName()+" "+(level-1)), luck.getName()+" "+level);
			} else {
				lore.add(luck.getName()+" "+level);
			}
			meta.setLore(lore);
			if(level <= luck.getMaxLevel()) {
				p.getInventory().getItemInMainHand().setItemMeta(meta);
				p.getInventory().getItemInMainHand().addEnchantment(luck, level);
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Already at maximum level!"));
			}
		} else if(enchName.equals("explosive")) {
			
		}
	}
	
	public boolean isEnchantable(ItemStack i, String ench) {
		switch(ench) {
		case("luck"):
			return luck.canEnchantItem(i);
		case("explosive"):
			return explosive.canEnchantItem(i);
		default:
			return false;
		}
	}
	
}
