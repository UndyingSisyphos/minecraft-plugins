package play.lococraft.tk;

import java.util.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.enchantments.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.*;

import enchantments.Luck;



public class Main extends JavaPlugin implements Listener{
	
	Location spawn = null;
	CustomEnchantsManager customEnchant= null;
	
	
	@Override
	public void onEnable() {
		initLocations();
		this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
		this.getServer().getPluginManager().registerEvents((Listener)new Luck(101), (Plugin)this);
		customEnchant = new CustomEnchantsManager();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			
			if(name.equalsIgnoreCase("spawn")) {
				p.sendMessage("Warping to spawn!");
				p.teleport(spawn);
			}
			
			if(name.equalsIgnoreCase("i")) {
				p.getInventory().getItemInMainHand().addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 2);
			}
			
			if(name.equalsIgnoreCase("incanta")) {
				ItemStack i = new ItemStack(p.getInventory().getItemInMainHand());
				if(customEnchant.isEnchantable(i, "luck")) {
					customEnchant.addEnchantment(p, "luck");
				}
				
			}
			
			if(name.equalsIgnoreCase("warp")) {
				warpTo(p, args[0]);
			}
			
			
		}
		
		return false;
	}
	
	@EventHandler
	public void onPlayerClicksInventory(InventoryClickEvent event) {
		if(event.isLeftClick()) {
			if(event.getCursor().getType().equals(Material.ENCHANTED_BOOK)) {
				if(event.getCurrentItem().getType().equals(Material.DIAMOND_PICKAXE)) {
					Map<Enchantment,Integer> ench = event.getCursor().getEnchantments();
					event.getCurrentItem().addEnchantments(ench);
				}
			}
		}
		
	}
	
	@EventHandler
	public void dontDropButGiveItems(BlockBreakEvent event) {
		Player p = event.getPlayer();
		event.setDropItems(false);
		event.setExpToDrop(0);
		if(event.getBlock().getType().equals(Material.IRON_ORE)) {
			p.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 1));
		} else if(event.getBlock().getType().equals(Material.COAL_ORE)) {
			p.getInventory().addItem(new ItemStack(Material.COAL, 1));
		} else if(event.getBlock().getType().equals(Material.LAPIS_ORE)) {
			p.getInventory().addItem(new ItemStack(Material.INK_SACK, 1, (short) 4));
		} else if(event.getBlock().getType().equals(Material.REDSTONE_ORE)) {
			p.getInventory().addItem(new ItemStack(Material.REDSTONE, 1));
		} else if(event.getBlock().getType().equals(Material.DIAMOND_ORE)) {
			p.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
		} else if(event.getBlock().getType().equals(Material.EMERALD_ORE)) {
			p.getInventory().addItem(new ItemStack(Material.EMERALD, 1));
		} else if(event.getBlock().getType().equals(Material.GOLD_ORE)) {
			p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 1));
		} else {
			p.getInventory().addItem(new ItemStack(event.getBlock().getType(), 1));
		}
	}

	public void initLocations() {
		spawn = new Location(this.getServer().getWorld("world"), 0.5, 90, 0.5);
	}
	
	public void warpTo(Player p, String loc) {
		if(loc.equalsIgnoreCase("spawn")) {
			p.sendMessage("Warping to spawn!");
			p.teleport(spawn);
		} else if(loc.equalsIgnoreCase("crates")) {
			p.sendMessage("Warping to crates!");
		} else if(loc.equalsIgnoreCase("pvp") || loc.equalsIgnoreCase("pvpmine")) {
			p.sendMessage("Warping to PvP mine!");
		} else if(loc.equalsIgnoreCase("a")) {
			
		} else if(loc.equalsIgnoreCase("b")) {
			
		} else if(loc.equalsIgnoreCase("c")) {
			
		} else if(loc.equalsIgnoreCase("d")) {
			
		} else if(loc.equalsIgnoreCase("e")) {
			
		} else if(loc.equalsIgnoreCase("f")) {
			
		} else if(loc.equalsIgnoreCase("g")) {
			
		} else if(loc.equalsIgnoreCase("h")) {
			
		} else if(loc.equalsIgnoreCase("i")) {
			
		} else if(loc.equalsIgnoreCase("j")) {
			
		} else if(loc.equalsIgnoreCase("k")) {
			
		} else if(loc.equalsIgnoreCase("l")) {
			
		} else if(loc.equalsIgnoreCase("m")) {
			
		} else if(loc.equalsIgnoreCase("n")) {
			
		} else if(loc.equalsIgnoreCase("o")) {
			
		} else if(loc.equalsIgnoreCase("p")) {
			
		} else if(loc.equalsIgnoreCase("q")) {
			
		} else if(loc.equalsIgnoreCase("r")) {
			
		} else if(loc.equalsIgnoreCase("s")) {
			
		} else if(loc.equalsIgnoreCase("t")) {
			
		} else if(loc.equalsIgnoreCase("u")) {
			
		} else if(loc.equalsIgnoreCase("v")) {
			
		} else if(loc.equalsIgnoreCase("w")) {
			
		} else if(loc.equalsIgnoreCase("x")) {
			
		} else if(loc.equalsIgnoreCase("y")) {
			
		} else if(loc.equalsIgnoreCase("z")) {
			
		}
	}
	
}
