package reorder.main;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.*;

public class TablesManager {

    private Main main;
    private static TablesManager instance = null;
    private InventoryListener il = null;

    private Utils u = null;
    private ArrayList<Table> TablesList = null;
    private Table activeTable = null;
    private String defaultTableName = null;
    private boolean importSuccess = true;


    private TablesManager(Main main) {
        this.main = main;
        u = Utils.getInstance();
        il = InventoryListener.getInstance(main);
        TablesList = new ArrayList<>();
        defaultTableName = main.getDefaultTableName();


        if(importTables(main.isEnableCustomTables())) {
            System.out.println(u.getChatPrefix() + "All available sorting tables have been loaded!");
        }

        setActiveTable(main.getActiveTableName());

        main.getServer().getPluginManager().registerEvents((Listener) il, (Plugin) main);
        System.out.println(u.getChatPrefix() + "Inventory listener loaded!");
    }

    public static TablesManager getInstance(Main main) {
        if(instance == null) {
            instance = new TablesManager(main);
        }
        return instance;
    }

    public ArrayList<Table> getTablesList() {
        return TablesList;
    }

    public String getDefaultTableName() {
        return defaultTableName;
    }

    public boolean isImportSuccess() {
        return importSuccess;
    }

    public void setTablesList(ArrayList<Table> tablesList) {
        TablesList = tablesList;
    }

    public void setDefaultTableName(String defaultTableName) {
        this.defaultTableName = defaultTableName;
    }

    public String getInternalPath(String name) {
        return "/" + name + ".csv";
    }

    public String getExternalPath(String name) {
        return main.getLocalPath() + name + ".yml";
    }

    public Table getTable(String name) {
        for(Table t: TablesList) {
            if(t.getName().equalsIgnoreCase(name)) {
                return t;
            }
        }
        return null;
    }


    public boolean importDefaultTable() {

        InputStreamReader in = null;
        BufferedReader br = null;

        Table t = getTable(defaultTableName);
        if(t == null) {
            t = new Table(defaultTableName, getExternalPath(defaultTableName));
        } else {
            TablesList.remove(t);
            t.clear();
        }

        try {
            in = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(getInternalPath(t.getName()))));
            br = new BufferedReader(in);
            System.out.println(u.getChatPrefix() + "Found default sorting table file. Reading data from it...");

            int index = 0;
            String line = null;
            while((line = br.readLine()) != null) {
                if(!line.startsWith("#") && !line.equals("")) {
                    line = line.replaceAll(" ", "");
                    Material material = Material.matchMaterial(line);
                    if(material != null) {
                        t.add(material, index);
                    } else {
                        System.out.println(u.getChatPrefix() + "Material '" + line + "' doesn't exist. There is an error in the default table file. Try to download the latest version of the plugin from spigotmc.org or contact the author.");
                    }
                    index++;
                }
            }


            br.close();
            in.close();
        } catch (NullPointerException e) {
            System.out.println(u.getChatPrefix() +  u.getChatFatalError() + "Default sorting map file not found. The plugin will not work without. Try to download the latest version of the plugin from spigotmc.org or contact the author.");
            return false;
        } catch (IOException ignored) {
            return false;
        }
        TablesList.add(t);
        System.out.println(u.getChatPrefix() + "Default sorting table loaded!");
        return true;
    }

    public boolean importExternalTable(String name) {

        InputStreamReader in = null;
        BufferedReader br = null;

        Table t = getTable(name);
        if(t == null) {
            t = new Table(name, getExternalPath(name));
        } else {
            TablesList.remove(t);
            t.clear();
        }

        try {
            in = new InputStreamReader(new FileInputStream(t.getPath()));
            br = new BufferedReader(in);
            System.out.println(u.getChatPrefix() + "Found '" + t.getFileName() + "' file. Reading data from it...");

            int ln = 1;
            String line = null;
            while((line = br.readLine()) != null) {
                if(!line.startsWith("#") && !line.equals("")) {
                    line = line.replaceAll(" ", "");
                    String[] args = line.split(":");
                    if (args.length == 2) {
                        Material material = Material.matchMaterial(args[0]);
                        int index = Integer.parseInt(args[1]);
                        if(material != null) {
                            t.add(material, index);
                        } else {
                            System.out.println(u.getChatPrefix() + "Material '" + args[0] + "' doesn't exist. Check the spelling with the help of the '" + getTable(defaultTableName).getFileName() + "' file.");
                        }
                    } else {
                        System.out.println(u.getChatPrefix() + u.getChatError() + "Formatting mistake at line " + ln + " in file '" + t.getFileName() + "', too many or too few ':'.");
                    }
                }
                ln++;
            }

            br.close();
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println(u.getChatPrefix() +  u.getChatError() + "Custom sorting table file '" + t.getFileName() + "' not found. Ignoring it...");
            return false;
        } catch (IOException ignored) {
            return false;
        }
        TablesList.add(t);
        System.out.println(u.getChatPrefix() + "Custom sorting table " + t.getName() + ", loaded!");
        return true;
    }

    public void importCustomTables() {
        if(main.getCustomTablesNames().size() == 0) {
            System.out.println(u.getChatPrefix() + "No custom tables found. Ignoring them...");
        }
        boolean all = true;
        boolean oneOrMore = false;
        for(String name: main.getCustomTablesNames()) {
            if(!name.equalsIgnoreCase("")){
                if(importExternalTable(name)) {
                    oneOrMore = true;
                } else {
                    all = false;
                }
            }
        }

        if(oneOrMore) {
            if(all) {
                System.out.println(u.getChatPrefix() + "All custom sorting tables loaded!");
            } else {
                System.out.println(u.getChatPrefix() + "All available custom tables loaded! Check above for error messages on unavailable tables.");
            }
        } else {
            System.out.println(u.getChatPrefix() + "None of the specified custom tables were available. Check above for error messages.");
        }
    }

    public boolean importTables(boolean importCustom) {
        if(!importDefaultTable()) {
            return false;
        }

        if(importCustom) {
            importCustomTables();
        }
        return true;
    }

    public void exportDefaultTable() {
        Table t = getTable(defaultTableName);
        try {
            File f = new File(t.getPath());
            if(f.exists()) {
                System.out.println(u.getChatPrefix() + "Found the default table file. Making sure it's updated...");
            } else {
                System.out.println(u.getChatPrefix() + "The default table file doesn't exist in the default directory or it couldn't be recognized. Creating a new one...");
                boolean result = f.getParentFile().mkdirs() && f.createNewFile();
            }

            FileWriter fw = new FileWriter(t.getPath());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("# Modifying this file will not have any effect on the plugin\n# This is just a guide for you to understand how to write your custom mapping of the items to the IDs\n# You can disable the export of this file in the config file\n# By doing so, you can remove this file or modify it and it will not return to the original state until you re-activate the exporting option\n\n");
            for(Couple couple: t.getList()) {
                bw.write(couple.getMaterial().name().toLowerCase() + ": " + couple.getIndex() + "\n");
            }

            bw.flush();
            bw.close();
            fw.close();
        } catch (IOException ignored) {}
        System.out.println(u.getChatPrefix() + "Default sorting table exported!");
    }

    public void exportExternalTable(Table t) {
        try {
            if(main.isEnableCustomFilledExport()) {
                t = complete(t);
            }

            File f = new File(t.getPath());
            if(f.exists()) {
                System.out.println(u.getChatPrefix() + "Found '" + f.getName() + "' file. Saving changes onto it...");
            } else {
                System.out.println(u.getChatPrefix() + "The custom sorting map file '" + f.getName() + "' couldn't be found. Creating a new one...");
                boolean result = f.getParentFile().mkdirs() && f.createNewFile();
            }

            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("# This is the list of customizations for the map '" + f.getName() + "'\n# The elements that exactly correspond to the default map will not be saved here\n\n");
            for(Couple couple: t.getList()) {
                bw.write(couple.getMaterial().name().toLowerCase() + ": " + couple.getIndex() + "\n");
            }

            System.out.println(u.getChatPrefix() + "Custom sorting table " + t.getName() + ", exported!");

            bw.flush();
            bw.close();
            fw.close();
        } catch (IOException ignored) {}
    }

    public void exportCustomTables() {
        for(Table t: TablesList) {
            if(!t.getName().equalsIgnoreCase(defaultTableName)) {
                exportExternalTable(t);
            }
        }
        System.out.println(u.getChatPrefix() + "All custom sorting tables exported!");
    }

    public void exportAllTables() {
        for(Table t: TablesList) {
            if(!t.getName().equalsIgnoreCase(defaultTableName)) {
                exportExternalTable(t);
            } else {
                exportDefaultTable();
            }
        }
        System.out.println(u.getChatPrefix() + "All sorting tables exported!");
    }

    public Table getActiveTable() {
        return activeTable;
    }

    public int setActiveTable(String name) {

        if(activeTable != null) {
            if (name.equalsIgnoreCase(activeTable.getName())) {
                System.out.println(u.getChatPrefix() + "The table '" + name + "' is already the active one.");
                return 0;
            }
        }

        if(name.equalsIgnoreCase(defaultTableName)) {
            activeTable = getTable(defaultTableName);
            System.out.println(u.getChatPrefix() + "The default table has been successfully activated.");
            return 1;
        }

        Table t = getTable(name);
        if(t == null) {
            setActiveTable(defaultTableName);
            System.out.println(u.getChatPrefix() + "The table '" + main.getActiveTableName() + "' was not found. The default table is now the active one.");
            return -1;
        } else {
            activeTable = complete(t);
            main.setActiveTableName(name);
            System.out.println(u.getChatPrefix() + "The table '" + main.getActiveTableName() + "' has been successfully activated.");
            return 1;
        }


    }

    public Table complete(Table t) {
        Table tmp = new Table(getTable(defaultTableName));
        for(Couple couple: t.getList()) {
            tmp.move(couple.getMaterial(), couple.getIndex());
        }
        return tmp;
    }
}
