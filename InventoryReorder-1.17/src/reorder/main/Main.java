package reorder.main;

import java.io.*;
import java.util.*;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

	private int c = 0;
	Utils u = null;

	private String pluginVersion = "2.2.0";
	private boolean enableDefaultExport = true;
	private boolean enableCustomMaps = false;
	private String activeMap = null;
	private boolean pluginLoaded = false;

	private Map<Material,Integer> baseDefaultMap = null;
	private TreeMap<Integer,Material> reverseDefaultMap = null;
	private ArrayList<String> customMapsNames = null;
	private ArrayList<CustomMap> customMaps = null;

	private final String currentPath = new java.io.File(".").getCanonicalPath();
	private final String csvPath = "/default-sorting-map.csv";
	private final String configPath = currentPath + "\\plugins\\InventoryReorder\\config.yml";
	private final String defaultMapPath = currentPath + "\\plugins\\InventoryReorder\\default-sorting-map.yml";

	private InventoryListener il = null;

	public Main() throws IOException {}

	@Override
	public void onEnable() {
		u = Utils.getInstance();
		if(importConfig()) {
			System.out.println(u.getChatPrefix() + "Config file loaded!");
		}
		if(importDefaultMap()) {
			System.out.println(u.getChatPrefix() + "Default sorting map loaded!");
		} else {
			System.out.println(u.getChatPrefix() + "Stopping the plugin loading procedure.");
			return;
		}
		if(enableCustomMaps) {
			if(importCustomMaps()) {
				System.out.println(u.getChatPrefix() + "Custom sorting maps loaded!");
			}
		}
		System.out.println(u.getChatPrefix() + "All ID mappings have been successfully loaded!");

		il = new InventoryListener(this, getActiveBaseMap());
		this.getServer().getPluginManager().registerEvents((Listener) il, (Plugin) this);
		System.out.println(u.getChatPrefix() + "Inventory listener loaded!");

		this.getCommand("hello").setExecutor(this);

		for(Material m: Material.values()) {
			System.out.println(m);
		}

		pluginLoaded = true;
	}
	
	@Override
	public void onDisable() {
		if(pluginLoaded) {
			if(enableDefaultExport) {
				exportDefaultMap();
				System.out.println(u.getChatPrefix() + "Exported the default map!");
			}
			if(enableCustomMaps) {
				exportCustomMaps();
			}
		}
	}

	private boolean importConfig() {
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(configPath);
			br = new BufferedReader(fr);
			System.out.println(u.getChatPrefix() + "Found config file. Reading data from it...");

			String line = null;
			while((line = br.readLine()) != null) {
				if(!line.startsWith("#") && !line.equals("")) {
					line = line.replaceAll(" ", "");
					String[] args = line. split(":");
					switch(args[0]) {
						case("enable-default-map-export"):
							enableDefaultExport = Boolean.parseBoolean(args[1]);
							break;
						case("enable-custom-maps-import"):
							enableCustomMaps = Boolean.parseBoolean(args[1]);
							break;
						case("custom-maps-names"):
							args[1] = args[1].replaceAll("\\[", "");
							args[1] = args[1].replaceAll("]", "");
							customMapsNames = new ArrayList<>(Arrays.asList(args[1].split(",")));
						case("active-map"):
							activeMap = args[1].split("\\.")[0];
						default:
							break;
					}
				}
			}

			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			System.out.println(u.getChatPrefix() + "Config file not found. Creating a new one...");
			exportConfig();
		} catch (IOException ignored) {}
		return true;
	}

	private void exportConfig() {
		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			File f = new File(configPath);
			if(f.exists()) {
				System.out.println(u.getChatPrefix() + "Found the config file. Saving changes onto it...");
			} else {
				boolean result = f.getParentFile().mkdirs() && f.createNewFile();
			}

			fw = new FileWriter(configPath);
			bw = new BufferedWriter(fw);

			bw.write("# Plugin installed version. Changing it will not have any effect on the plugin itself\n");
			bw.write("version: " + pluginVersion + "\n");

			bw.write("\n# Enables the export on file of the default sorting-map hard-coded into the plugin, if true it gets exported at every plugin unload\n");
			bw.write("enable-default-map-export: " + enableDefaultExport + "\n");

			bw.write("\n# Enables the import of customizations through custom '.yml' files\n");
			bw.write("enable-custom-maps-import: " + enableCustomMaps + "\n");

			bw.write("\n# List of the names of the '.yml' files of the custom sorting mappings\n# You can write the names with or without the extension, it doesn't matter\n# But be careful not to put dots in the name, everything past a dot will not be considered\n# Divide the names with commas (',') but don't change line, spaces will be ignored\n");
			if(customMapsNames != null) {
				bw.write("custom-maps-names: " + customMapsNames + "\n");
			} else {
				bw.write("custom-maps-names: []\n");
			}


			bw.write("\n# The name of the active custom sorting mapping, as before it can have or not the extension, it doesn't matter\n");
			if(activeMap == null) {
				activeMap = "default";
			}
			bw.write("active-map: " + activeMap + "\n");

			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException ignored) {}
	}

	private void exportCSV() {
		try {
			File f = new File(csvPath);
			if(!f.exists()) {
				boolean result = f.createNewFile();
			}

			FileWriter fw = new FileWriter(csvPath);
			BufferedWriter bw = new BufferedWriter(fw);

			for(Map.Entry<Integer,Material> entry: reverseDefaultMap.entrySet()) {
				bw.write(entry.getValue().toString().toLowerCase() + "\n");
			}

			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException ignored) {
			System.out.println("IOException");
		}
	}

	private boolean importDefaultMap() {

		InputStream in = null;
		BufferedReader br = null;

		try {
			in = getClass().getResourceAsStream(csvPath);
			br = new BufferedReader(new InputStreamReader(in));
			System.out.println(u.getChatPrefix() + "Found default sorting map file. Reading data from it...");

			c = 0;
			baseDefaultMap = new HashMap<>();
			reverseDefaultMap = new TreeMap<>();
			String line = null;
			while((line = br.readLine()) != null) {
				line = line.strip();
				Material m = Material.getMaterial(line.toUpperCase());
				baseDefaultMap.put(m, c);
				reverseDefaultMap.put(c, m);
				c++;
			}

			br.close();
			in.close();
		} catch (NullPointerException e) {
			System.out.println(u.getChatPrefix() +  u.getChatFatalError() + "Default sorting map file not found. The plugin will not work without. If you didn't modify the jar file try to download it again from spigotmc.org or contact the author.");
			return false;
		} catch (IOException ignored) {
			return false;
		}
		return true;
	}

	private void exportDefaultMap() {
		try {
			File f = new File(defaultMapPath);
			if(f.exists()) {
				System.out.println(u.getChatPrefix() + "Found the default sorting-map file. Saving changes onto it...");
			} else {
				System.out.println(u.getChatPrefix() + "The default sorting-map file doesn't exist in the default directory or it couldn't be recognized. Creating a new one...");
				boolean result = f.getParentFile().mkdirs() && f.createNewFile();
			}

			FileWriter fw = new FileWriter(defaultMapPath);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write("# Modifying this file will not have any effect on the plugin\n# This is just a guide for you to understand how to write your custom mapping of the items to the IDs\n# You can disable the export of this file in the config file\n# By doing so, you can remove this file or modify it and it will not return to the original state until you re-activate the exporting option\n\n");
			for(Map.Entry<Integer,Material> entry: reverseDefaultMap.entrySet()) {
				bw.write(entry.getValue().toString().toLowerCase() + ": " + entry.getKey() + "\n");
			}

			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException ignored) {}
	}

	private boolean importCustomMaps() {
		customMaps = new ArrayList<>();
		for(String s: customMapsNames) {
			CustomMap cm = new CustomMap(s.split("\\.")[0]);
			if(cm.importMap(baseDefaultMap, reverseDefaultMap)) {
				customMaps.add(cm);
			}
		}
		return true;
	}

	private void exportCustomMaps() {
		for(CustomMap cm: customMaps) {
			if(cm.getReverse() != null) {
				cm.exportMap();
				System.out.println(u.getChatPrefix() + "Exported the map '" + cm.getName() + "'...");
			}
		}
		System.out.println(u.getChatPrefix() + "Exported all the custom maps!");
	}

	public Map<Material,Integer> getActiveBaseMap() {
		if(activeMap == null) {
			System.out.println(u.getChatPrefix() + "No sorting map was specified to be active. The default sorting map is now the active one.");
			activeMap = "default-sorting-map";
			return baseDefaultMap;
		}

		if(enableCustomMaps) {
			for(CustomMap cm: customMaps) {
				if(activeMap.equals(cm.getName())) {
					System.out.println(u.getChatPrefix() + "The map '" + cm.getName() + "' has been successfully set as the current sorting map!");
					return cm.getCompleteBase();
				}
			}
			System.out.println(u.getChatPrefix() + "The specified sorting map was not found. The default sorting map is now the active one.");
			activeMap = "default-sorting-map";
			return baseDefaultMap;
		}

		if(activeMap != "default-sorting-map") {
			System.out.println(u.getChatPrefix() + "The custom sorting maps are disabled, you can enable them in 'config.yml'. The default sorting map is now the active one.");
			activeMap = "default-sorting-map";
		}

		return baseDefaultMap;
	}

	public TreeMap<Integer,Material> getActiveReverseMap() {
		if(activeMap == null) {
			System.out.println(u.getChatPrefix() + "No active map was specified. The default map is now the active one.");
			activeMap = "default-sorting-map";
			return reverseDefaultMap;
		}

		if(enableCustomMaps) {
			for(CustomMap cm: customMaps) {
				if(activeMap.equals(cm.getName())) {
					System.out.println(u.getChatPrefix() + "The map '" + cm.getName() + "' has been successfully set as the current sorting map.");
					return cm.getCompleteReverse();
				}
			}
			System.out.println(u.getChatPrefix() + "The specified sorting map was not found. The default sorting map is now the active one.");
			activeMap = "default-sorting-map";
			return reverseDefaultMap;
		}

		if(activeMap != "default-sorting-map") {
			System.out.println(u.getChatPrefix() + "The custom sorting maps are disabled, you can enable them in 'config.yml'. The default sorting map is now the active one.");
			activeMap = "default-sorting-map";
		}

		return reverseDefaultMap;
	}

	public String getActiveMap() {
		return activeMap;
	}

	public void setActiveMap(String name) {
		this.activeMap = name;
	}
}