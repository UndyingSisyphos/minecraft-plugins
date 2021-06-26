package prison.customWarps;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import prison.customGUI.GUIManager;
import prison.customRewards.RewardsManager;
import prison.main.FilesManager;
import prison.main.Main;
import prison.main.Utils;

@SuppressWarnings({"unused"})
public class WarpManager {
	
	private static WarpManager instance = null;
	private Main main = null;
	private Utils u = null;
	private WarpUtils wu = null;
	
	private WarpExecutor wex = null;
	private Map<String,Location> warps = null;
	
	public WarpManager(Main main) {
		this.main = main;
		u = Utils.getInstance();
		warps = FilesManager.getInstance(main).readWarps();
		wu = WarpUtils.getInstance(main);
		wex = new WarpExecutor(main, this);
	}
	
	public static WarpManager getInstance(Main main) {
		if(instance == null) {
			instance = new WarpManager(main);
		}
		return instance;
	}
	
	public void createWarp(Player p, String[] args) {
		String name = args[0];
		Location l = null;
		
		if(args[1].equals("here")) {
			l = p.getLocation();
			Double x = (double) ((int) l.getX()/1);
			Double y = (double) ((int) l.getY()/1);
			Double z = (double) ((int) l.getZ()/1);
			if(Math.abs(l.getX()%1) >= 0.15 && Math.abs(l.getX()%1) <= 0.85) {
				if(l.getX() >= 0) {
					x += 0.5;
				} else {
					x -= 0.5;
				}
			} else if(Math.abs(l.getX()%1) > 0.85) {
				if(l.getX() >= 0) {
					x += 1;
				} else {
					x -= 1;
				}
			}
			if(Math.abs(l.getY()%1) >= 0.25 && Math.abs(l.getY()%1) <= 0.75) {
				y += 0.5;
			} else if(Math.abs(l.getY()%1) > 0.75) {
				y += 1;
			}
			if(Math.abs(l.getZ()%1) >= 0.15 && Math.abs(l.getZ()%1) <= 0.85) {
				if(l.getZ() >= 0) {
					z += 0.5;
				} else {
					z -= 0.5;
				}
			} else if(Math.abs(l.getZ()%1) > 0.85) {
				if(l.getZ() >= 0) {
					z += 1;
				} else {
					z -= 1;
				}
			}
			l = new Location(p.getWorld(), x, y, z);
		} else {
			l = wu.getLocation(p, args);
		}
		
		warps.put(name, l);
		p.sendMessage(u.chat("&7&oWarp "+ name +" has been set at:"));
		p.sendMessage(u.chat("&7&oX: "+l.getX()+";     Y: "+l.getY()+";     Z: "+l.getZ()));
	}
	
	public void removeWarp(Player p, String name) {
		if(warps.containsKey(name)) {
			warps.remove(name);
			p.sendMessage(u.chat("&7&oWarp "+ name +" has been removed succesfully!"));
		} else {
			p.sendMessage(u.chat("&4&oWarp "+ name +" doesn't exist!"));
		}
		
	}
	
	public void tp(Player p, String command) {
		
		if(command.equals("pvp")) {
			command += "mine";
		}
		
		String show = null;
		if(command.equals("pvpmine")) {
			show = "PvP Mine";
		} else {
			show = command.substring(0, 1).toUpperCase() + command.substring(1);
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
	
	public Map<String,Location> getWarps() {
		return warps;
	}
}
