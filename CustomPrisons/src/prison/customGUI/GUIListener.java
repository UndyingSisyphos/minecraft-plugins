package prison.customGUI;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import prison.customWarps.WarpExecutor;
import prison.customWarps.WarpManager;
import prison.main.Main;
import prison.main.Utils;

@SuppressWarnings({"unused"})
public class GUIListener implements Listener{
	
	private Main main = null;
	private Utils u = null;
	private GUIUtils gu = null;
	private GUIManager gm = null;
	private WarpManager wm = null;
	
	public GUIListener(Main main) {
		this.main = main;
		main.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) main);
		u = Utils.getInstance();
		gu = GUIUtils.getInstance(main);
		gm = GUIManager.getInstance(main);
		wm = WarpManager.getInstance(main);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		
		Player p = (Player) e.getWhoClicked();
		if(e.getClickedInventory() != null) {
			if(e.getClickedInventory().equals(gm.getInv("warps", p))) {
				ItemStack clickedItem = e.getCurrentItem();
				e.setCancelled(true);
				if(!clickedItem.equals(gu.placeholder())) {
					if(clickedItem.getType().equals(Material.NETHER_STAR)) {
						wm.tp(p, "spawn");
					} else if(clickedItem.getType().equals(Material.CHEST)) {
						wm.tp(p, "crates");
					} else if(clickedItem.getType().equals(Material.COBBLESTONE)) {
						p.openInventory(gm.getInv("mines", p));
					} else if(clickedItem.getType().equals(Material.GRASS)) {
						// Show plots GUI																													Plots GUI
					} else if(clickedItem.getItemMeta().getDisplayName().equals(u.chat("&cBack"))) {
						p.closeInventory();
					}
				}
				
			} else if(e.getClickedInventory().equals(gm.getInv("mines", p))) {
				ItemStack clickedItem = e.getCurrentItem();
				e.setCancelled(true);
				if(!clickedItem.equals(gu.placeholder())) {
					if(clickedItem.getType().equals(Material.GRASS)) {
						p.openInventory(gm.getInv("standard_mines", p));
					} else if(clickedItem.getType().equals(Material.BEDROCK)) {
						p.openInventory(gm.getInv("prestige_mines", p));
					} else if(clickedItem.getType().equals(Material.DIAMOND_BLOCK)) {
						p.openInventory(gm.getInv("donator_mines", p));
					} else if(clickedItem.getType().equals(Material.DIAMOND_SWORD)) {
						wm.tp(p, "pvpmine");
					} else if(clickedItem.getItemMeta().getDisplayName().equals(u.chat("&cBack"))) {
						p.openInventory(gm.getInv("warps", p));
					}
				}
			} else if(e.getClickedInventory().equals(gm.getInv("standard_mines", p))) {
				ItemStack clickedItem = e.getCurrentItem();
				e.setCancelled(true);
				if(!clickedItem.equals(gu.placeholder())) {
					if(clickedItem.getItemMeta().getDisplayName().equals(u.chat("&cBack"))) {
						p.openInventory(gm.getInv("mines", p));
					} else if(clickedItem.getType().equals(Material.GOLDEN_APPLE)) {
						// Rankup command																										            Rankup command
					} else if(clickedItem.getType().equals(Material.IRON_FENCE)) {
						p.sendMessage(u.chat("&c&oYour rank is not high enough!"));
					} else if(clickedItem.getType().equals(Material.LOG)) {
						wm.tp(p, "a");
					}
				}
			} else if(e.getClickedInventory().equals(gm.getInv("prestige_mines", p))) {
				ItemStack clickedItem = e.getCurrentItem();
				e.setCancelled(true);
				if(!clickedItem.equals(gu.placeholder())) {
					if(clickedItem.getItemMeta().getDisplayName().equals(u.chat("&cBack"))) {
						p.openInventory(gm.getInv("mines", p));
					} else if(clickedItem.getType().equals(Material.GOLDEN_APPLE)) {
						// Prestige command																										            Prestige command
					} else if(clickedItem.getType().equals(Material.IRON_FENCE)) {
						p.sendMessage(u.chat("&c&oYour prestige is not high enough!"));
					} else if(clickedItem.getType().equals(Material.BEDROCK)) {
						if(clickedItem.getItemMeta().getDisplayName().equals(u.chat("&aP1"))) {
							wm.tp(p, "p1");
						} else if(clickedItem.getItemMeta().getDisplayName().equals(u.chat("&aP2"))) {
							wm.tp(p, "p2");
						} else if(clickedItem.getItemMeta().getDisplayName().equals(u.chat("&aP3"))) {
							wm.tp(p, "p3");
						} else if(clickedItem.getItemMeta().getDisplayName().equals(u.chat("&aP4"))) {
							wm.tp(p, "p4");
						} else if(clickedItem.getItemMeta().getDisplayName().equals(u.chat("&aP5"))) {
							wm.tp(p, "p5");
						} else if(clickedItem.getItemMeta().getDisplayName().equals(u.chat("&aP6"))) {
							wm.tp(p, "p6");
						} else if(clickedItem.getItemMeta().getDisplayName().equals(u.chat("&aP7"))) {
							wm.tp(p, "p7");
						} else if(clickedItem.getItemMeta().getDisplayName().equals(u.chat("&aP8"))) {
							wm.tp(p, "p8");
						} else if(clickedItem.getItemMeta().getDisplayName().equals(u.chat("&aP9"))) {
							wm.tp(p, "p9");
						} else if(clickedItem.getItemMeta().getDisplayName().equals(u.chat("&aP10"))) {
							wm.tp(p, "p10");
						}
					}
				}
			}
		}
	}
}
