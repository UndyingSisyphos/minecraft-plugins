package prison.customGUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import prison.customWarps.WarpGUI;
import prison.main.FilesManager;
import prison.main.Main;
import prison.main.Utils;

@SuppressWarnings({"unused"})
public class GUIManager {
	
	private static GUIManager instance = null;
	private Main main = null;
	private Utils u = null;
	
	private GUIListener  gl = null;
	private WarpGUI warps = null;
	private Map<String,GUICreator> all_guis = null;
	
	public GUIManager(Main main) {
		this.main = main;
		u = Utils.getInstance();
		gl = new GUIListener(main, this);
		warps = new WarpGUI(main);
		all_guis = new HashMap<String,GUICreator>();
		initGUIs();
	}
	
	public static GUIManager getInstance(Main main) {
		if(instance == null) {
			instance = new GUIManager(main);
		}
		return instance;
	}
	
	public void initGUIs() {
		all_guis.putAll(warps.getGUIs());
	}
	
	public CustomGUI getGUI(String name, Player p) {
		if(all_guis.containsKey(name)) {
			return all_guis.get(name).getGUI(name, p);
		} else {
			p.sendMessage(u.chat("&4&oThe requested GUI doesn't exist!"));
			return all_guis.get("warps").getGUI(name, p);
		}
	}
	
	public Inventory getInv(String name, Player p) {
		if(all_guis.containsKey(name)) {
			return all_guis.get(name).getGUI(name, p).getInv();
		} else {
			p.sendMessage(u.chat("&4&oThe requested inventory doesn't exist!"));
			return all_guis.get("warps").getGUI(name, p).getInv();
		}
	}
}
