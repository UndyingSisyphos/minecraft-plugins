package reorder.main;

import org.bukkit.Material;

import java.util.*;

public class Table implements Cloneable {

    private String name;
    private String fileName;
    private String path;
    private boolean enabled = false;
    private TreeMap<Integer,Material> cMap = null;
    private HashMap<Material,Integer> fMap = null;


    public Table(String name, String path) {
        this.name = name;
        fileName = path.substring(path.lastIndexOf("\\")+1);
        this.path = path;
        cMap = new TreeMap<>();
    }

    public Table(Table t) {
        name = t.getName();
        fileName = t.getFileName();
        path = t.getPath();
        cMap = new TreeMap<>(t.getcMap());
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPath() {
        return path;
    }

    public TreeMap<Integer,Material> getcMap() {
        return cMap;
    }

    public HashMap<Material,Integer> getfMap() {
        return fMap;
    }

    public Table clone() {
        try {
            return (Table) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer indexOf(Material material) {
        for(Map.Entry<Integer,Material> entry: cMap.entrySet()) {
            if(entry.getValue().equals(material)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public Material getElementAt(Integer index) {
        return cMap.get(index);
    }

    public void put(Material material, Integer index) {
        cMap.put(index, material);
    }

    public void putAll(Table t) {
        cMap.putAll(t.getcMap());
    }

    public void remove(Integer index) {
        cMap.remove(index);
    }

    public void remove(Material material) {
        cMap.remove(indexOf(material));
    }

    public void clear() {
        for(Integer key: cMap.keySet()) {
            remove(key);
        }
    }

    public void replace(Integer index, Material newValue) {
        cMap.replace(index, newValue);
    }

    public void replace(Material oldValue, Material newValue) {
        cMap.replace(indexOf(oldValue), newValue);
    }

    public void move(Material material, Integer oldIndex, Integer newIndex) {
        int var = Integer.signum(oldIndex-newIndex);
        if(var != 0) {
            int start = (-var * oldIndex + 1);
            int finish = (-var * newIndex);

            cMap.remove(oldIndex);

            for(int c = start; c <= finish; c++) {
                int a = Math.abs(c);
                if(cMap.containsKey(a)) {
                    cMap.put(a+var, cMap.get(a));
                    cMap.remove(a);
                }
            }

            cMap.put(finish, material);
        }
    }

    public void move(Material material, Integer newIndex) {
        int oldPos = indexOf(material);
        move(material, oldPos, newIndex);
    }

    public void move(Integer oldIndex, Integer newIndex) {
        Material material = cMap.get(oldIndex);
        move(material, oldIndex, newIndex);
    }

    public void enable(Table t) {
        fMap = new HashMap<>();
        for(Map.Entry<Integer,Material> entry: cMap.entrySet()) {
            t.move(entry.getValue(), entry.getKey());
        }
        for(Map.Entry<Integer,Material> entry: t.getcMap().entrySet()) {
            fMap.put(entry.getValue(), entry.getKey());
        }
        enabled = true;
    }

    public void disable() {
        fMap = null;
        enabled = false;
    }

    public void reload(Table t) {
        if(enabled) {
            disable();
            enable(t);
        }
    }

    public TreeMap<Integer,Material> export() {
        return cMap;
    }

    public TreeMap<Integer,Material> export(Table t) {
        Table tt = t.clone();
        for(Map.Entry<Integer,Material> entry: cMap.entrySet()) {
            tt.move(entry.getValue(), entry.getKey());
        }
        return tt.getcMap();
    }
}
