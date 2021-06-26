package prison.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import prison.customEconomy.EconomyManager;
import prison.customEconomy.EconomyUtils;
import prison.customEnchants.CustomEnchantmentsManager;
import prison.customGUI.CustomGUI;
import prison.customGUI.GUIListener;
import prison.customGUI.GUIManager;
import prison.customWarps.WarpExecutor;
import prison.customWarps.WarpManager;

@SuppressWarnings({"unused"})
public class Main extends JavaPlugin implements Listener {
	
	private BukkitScheduler scheduler = null;
	private Utils u = null;
	private FilesManager fm = null;
	private GUIManager gm = null;
	private WarpManager wm = null;
	private CustomEnchantmentsManager cem = null;
	private EconomyManager em = null;
	
	@Override
	public void onEnable() {
		u = Utils.getInstance();
		fm = FilesManager.getInstance(this);
		scheduler = getServer().getScheduler();
		em = EconomyManager.getInstance(this);
		cem = CustomEnchantmentsManager.getInstance(this);
		gm = GUIManager.getInstance(this);
		wm = WarpManager.getInstance(this);
	}
	
	@Override
	public void onDisable() {
		fm.writeWarps(wm.getWarps());
		fm.writePlayersData(em.getAllData());
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			
		}
		return false;
	}
	
	public BukkitScheduler getScheduler() {
		return scheduler;
	}
}
