package prison.customGUI;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Command;
import org.bukkit.plugin.Plugin;

import prison.main.Main;
import prison.main.Utils;

@SuppressWarnings({"unused" })
public class CustomGUI {
	
	private Main main = null;
	private Utils u = null;
	private GUIUtils gu = null;
	private Inventory inv = null;
	private String inv_name = null;
	private int inv_rows;
	private int inv_slots;
	private boolean initialized = false;
	
	// Override
	public CustomGUI(Main main) {
		this.main = main;
	}
	
	// Override
	public CustomGUI(Main main, String inv_name, int inv_rows) {
		this.main = main;
		u = Utils.getInstance();
		gu = GUIUtils.getInstance(main);
		this.inv_name = inv_name;
		this.inv_rows = inv_rows;
		this.inv_slots = inv_rows * 9;
		inv = Bukkit.createInventory(null, inv_slots, this.inv_name);
	}
	
	public void addItems(int pos, ItemStack... items) {
		for(ItemStack item: items) {
			inv.setItem(pos, item);
			pos++;
		}
	}
	
	public void setItem(ItemStack item, int pos) {
		inv.setItem(pos, item);
	}
	
	public Inventory getInv() {
		return inv;
	}
	public void setInv(Inventory inv) {
		this.inv = inv;
	}
	
	public String getInv_name() {
		return inv_name;
	}
	public void setInv_name(String inv_name) {
		this.inv_name = inv_name;
	}
	
	public int getInv_rows() {
		return inv_rows;
	}
	public void setInv_rows(int inv_rows) {
		this.inv_rows = inv_rows;
	}
	
	public int getInv_slots() {
		return inv_slots;
	}
	public void setInv_slots(int inv_slots) {
		this.inv_slots = inv_slots;
	}

	
	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
}
