package prison.customGUI;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import prison.main.Main;
import prison.main.Utils;

@SuppressWarnings({"unused" })
public class GUIUtils {
	
	private static GUIUtils instance = null;
	private Main main = null;
	private Utils u = null;
	
	public GUIUtils(Main main) {
		this.main = main;
		u = Utils.getInstance();
	}
	
	public static GUIUtils getInstance(Main main) {
		if(instance == null) {
			instance = new GUIUtils(main);
		}
		return instance;
	}
	
	public ItemStack placeholder() {
		return u.createItem(Material.STAINED_GLASS_PANE, 1, 0, u.chat(" "));
	}
	
	public CustomGUI fillGUI(CustomGUI gui) {
		for(int c = 0; c < gui.getInv_slots(); c++) {
			gui.setItem(placeholder(), c);
		}
		return gui;
	}
}
