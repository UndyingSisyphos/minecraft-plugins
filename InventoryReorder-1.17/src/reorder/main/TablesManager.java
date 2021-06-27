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

        if(main.isEnableCustomTables()) {
            importSuccess = importAllTables();
        } else {
            importSuccess = importDefaultTable();
        }
        if(importSuccess) {
            System.out.println(u.getChatPrefix() + "All available ID sorting maps have been successfully loaded!");
        }

        int res = setActiveTable(main.getActiveTable());
        switch (res) {
            case (-1) -> System.out.println(u.getChatPrefix() + "The table '" + main.getActiveTable() + "' was not found. The default table is now the active one.");
            case (0) -> System.out.println(u.getChatPrefix() + "The table '" + main.getActiveTable() + "' is already the active one.");
            case (1) -> System.out.println(u.getChatPrefix() + "The table '" + main.getActiveTable() + "' has been successfully activated.");
        }

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

        int c = 0;
        boolean newT = false;
        Material m = null;
        InputStreamReader in = null;
        BufferedReader br = null;

        Table t = getTable(defaultTableName);
        if(t == null) {
            t = new Table(defaultTableName, getExternalPath(defaultTableName));
            newT = true;
        }
        t.clear();

        try {
            in = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(getInternalPath(t.getName()))));
            br = new BufferedReader(in);
            System.out.println(u.getChatPrefix() + "Found default sorting map file. Reading data from it...");

            String line = null;
            while((line = br.readLine()) != null) {
                if(!line.startsWith("#") && !line.equals("")) {
                    line = line.replaceAll(" ", "");
                    m = Material.getMaterial(line.toUpperCase());
                    if(m != null) {
                        t.put(m, c);
                    } else {
                        System.out.println(u.getChatPrefix() + "Material '" + line + "' doesn't exist. There is an error in the default table file. Try to download the latest version of the plugin from spigotmc.org or contact the author.");
                    }
                    c++;
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

        if(newT) {
            TablesList.add(t);
        }

        return true;
    }

    public boolean importExternalTable(String name) {

        int c = 0, ln = 1;
        Table t = null;
        boolean newT = false;
        Material m = null;
        InputStreamReader in = null;
        BufferedReader br = null;

        t = getTable(name);
        if(t == null) {
            t = new Table(name, getExternalPath(name));
            newT = true;
        }
        t.clear();

        try {
            in = new InputStreamReader(new FileInputStream(t.getPath()));
            br = new BufferedReader(in);
            System.out.println(u.getChatPrefix() + "Found '" + t.getFileName() + "' file. Reading data from it...");

            String line = null;
            while((line = br.readLine()) != null) {
                if(!line.startsWith("#") && !line.equals("")) {

                    line = line.replaceAll(" ", "");
                    String[] args = line.split(":");

                    if (args.length == 2) {
                        m = Material.getMaterial(args[0].toUpperCase());
                        c = Integer.parseInt(args[1]);
                        if(m != null) {
                            t.put(m, c);
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
            System.out.println(u.getChatPrefix() +  u.getChatError() + "Custom map file '" + t.getFileName() + "' not found. Ignoring it...");
            return false;
        } catch (IOException ignored) {
            return false;
        }

        if(newT) {
            TablesList.add(t);
        }

        return true;
    }

    public boolean importCustomTables() {
        if(main.getCustomMapsNames().size() == 0) {
            return true;
        }
        boolean res = false;
        for(String name: main.getCustomMapsNames()) {
            if(!name.equalsIgnoreCase("")){
                if(importExternalTable(name)) {
                    res = true;
                }
            }
        }
        return res;
    }

    public boolean importAllTables() {
        if(!importDefaultTable()) {
            System.out.println(u.getChatPrefix() + "Stopping the plugin loading procedure.");
            return false;
        }
        System.out.println(u.getChatPrefix() + "Default sorting map loaded!");

        if(importCustomTables()) {
            System.out.println(u.getChatPrefix() + "Custom sorting maps loaded!");
        } else {
            System.out.println(u.getChatPrefix() + "Couldn't load custom sorting maps, check for error messages above.");
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
            for(Map.Entry<Integer,Material> entry: t.export().entrySet()) {
                bw.write(entry.getValue().toString().toLowerCase() + ": " + entry.getKey() + "\n");
            }

            bw.flush();
            bw.close();
            fw.close();
        } catch (IOException ignored) {}
        System.out.println(u.getChatPrefix() + "Default table has been successfully exported!");
    }

    public void exportExternalTable(Table t) {
        try {
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
            for(Map.Entry<Integer,Material> entry: t.export().entrySet()) {
                bw.write(entry.getValue().toString().toLowerCase() + ": " + entry.getKey() + "\n");
            }

            bw.flush();
            bw.close();
            fw.close();

            if(main.isEnableCustomFilledExport()) {
                f = new File(t.getPath().replace(".yml", "_Filled.yml"));
                if(f.exists()) {
                    System.out.println(u.getChatPrefix() + "Found '" + f.getName() + "' file. Saving changes onto it...");
                } else {
                    System.out.println(u.getChatPrefix() + "The filled custom sorting map file '" + f.getName() + "' couldn't be found. Creating a new one...");
                    boolean result = f.getParentFile().mkdirs() && f.createNewFile();
                }

                fw = new FileWriter(f);
                bw = new BufferedWriter(fw);

                bw.write("# This is the custom map filled with the customizations from '" + f.getName() + "'\n\n");
                for(Map.Entry<Integer,Material> entry: t.export(getTable(defaultTableName)).entrySet()) {
                    bw.write(entry.getValue().toString().toLowerCase() + ": " + entry.getKey() + "\n");
                }

                bw.flush();
                bw.close();
                fw.close();
            }
        } catch (IOException ignored) {}
    }

    public void exportCustomTables() {
        for(Table t: TablesList) {
            if(!t.getName().equalsIgnoreCase(defaultTableName)) {
                exportExternalTable(t);
            }
        }
        System.out.println(u.getChatPrefix() + "All custom tables have been successfully exported!");
    }

    public void exportAllTables() {
        for(Table t: TablesList) {
            if(!t.getName().equalsIgnoreCase(defaultTableName)) {
                exportExternalTable(t);
            } else {
                exportDefaultTable();
            }
        }
        System.out.println(u.getChatPrefix() + "All custom tables have been successfully exported!");
    }

    public Table getActiveTable() {
        return activeTable;
    }

    public int setActiveTable(String name) {
        if(activeTable != null) {
            if (name.equalsIgnoreCase(activeTable.getName())) {
                System.out.println(u.getChatPrefix() + "The table '" + name + "' is already active.");
                return 0;
            }
        }
        for(Table t: TablesList) {
            if(name.equalsIgnoreCase(t.getName())) {
                t.enable(getTable(defaultTableName));
                if(il.changeActiveTable(t)) {
                    if(activeTable != null) {
                        activeTable.disable();
                    }
                    activeTable = t;
                    main.setActiveTable(name);
                    return 1;
                } else {
                    t.disable();
                }
            }
        }
        setActiveTable(defaultTableName);
        return -1;
    }
}
