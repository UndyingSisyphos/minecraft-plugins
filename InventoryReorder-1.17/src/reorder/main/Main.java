package reorder.main;

import java.io.*;
import java.util.*;

import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

	private Utils u = null;
	private TablesManager tm = null;
	private IRCommandExecutor ce = null;

	private final String pluginVersion = "2.2.1";
	private boolean enableDefaultExport = true;
	private boolean enableCustomTables = false;
	private ArrayList<String> customMapsNames = null;
	private String activeTableName = null;
	private boolean enableCustomFilledExport = false;
	private boolean pluginLoaded = false;

	private final String defaultTableName = "default-table";
	private String localPath = null;
	private String configPath = null;


	public Main() {}

	@Override
	public void onEnable() {

		u = Utils.getInstance();

		if(importConfig()) {
			System.out.println(u.getChatPrefix() + "Config file loaded!");
		}

		tm = TablesManager.getInstance(this);
		if(!tm.isImportSuccess()) {
			return;
		}

		ce = new IRCommandExecutor(this);
		System.out.println(u.getChatPrefix() + "Command executor loaded!");

		pluginLoaded = true;
	}
	
	@Override
	public void onDisable() {
		if(pluginLoaded) {
			if(enableDefaultExport) {
				tm.exportDefaultTable();
			}
			if(enableCustomTables) {
				tm.exportCustomTables();
			}
		}
	}

	private boolean importConfig() {
		FileReader fr = null;
		BufferedReader br = null;

		try {
			localPath = new File(".").getCanonicalPath() + "\\plugins\\InventoryReorder\\";
			configPath = localPath + "config.yml";

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
							enableCustomTables = Boolean.parseBoolean(args[1]);
							break;
						case("custom-maps-names"):
							args[1] = args[1].replaceAll("\\[", "");
							args[1] = args[1].replaceAll("]", "");
							customMapsNames = new ArrayList<>(Arrays.asList(args[1].split(",")));
							customMapsNames.removeAll(Collections.singleton(""));
							break;
						case("active-map"):
							activeTableName = args[1].split("\\.")[0];
							break;
						case("enable-filled-custom-map-export"):
							enableCustomFilledExport = Boolean.parseBoolean(args[1]);
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
			bw.write("enable-custom-maps-import: " + enableCustomTables + "\n");

			bw.write("\n# List of the names of the '.yml' files of the custom sorting mappings\n# You can write the names with or without the extension, it doesn't matter\n# But be careful not to put dots in the name, everything past a dot will not be considered\n# Divide the names with commas (',') but don't change line, spaces will be ignored\n");
			if(customMapsNames != null) {
				bw.write("custom-maps-names: " + customMapsNames + "\n");
			} else {
				bw.write("custom-maps-names: []\n");
			}

			bw.write("\n# The name of the active custom sorting mapping, as before it can have or not the extension, it doesn't matter\n");
			if(activeTableName == null) {
				activeTableName = defaultTableName;
			}
			bw.write("active-map: " + activeTableName + "\n");

			bw.write("\n# Enables the export on file of the custom maps filled with the non-customized values\n");
			bw.write("enable-filled-custom-map-export: " + enableCustomFilledExport + "\n");

			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException ignored) {}
	}



	public String getPluginVersion() {
		return pluginVersion;
	}

	public boolean isEnableDefaultExport() {
		return enableDefaultExport;
	}

	public boolean isEnableCustomTables() {
		return enableCustomTables;
	}

	public ArrayList<String> getCustomTablesNames() {
		return customMapsNames;
	}

	public String getDefaultTableName() {
		return defaultTableName;
	}

	public String getActiveTableName() {
		return activeTableName;
	}

	public boolean isEnableCustomFilledExport() {
		return enableCustomFilledExport;
	}

	public boolean isPluginLoaded() {
		return pluginLoaded;
	}

	public String getLocalPath() {
		return localPath;
	}


	public void setEnableDefaultExport(boolean enableDefaultExport) {
		this.enableDefaultExport = enableDefaultExport;
	}

	public void setEnableCustomTables(boolean enableCustomTables) {
		this.enableCustomTables = enableCustomTables;
	}

	public void setCustomMapsNames(ArrayList<String> customMapsNames) {
		this.customMapsNames = customMapsNames;
	}

	public void setActiveTableName(String activeTableName) {
		this.activeTableName = activeTableName;
	}

	public void setEnableCustomFilledExport(boolean enableCustomFilledExport) {
		this.enableCustomFilledExport = enableCustomFilledExport;
	}

	public void setPluginLoaded(boolean pluginLoaded) {
		this.pluginLoaded = pluginLoaded;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
}