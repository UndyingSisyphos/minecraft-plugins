package prison.customWarps;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import prison.customGUI.CustomGUI;
import prison.customGUI.GUICreator;
import prison.customGUI.GUIUtils;
import prison.main.Main;
import prison.main.Utils;

@SuppressWarnings({"unused" })
public class WarpGUI implements GUICreator {
	
	private Main main = null;
	private Utils u = null;
	private GUIUtils gu = null;
	
	private CustomGUI warps = null;
	private CustomGUI mines = null;
	private CustomGUI standard_mines = null;
	private CustomGUI prestige_mines = null;
	private CustomGUI donator_mines = null;
	private CustomGUI plots = null;
	
	public WarpGUI(Main main) {
		this.main = main;
		u = Utils.getInstance();
		gu = GUIUtils.getInstance(main);
		createGUIs();
	}
	
	@Override
	public void createGUIs() {
		warps = gu.fillGUI(new CustomGUI(main, u.chat("&0Warps"), 3));
		mines = gu.fillGUI(new CustomGUI(main, u.chat("&0Mines"), 3));
		standard_mines = gu.fillGUI(new CustomGUI(main, u.chat("&0Mines A-Z"), 6));
		prestige_mines = gu.fillGUI(new CustomGUI(main, u.chat("&0Prestige Mines"), 4));
		donator_mines = gu.fillGUI(new CustomGUI(main, u.chat("&0Donator Mines"), 3));
		plots = gu.fillGUI(new CustomGUI(main, u.chat("&0Plots"), 3));
	}
	
	@Override
	public HashMap<String,GUICreator> getGUIs() {
		HashMap<String,GUICreator> fullList = new HashMap<String,GUICreator>();
		fullList.put("warps", this);
		fullList.put("mines", this);
		fullList.put("standard_mines", this);
		fullList.put("prestige_mines", this);
		fullList.put("donator_mines", this);
		fullList.put("plots", this);
		return fullList;
	}
	
	@Override
	public CustomGUI getGUI(String name, Player p) {
		switch(name) {
		case("warps"):
			return getWarps();
		case("mines"):
			return getMines();
		case("standard_mines"):
			return getStandardMines(p);
		case("prestige_mines"):
			return getPrestigeMines(p);
		case("donator_mines"):
			return getDonatorMines(p);
		case("plots"):
			return getPlots();
		default:
			return null;
		}
	}
	
	// Generic Warps GUI Edit
	public CustomGUI getWarps() {
		if(!warps.isInitialized()) {
			warps.setItem(u.createItem(Material.NETHER_STAR, 1, u.chat("&bSpawn")), 10);
			warps.setItem(u.createItem(Material.CHEST, 1, u.chat("&aCrates")), 12);
			warps.setItem(u.createItem(Material.COBBLESTONE, 1, u.chat("&eMines")), 14);
			warps.setItem(u.createItem(Material.GRASS, 1, u.chat("&6Plots")), 16);
			warps.setItem(u.createItem(Material.STAINED_GLASS_PANE, 1, 14, u.chat("&cBack")), 18);
			warps.setInitialized(true);
		}
		return warps;
	}
	public void setWarps(CustomGUI warps) {
		this.warps = warps;
	}
	
	// Generic Mines GUI Edit
	public CustomGUI getMines() {
		if(!mines.isInitialized()) {
			mines.setItem(u.createItem(Material.GRASS, 1, u.chat("&aA-Z Mines")), 10);
			mines.setItem(u.createItem(Material.BEDROCK, 1, u.chat("&bPrestige Mines")), 12);
			mines.setItem(u.createItem(Material.DIAMOND_BLOCK, 1, u.chat("&6Donator Mines")), 14);
			mines.setItem(u.createItem(Material.DIAMOND_SWORD, 1, u.chat("&cPvP Mine"), true), 16);
			mines.setItem(u.createItem(Material.STAINED_GLASS_PANE, 1, 14, u.chat("&cBack")), 18);
			mines.setInitialized(true);
		}
		return mines;
	}
	
	public void setMines(CustomGUI mines) {
		this.mines = mines;
	}
	
	public CustomGUI getStandardMines(Player p) {
		Material m = Material.IRON_FENCE;
		if(p.hasPermission("warp.mine.a")) {
			standard_mines.setItem(u.createItem(Material.LOG, 1, u.chat("&aA"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 10);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cA"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 10);
		}
		if(p.hasPermission("warp.mine.b")) {
			standard_mines.setItem(u.createItem(Material.COBBLESTONE, 1, u.chat("&aB"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 11);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cB"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 11);
		}
		if(p.hasPermission("warp.mine.c")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aC"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 12);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cC"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 12);
		}
		if(p.hasPermission("warp.mine.d")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aD"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 13);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cD"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 13);
		}
		if(p.hasPermission("warp.mine.e")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aE"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 14);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cE"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 14);
		}
		if(p.hasPermission("warp.mine.f")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aF"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 15);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cF"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 15);
		}
		if(p.hasPermission("warp.mine.g")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aG"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 16);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cG"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 16);
		}
		if(p.hasPermission("warp.mine.h")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aH"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 19);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cH"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 19);
		}
		if(p.hasPermission("warp.mine.i")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aI"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 20);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cI"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 20);
		}
		if(p.hasPermission("warp.mine.j")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aJ"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 21);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cJ"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 21);
		}
		if(p.hasPermission("warp.mine.k")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aK"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 22);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cK"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 22);
		}
		if(p.hasPermission("warp.mine.l")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aL"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 23);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cL"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 23);
		}
		if(p.hasPermission("warp.mine.m")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aM"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 24);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cM"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 24);
		}
		if(p.hasPermission("warp.mine.n")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aN"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 25);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cN"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 25);
		}
		if(p.hasPermission("warp.mine.o")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aO"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 28);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cO"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 28);
		}
		if(p.hasPermission("warp.mine.p")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aP"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 29);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cP"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 29);
		}
		if(p.hasPermission("warp.mine.q")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aQ"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 30);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cQ"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 30);
		}
		if(p.hasPermission("warp.mine.r")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aR"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 31);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cR"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 31);
		}
		if(p.hasPermission("warp.mine.s")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aS"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 32);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cS"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 32);
		}
		if(p.hasPermission("warp.mine.t")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aT"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 33);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cT"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 33);
		}
		if(p.hasPermission("warp.mine.u")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aU"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 34);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cU"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 34);
		}
		if(p.hasPermission("warp.mine.v")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aV"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 38);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cV"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 38);
		}
		if(p.hasPermission("warp.mine.w")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aW"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 39);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cW"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 39);
		}
		if(p.hasPermission("warp.mine.x")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aX"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 40);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cX"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 40);
		}
		if(p.hasPermission("warp.mine.y")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aY"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 41);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cY"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 41);
		}
		if(p.hasPermission("warp.mine.z")) {
			standard_mines.setItem(u.createItem(Material.STONE, 1, u.chat("&aZ"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 42);
		} else {
			standard_mines.setItem(u.createItem(m, 1, u.chat("&cZ"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 42);
		}
		standard_mines.setItem(u.createItem(Material.STAINED_GLASS_PANE, 1, 14, u.chat("&cBack")), 45);
		standard_mines.setItem(u.createItem(Material.GOLDEN_APPLE, 1, 0, u.chat("&6Max Rankup"), u.chat(""), u.chat("&fCost: ")), 53);
		return standard_mines;
	}
	
	public void setStandardMines(CustomGUI standard_mines) {
		this.standard_mines = standard_mines;
	}
	
	public CustomGUI getPrestigeMines(Player p) {
		Material m = Material.IRON_FENCE;
		Material b = Material.BEDROCK;
		if(p.hasPermission("warp.mine.p1")) {
			prestige_mines.setItem(u.createItem(b, 1, u.chat("&aP1"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 10);
		} else {
			prestige_mines.setItem(u.createItem(m, 1, u.chat("&cP1"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 11);
		}
		if(p.hasPermission("warp.mine.p2")) {
			prestige_mines.setItem(u.createItem(b, 1, u.chat("&aP2"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 11);
		} else {
			prestige_mines.setItem(u.createItem(m, 1, u.chat("&cP2"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 12);
		}
		if(p.hasPermission("warp.mine.p3")) {
			prestige_mines.setItem(u.createItem(b, 1, u.chat("&aP3"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 12);
		} else {
			prestige_mines.setItem(u.createItem(m, 1, u.chat("&cP3"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 13);
		}
		if(p.hasPermission("warp.mine.p4")) {
			prestige_mines.setItem(u.createItem(b, 1, u.chat("&aP4"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 13);
		} else {
			prestige_mines.setItem(u.createItem(m, 1, u.chat("&cP4"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 14);
		}
		if(p.hasPermission("warp.mine.p5")) {
			prestige_mines.setItem(u.createItem(b, 1, u.chat("&aP5"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 13);
		} else {
			prestige_mines.setItem(u.createItem(m, 1, u.chat("&cP5"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 15);
		}
		if(p.hasPermission("warp.mine.p6")) {
			prestige_mines.setItem(u.createItem(b, 1, u.chat("&aP6"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 10);
		} else {
			prestige_mines.setItem(u.createItem(m, 1, u.chat("&cP6"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 20);
		}
		if(p.hasPermission("warp.mine.p7")) {
			prestige_mines.setItem(u.createItem(b, 1, u.chat("&aP7"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 11);
		} else {
			prestige_mines.setItem(u.createItem(m, 1, u.chat("&cP7"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 21);
		}
		if(p.hasPermission("warp.mine.p8")) {
			prestige_mines.setItem(u.createItem(b, 1, u.chat("&aP8"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 12);
		} else {
			prestige_mines.setItem(u.createItem(m, 1, u.chat("&cP8"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 22);
		}
		if(p.hasPermission("warp.mine.p9")) {
			prestige_mines.setItem(u.createItem(b, 1, u.chat("&aP9"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 13);
		} else {
			prestige_mines.setItem(u.createItem(m, 1, u.chat("&cP9"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 23);
		}
		if(p.hasPermission("warp.mine.p10")) {
			prestige_mines.setItem(u.createItem(b, 1, u.chat("&aP10"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 13);
		} else {
			prestige_mines.setItem(u.createItem(m, 1, u.chat("&cP10"), u.chat("&c&lLocked!&r"), u.chat("&7Unlock cost: &r"), u.chat(""), u.chat("&fAvg &6$ &fper inventory: ")), 24);
		}
		prestige_mines.setItem(u.createItem(Material.STAINED_GLASS_PANE, 1, 14, u.chat("&cBack")), 27);
		prestige_mines.setItem(u.createItem(Material.GOLDEN_APPLE, 1, 1, u.chat("&6Prestige"), u.chat(""), u.chat("&fCost: ")), 35);
		return prestige_mines;
	}
	public void setPrestigeMines(CustomGUI prestige_mines) {
		this.prestige_mines = prestige_mines;
	}
	
	public CustomGUI getDonatorMines(Player p) {
		
		return donator_mines;
	}
	public void setDonatorMines(CustomGUI donator_mines) {
		this.donator_mines = donator_mines;
	}
	
	public CustomGUI getPlots() {
		
		return plots;
	}
	public void setPlots(CustomGUI plots) {
		this.plots = plots;
	}
}
