package reorder.main;

import org.bukkit.Material;

public class Couple {

    private Material material = null;
    private Integer index = null;

    public Couple(Material material, Integer index) {
        this.material = material;
        this.index = index;
    }

    public Material getMaterial() {
        return material;
    }

    public Integer getIndex() {
        return index;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
