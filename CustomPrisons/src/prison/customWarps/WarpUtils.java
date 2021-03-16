package prison.customWarps;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import prison.customGUI.CustomGUI;
import prison.main.Main;
import prison.main.Utils;

@SuppressWarnings({"unused"})
public class WarpUtils {
	
	private Main main = null;
	private Utils u = null;
	
	public WarpUtils(Main main) {
		this.main = main;
		u = new Utils();
	}
	
	public void tp(Player p, String command, Map<String,Location> warps) {
		String show = command.substring(0, 1).toUpperCase() + command.substring(1);
		if(command.equals("pvpmine")) {
			show = "PvP Mine";
		}
		if(warps.size() > 0) {
			if(warps.containsKey(command)) {
				p.sendMessage(u.chat("&7&oWarping to "+ show +"!"));
				p.teleport(warps.get(command));
			} else {
				p.sendMessage(u.chat("&4&oWarp "+ command +" doesn't exist!"));
			}
		} else {
			p.sendMessage(u.chat("&4&oWarp "+ command +" doesn't exist!"));
		}
	}
	
	public Location getLocation(Player p, String[] args) {
		Double x = null, y = null, z = null;
		if(args[1].equals("~")) {
			x = p.getLocation().getX();
		} else {
			x = Double.parseDouble(args[1]);
		}
		if(args[2].equals("~")) {
			y = p.getLocation().getY();
		} else {
			y = Double.parseDouble(args[2]);
		}
		if(args[3].equals("~")) {
			z = p.getLocation().getZ();
		} else {
			z = Double.parseDouble(args[3]);
		}
		return new Location(p.getWorld(), x, y, z);
	}
 }
