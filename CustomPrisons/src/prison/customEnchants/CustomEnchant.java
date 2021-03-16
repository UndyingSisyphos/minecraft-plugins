package prison.customEnchants;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import prison.main.Utils;

public class CustomEnchant extends Enchantment {

	private Utils u = null;
	private int id = 0;
	private String name = null;
	private String rarity = null;
	private int startLevel = 0;
	private int maxLevel = 0;
	private PotionEffectType pet = null;
	private Material[] tools = null;
	
	
	public CustomEnchant(int id) {
		super(id);
	}
	
	public CustomEnchant(int id, String name, String rarity, int startLevel, int maxLevel, Material... tools) {
		super(id);
		u = Utils.getInstance();
		this.id = id;
		this.name = name;
		this.rarity = rarity;
		this.startLevel = startLevel;
		this.maxLevel = maxLevel;
		this.tools = tools;
	}
	
	public CustomEnchant(int id, String name, String rarity, int startLevel, int maxLevel, PotionEffectType pet, Material... tools) {
		super(id);
		u = Utils.getInstance();
		this.id = id;
		this.name = name;
		this.rarity = rarity;
		this.startLevel = startLevel;
		this.maxLevel = maxLevel;
		this.pet = pet;
		this.tools = tools;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public int getId() {
		return this.id;
	}
	public String getLoreText(Player p, int lvl) {
		String loreName = name.replaceAll("_", " ");
		if(getStartLevel() > 0) {
			return u.chat(getRarityCode()+loreName+" ∞");
		} else {
			if(lvl >= 1) {
				if(maxLevel == 1) {
					return u.chat(getRarityCode()+loreName);
				} else if(maxLevel <= 10) {
					return u.chat(getRarityCode()+loreName+" "+u.getRoman(lvl));
				} else {
					return u.chat(getRarityCode()+loreName+" "+lvl);
				}
			} else {
				return null;
			}
		}
	}

	public String getRarity() {
		return rarity;
	}
	
	public String getRarityCode() {
		switch(rarity) {
		case("Common"):
			return "&7";
		case("Uncommon"):
			return "&a&l";
		case("Rare"):
			return "&b&l";
		case("Epic"):
			return "&d&l";
		case("Forbidden"):
			return "&6&l";
		default:
			return "";
		}
	}
	
	public String getRarityCode(String rarity) {
		switch(rarity) {
		case("Common"):
			return "&7";
		case("Uncommon"):
			return "&a&l";
		case("Rare"):
			return "&b&l";
		case("Epic"):
			return "&d&l";
		case("Forbidden"):
			return "&6&l";
		default:
			return "";
		}
	}

	@Override
	public boolean canEnchantItem(ItemStack i) {
		for(Material m: tools) {
			if(i.getType().equals(m)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getStartLevel() {
		return startLevel;
	}
	
	@Override
	public int getMaxLevel() {
		return maxLevel;
	}
	
	public PotionEffectType getPet() {
		return pet;
	}

	@Override
	public boolean conflictsWith(Enchantment e) {
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return null;
	}

	@Override
	public boolean isCursed() {
		return false;
	}

	@Override
	public boolean isTreasure() {
		return false;
	}
}
