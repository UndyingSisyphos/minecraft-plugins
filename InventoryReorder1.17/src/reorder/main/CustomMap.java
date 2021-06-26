package reorder.main;

import org.bukkit.Material;

import java.io.*;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CustomMap {

    private String name = null;
    private String path = null;
    private Map<Material,Integer> base = null;
    private TreeMap<Integer,Material> reverse = null;
    private Map<Material,Integer> baseComplete = null;
    private TreeMap<Integer,Material> reverseComplete = null;
    private Utils u = null;


    public CustomMap(String name) {
        u = Utils.getInstance();
        this.name = name;
        try {
            path = new java.io.File(".").getCanonicalPath() + "\\plugins\\InventoryReorder\\" + name + ".yml";
        } catch (IOException e) {
            e.printStackTrace();
        }
        base = new HashMap<>();
        reverse = new TreeMap<>();
    }

    public CustomMap(String name, String path) {
        u = Utils.getInstance();
        this.name = name;
        this.path = path;
        base = new HashMap<>();
        reverse = new TreeMap<>();
    }

    public CustomMap(String name, Map<Material,Integer> base, TreeMap<Integer,Material> reverse) {
        u = Utils.getInstance();
        this.name = name;
        try {
            path = new java.io.File(".").getCanonicalPath() + "\\plugins\\InventoryReorder\\" + name + ".yml";
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.base = new HashMap<>(base);
        this.reverse = new TreeMap<>(reverse);
    }

    public CustomMap(String name, String path, Map<Material,Integer> base, TreeMap<Integer,Material> reverse) {
        u = Utils.getInstance();
        this.name = name;
        this.path = path;
        this.base = new HashMap<>(base);
        this.reverse = new TreeMap<>(reverse);
    }


    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Map<Material,Integer> getBase() {
        return base;
    }

    public TreeMap<Integer,Material> getReverse() {
        return reverse;
    }

    public Map<Material,Integer> getCompleteBase() {
        return baseComplete;
    }

    public TreeMap<Integer,Material> getCompleteReverse() {
        return reverseComplete;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setBase(Map<Material,Integer> map) {
        base.clear();
        base.putAll(map);
    }

    public void setReverse(TreeMap<Integer,Material> map) {
        reverse.clear();
        reverse.putAll(map);
    }

    public void setCompleteBase(Map<Material,Integer> map) {
        baseComplete.clear();
        baseComplete.putAll(map);
    }

    public void setCompleteReverse(TreeMap<Integer,Material> map) {
        reverseComplete.clear();
        reverseComplete.putAll(map);
    }

    private void makeNew() {
        base = new HashMap<>();
        reverse = new TreeMap<>();
    }

    private void makeNewComplete() {
        baseComplete = new HashMap<>();
        reverseComplete = new TreeMap<>();
    }

    private void makeNew(Map<Material,Integer> base, TreeMap<Integer,Material> reverse) {
        this.base = new HashMap<>(base);
        this.reverse = new TreeMap<>(reverse);
    }

    private void makeNewComplete(Map<Material,Integer> base, TreeMap<Integer,Material> reverse) {
        this.baseComplete = new HashMap<>(base);
        this.reverseComplete = new TreeMap<>(reverse);
    }

    public void put(Material m, Integer i) {
        base.put(m, i);
        reverse.put(i, m);
    }

    public void putComplete(Material m, Integer i) {
        baseComplete.put(m, i);
        reverseComplete.put(i, m);
    }

    public void remove(Material m, Integer i) {
        base.remove(m);
        reverse.remove(i);
    }

    public void removeComplete(Material m, Integer i) {
        baseComplete.remove(m);
        reverseComplete.remove(i);
    }

    public void putAll(Map<Material,Integer> base, TreeMap<Integer,Material> reverse) {
        this.base.putAll(base);
        this.reverse.putAll(reverse);
    }

    public void putAllComplete(Map<Material,Integer> base, TreeMap<Integer,Material> reverse) {
        this.baseComplete.putAll(base);
        this.reverseComplete.putAll(reverse);
    }

    public boolean importMap(Map<Material,Integer> baseDefaultMap, TreeMap<Integer,Material> reverseDefaultMap) {
        FileReader fr = null;
        BufferedReader br = null;

        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr);
            System.out.println(u.getChatPrefix() + "Found '" + this.getName() + ".yml' file. Reading data from it...");

            this.makeNew();
            Map<Material,Integer> tempBaseMap = new HashMap<>(baseDefaultMap);
            TreeMap<Integer,Material> tempReverseMap = new TreeMap<>(reverseDefaultMap);

            String line = null;
            while((line = br.readLine()) != null) {
                if(!line.startsWith("#") && !line.equals("")) {
                    line = line.replaceAll(" ", "");
                    String[] args = line.split(":");
                    Material m = Material.getMaterial(args[0].toUpperCase());
                    int i = Integer.parseInt(args[1]);

                    if(m != null) {
                        int var = Integer.signum(tempBaseMap.get(m)-i);
                        if(var != 0) {
                            int start = (-var * tempBaseMap.get(m) + 1);
                            int finish = (-var * i);

                            this.put(m, i);
                            tempReverseMap.remove(tempBaseMap.get(m));
                            tempBaseMap.remove(m);

                            for(int c = start; c <= finish; c++) {
                                int a = Math.abs(c);
                                if(tempReverseMap.containsKey(a)) {
                                    tempBaseMap.replace(tempReverseMap.get(a), a+var);
                                    tempReverseMap.put(a+var, tempReverseMap.get(a));
                                    tempReverseMap.remove(a);
                                }
                            }
                        }
                    } else {
                        System.out.println(u.getChatPrefix() + "Material '" + args[0] + "' doesn't exist. Check the spelling and the file formatting with the help of the 'default-sorting-map.yml' file.");
                    }
                }
            }

            this.makeNewComplete(base, reverse);
            this.putAllComplete(tempBaseMap, tempReverseMap);

            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            System.out.println(u.getChatPrefix() + "Custom map file '" + name + "' not found. Ignoring it...");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void exportMap() {
        try {
            File f = new File(path);
            if(f.exists()) {
                System.out.println(u.getChatPrefix() + "Found '" + f.getName() + "' file. Saving changes onto it...");
            } else {
                System.out.println(u.getChatPrefix() + "The custom sorting map file '" + f.getName() + "' couldn't be found. Creating a new one...");
                boolean result = f.getParentFile().mkdirs() && f.createNewFile();
            }

            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("# This is the list of customizations for the map '" + name + "'\n# The elements that exactly correspond to the default map will not be saved here\n\n");

            for(Map.Entry<Integer,Material> entry: reverse.entrySet()) {
                bw.write(entry.getValue().toString().toLowerCase() + ": " + entry.getKey() + "\n");
            }

            bw.flush();
            bw.close();
            fw.close();

            f = new File(path.replace(".yml", "") + "_CompleteMap.yml");
            if(f.exists()) {
                System.out.println(u.getChatPrefix() + "Found '" + f.getName() + "' file. Saving changes onto it...");
            } else {
                System.out.println(u.getChatPrefix() + "The complete custom sorting map file '" + f.getName() + "' doesn't exist yet. Creating a new one...");
                boolean result = f.getParentFile().mkdirs() && f.createNewFile();
            }

            fw = new FileWriter(f);
            bw = new BufferedWriter(fw);
            bw.write("# This is the complete customized map from the '" + name + ".yml' customizations file\n\n");

            for(Map.Entry<Integer,Material> entry: reverseComplete.entrySet()) {
                bw.write(entry.getValue().toString().toLowerCase() + ": " + entry.getKey() + "\n");
            }


            bw.flush();
            bw.close();
            fw.close();
        } catch (IOException ignored) {}
    }
}