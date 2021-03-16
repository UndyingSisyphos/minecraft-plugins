package prison.customWarps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import prison.customGUI.GUIManager;
import prison.customGUI.GUIUtils;
import prison.main.Main;
import prison.main.Utils;

@SuppressWarnings({"unused"})
public class WarpExecutor implements CommandExecutor {
	
	private Main main = null;
	private Utils u = null;
	private GUIManager gm = null;
	private WarpManager wm = null;
	
	public WarpExecutor(Main main) {
		this.main = main;
		u = Utils.getInstance();
		gm = GUIManager.getInstance(main);
		wm = WarpManager.getInstance(main);
		initCommands();
	}
	
	private void initCommands() {
		main.getCommand("spawn").setExecutor(this);
		main.getCommand("crates").setExecutor(this);
		main.getCommand("pvpmine").setExecutor(this);
		main.getCommand("warp").setExecutor(this);
		main.getCommand("setwarp").setExecutor(this);
		main.getCommand("delwarp").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			switch(cmd.getName()) {
			case("warp"):
				switch(args.length) {
				case(0):
					p.openInventory(gm.getInv("warps", p));
					return true;
				case(1):
					wm.tp(p, args[0]);
					return true;
				default:
					p.sendMessage(u.chat("&4&oError! Too many arguments!"));
					return false;
				}
			case("setwarp"):
				if(args.length == 2 || args.length == 4) {
					wm.createWarp(p, args);
					return true;
				} else {
					p.sendMessage(u.chat("&4&oError! Too many (or too few) arguments!"));
					return false;
				}
			case("delwarp"):
				if(args.length == 1) {
					wm.removeWarp(p, args[0]);
					return true;
				} else {
					p.sendMessage(u.chat("&4&oError! Too manyies arguments!"));
					return false;
				}
			default:
				p.sendMessage(u.chat("&4&oError! Command doesn't exist!"));
				return false;
			}
		}
		return false;
	}
}
