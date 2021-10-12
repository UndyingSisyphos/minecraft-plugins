package reorder.main;

import org.bukkit.Material;

import java.util.ArrayList;

public class Table implements Cloneable {

    private String name = null;
    private String path = null;
    private String fileName = null;
    private ArrayList<Couple> list = null;



    public Table(String name, String path) {
        this.name = name;
        this.path = path;
        this.fileName = path.substring(path.lastIndexOf("\\")+1);
        list = new ArrayList<>();
    }

    public Table(String name, String path, ArrayList<Couple> list) {
        this.name = name;
        this.path = path;
        this.fileName = path.substring(path.lastIndexOf("\\")+1);
        this.list = list;
    }

    public Table(Table t) {
        this.name = t.getName();
        this.path = t.getPath();
        this.fileName = t.getFileName();
        this.list = t.getList();
    }

    public Table clone() {
        try {
            return (Table) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }



    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }

    public ArrayList<Couple> getList() {
        return list;
    }



    public Material get(Integer index) {
        for(Couple couple: list) {
            if(couple.getIndex().equals(index)) {
                return couple.getMaterial();
            }
        }
        return null;
    }

    public Integer indexOf(Material material) {
        for(Couple couple: list) {
            if(couple.getMaterial().equals(material)) {
                return couple.getIndex();
            }
        }
        return -1;
    }

    public Couple getCouple(Integer index) {
        for(Couple couple: list) {
            if(couple.getIndex().equals(index)) {
                return couple;
            }
        }
        return null;
    }

    public Couple getCouple(Material material) {
        for(Couple couple: list) {
            if(couple.getMaterial().equals(material)) {
                return couple;
            }
        }
        return null;
    }

    public void add(Material material, Integer index) {
        list.add(new Couple(material, index));
    }

    public void remove(Material material) {
        for(Couple couple: list) {
            if(couple.getMaterial().equals(material)) {
                list.remove(couple);
                break;
            }
        }
    }

    public void remove(Integer index) {
        for(Couple couple: list) {
            if(couple.getIndex().equals(index)) {
                list.remove(couple);
                break;
            }
        }
    }

    public void clear() {
        list.clear();
    }

    public void move(Material material, Integer oldIndex, Integer newIndex) {
        int low, high, var;

        if(newIndex.equals(oldIndex)) {
            return;
        }

        if(newIndex < oldIndex) {
            low = newIndex;
            high = oldIndex;
            var = +1;
        } else {
            low = oldIndex;
            high = newIndex;
            var = -1;
        }

        for(Couple couple: list) {
            if(couple.getMaterial().equals(material)) {
                couple.setIndex(newIndex);
            } else if(low <= couple.getIndex() && couple.getIndex() <= high) {
                couple.setIndex(couple.getIndex()+var);
            }
        }
    }

    public void move(Material material, Integer newIndex) {
        Integer oldIndex = indexOf(material);
        move(material, oldIndex, newIndex);
    }

    public void move(Integer oldIndex, Integer newIndex) {
        Material material = get(oldIndex);
        move(material, oldIndex, newIndex);
    }

    public void swap(Material firstElement, Material secondElement) {
        Integer firstIndex = indexOf(firstElement);
        Integer secondIndex = indexOf(secondElement);
        getCouple(firstElement).setIndex(secondIndex);
        getCouple(secondElement).setIndex(firstIndex);
    }

    public void swap(Integer firstIndex, Integer secondIndex) {
        Material firstElement = get(firstIndex);
        Material secondElement = get(secondIndex);
        getCouple(firstElement).setIndex(secondIndex);
        getCouple(secondElement).setIndex(firstIndex);
    }

    public void swap(Material firstElement, Integer secondIndex) {
        Integer firstIndex = indexOf(firstElement);
        Material secondElement = get(secondIndex);
        getCouple(firstElement).setIndex(secondIndex);
        getCouple(secondElement).setIndex(firstIndex);
    }
}
