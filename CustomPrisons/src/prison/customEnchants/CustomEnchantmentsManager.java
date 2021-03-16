package prison.customEnchants;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import prison.main.Main;
import prison.main.Utils;

public class CustomEnchantmentsManager implements Listener {
	
	private static CustomEnchantmentsManager instance = null;
	private Main main = null;
	private Utils u = null;
	private Random rand = null;
	private ArrayList<CustomEnchant> list = null;
	private ArrayList<CustomEnchant> potioned = null;
	
	public CustomEnchantmentsManager(Main main) {
		this.main = main;
		main.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) main);
		u = new Utils();
		rand = new Random();
		list = new ArrayList<CustomEnchant>();
		list.add(new CustomEnchant(100, "Unbreaking", "Common", 1, 1, Material.DIAMOND_PICKAXE));
		list.add(new CustomEnchant(101, "Efficiency", "Common", 0, 100, Material.DIAMOND_PICKAXE));
		list.add(new CustomEnchant(102, "Fortune", "Common", 0, 100, Material.DIAMOND_PICKAXE));
		list.add(new CustomEnchant(103, "SilkTouch", "Rare", 0, 1, Material.DIAMOND_PICKAXE));
		list.add(new CustomEnchant(104, "Sphere", "Forbidden", 0, 3, Material.DIAMOND_PICKAXE));
		list.add(new CustomEnchant(105, "Night_Vision", "Uncommon", 0, 1, PotionEffectType.NIGHT_VISION, Material.DIAMOND_PICKAXE));
		list.add(new CustomEnchant(106, "Jump", "Uncommon", 0, 4, PotionEffectType.JUMP, Material.DIAMOND_PICKAXE));
		list.add(new CustomEnchant(107, "Speed", "Uncommon", 0, 5, PotionEffectType.SPEED, Material.DIAMOND_PICKAXE));
		list.add(new CustomEnchant(108, "Haste", "Uncommon", 0, 4, PotionEffectType.FAST_DIGGING, Material.DIAMOND_PICKAXE));
		potioned = new ArrayList<CustomEnchant>();
		for(CustomEnchant ce: list) {
			if(ce.getPet() != null) {
				potioned.add(ce);
			}
		}
		loadEnchantments();
	}
	
	public static CustomEnchantmentsManager getInstance(Main main) {
		if(instance == null) {
			instance = new CustomEnchantmentsManager(main);
		}
		return instance;
	}
	
	public void loadEnchantments() {
		Field f = null;
		try {
			f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
			for(CustomEnchant ce: list) {
				Enchantment.registerEnchantment(ce);
			}
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	public void unloadEnchantments() {
		try {
			Field byIdField = Enchantment.class.getDeclaredField("byId");
			Field byNameField = Enchantment.class.getDeclaredField("byName");
			byIdField.setAccessible(true);
			byNameField.setAccessible(true);
			HashMap<Integer,Enchantment> byId = (HashMap<Integer,Enchantment>) byIdField.get(null);
			HashMap<Integer,Enchantment> byName = (HashMap<Integer,Enchantment>) byNameField.get(null);
			for(CustomEnchant ce: list) {
				if(byId.containsKey(ce.getId())) {
					byId.remove(ce.getId());
				}
				if(byId.containsKey(ce.getName())) {
					byName.remove(ce.getName());
				}
			}
		} catch(Exception ignored) {
			
		}
	}
	
	public CustomEnchant getEnchantment(String name) {
		for(CustomEnchant ce: list) {
			if(ce.getName().equals(name)) {
				return ce;
			}
		}
		return  null;
	}
	
	public void addEnchantment(Player p, String enchName) {
		addEnchantment(p, enchName, 1);
	}
	
	public void addEnchantment(Player p, String enchName, int lvl) {
		CustomEnchant e = getEnchantment(enchName);
		ItemStack i = p.getInventory().getItemInMainHand();
		if(e == null) {
			p.sendMessage(u.chat("&4&oEnchantment doesn't exist!"));
		} else if(e.canEnchantItem(i)) {
			ItemMeta meta = i.getItemMeta();
			ArrayList<String> lore = null;
			
			if(toolContainsEnchantment(i, e.getName())) {
				lore = (ArrayList<String>) meta.getLore();
				if(getEnchantmentLevel(i, e.getName()) == e.getMaxLevel()) {
					p.sendMessage(u.chat("&c&o"+e.getName().replaceAll("_", " ")+" is already at maximum level!"));
					return;
				} else {
					lvl += getEnchantmentLevel(i, e.getName());
					if(lvl > e.getMaxLevel()) {
						lvl = e.getMaxLevel();
					}
				}
				for(String l: lore) {
					if(l.equals(e.getLoreText(p, getEnchantmentLevel(i, e.getName())))) {
						lore.set(lore.indexOf(l), e.getLoreText(p, lvl));
					}
				}
			} else {
				if(meta.hasLore()) {
					lore = (ArrayList<String>) meta.getLore();
				} else {
					lore = new ArrayList<String>();
				}
				lore.add(e.getLoreText(p, lvl));
			}
			
			lore = reorderLore(lore);
			meta.setLore(lore);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
			if(e.getName().equals("Unbreaking")) {
				meta.setUnbreakable(true);
			}
			
			p.getInventory().getItemInMainHand().setItemMeta(meta);
			p.getInventory().getItemInMainHand().addEnchantment(e, lvl);
			
			if(e.getName().equals("Efficiency")) {
				p.getInventory().getItemInMainHand().addUnsafeEnchantment(Enchantment.DIG_SPEED, lvl);
			}
		} else {
			p.sendMessage(u.chat("&cCannot enchant this item with "+e.getName()));
		}
	}
	
	public ArrayList<String> reorderLore(ArrayList<String> lore) {
		ArrayList<String> ordered = new ArrayList<String>();
		CustomEnchant ce = new CustomEnchant(99);
		for(int c = 0; c < lore.size(); c++) {
			String s = lore.get(c);
			if(s.startsWith(u.chat(ce.getRarityCode("Common")))) {
				ordered.add(s);
			}
		}
		ordered.add(getDivider());
		ordered.add("");
		for(int c = 0; c < lore.size(); c++) {
			String s = lore.get(c);
			if(!s.startsWith(u.chat(ce.getRarityCode("Common"))) && !s.equals(getDivider()) && !s.equals("")) {
				ordered.add(s);
			}
		}
		ordered.add("");
		return ordered;
	}
	
	
	public String getDivider() {
		return u.chat("&c______________________");
	}
	
	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent e) {
		if(e.isDropItems()) {
			e.setDropItems(false);
			Player p = e.getPlayer();
			ItemStack mainHand = p.getInventory().getItemInMainHand();
			ArrayList<Block> broken = new ArrayList<Block>();
			broken.add(e.getBlock());
			float fortuneLvl = 0;
			
			if(toolContainsEnchantment(mainHand, "Fortune")) {
				fortuneLvl = getEnchantmentLevel(mainHand, "Fortune");
			}
			if(toolContainsEnchantment(mainHand, "Sphere")) {
				broken.addAll(breakSphere(p, e.getBlock(), 1+getEnchantmentLevel(mainHand, "Sphere")));
			}
			
			
			
			
			
			
			
			for(Block b: broken) {
				float amount = 1;
				amount *= (49/(fortuneLvl+50)) + ((fortuneLvl+1)/50);
				if(amount < 1) {
					amount = 1;
				} else if(amount%1 != 0) {
					float rest = amount%1;
					if(rand.nextFloat() < rest) {
						amount += 1;
					}
				}
				if(toolContainsEnchantment(mainHand, "SilkTouch")) {
					e.getPlayer().getInventory().addItem(b.getState().getData().toItemStack((int) amount));
				} else {
					for(ItemStack i: b.getDrops()) {
						amount *= i.getAmount();
						i.setAmount((int) amount);
						e.getPlayer().getInventory().addItem(i);
					}
				}
				b.setType(Material.AIR);
			}
		}
	}
	
	public void scheduleCheckEnchantment(Player p) {
		main.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
			@Override
			public void run() {
				ItemStack mainHand = p.getInventory().getItemInMainHand();
				for(CustomEnchant ce: potioned) {
					if(toolContainsEnchantment(mainHand, ce.getName())) {
						if(p.hasPotionEffect(ce.getPet())) {
							if(p.getPotionEffect(ce.getPet()).getAmplifier() < (getEnchantmentLevel(mainHand, ce.getName())-1)) {
								p.removePotionEffect(ce.getPet());
							}
						}
						p.addPotionEffect(new PotionEffect(ce.getPet(), 128000, (getEnchantmentLevel(mainHand, ce.getName())-1), false, false));
					} else {
						p.removePotionEffect(ce.getPet());
	 				}
				}
			}
		}, 20, 40);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		scheduleCheckEnchantment(e.getPlayer());
	}
	
	public boolean toolContainsEnchantment(ItemStack i, String e) {
		if(i.getEnchantments() != null) {
			Map<Enchantment,Integer> map = i.getEnchantments();
			for(Entry<Enchantment,Integer> en: map.entrySet()) {
				if(en.getKey().getName().equals(e)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int getEnchantmentLevel(ItemStack i, String e) {
		Map<Enchantment,Integer> map = i.getEnchantments();
		for(Entry<Enchantment,Integer> en: map.entrySet()) {
			if(en.getKey().getName().equals(e)) {
				return en.getValue();
			}
		}
		return 0;
	}

	public ArrayList<Block> breakSphere(Player p, Block c, int r) {
		ArrayList<Block> list = new ArrayList<Block>();
		Double xc = (double) c.getX();
		Double yc = (double) c.getY();
		Double zc = (double) c.getZ();
		Double x = 0.0;
		Double y = 0.0;
		Double z = 0.0;
		boolean eq = false;
		for(int a = 0; a <= 2*r; a++) {
			x = xc+(a-r);
			for(int s = 0; s <= 2*r; s++) {
				y = yc+(s-r);
				for(int d = 0; d <= 2*r; d++) {
					z = zc+(d-r);
					eq = Math.sqrt(Math.pow((x-xc), 2)+Math.pow((y-yc), 2)+Math.pow((z-zc), 2)) <= r;
					if(eq) {
						Block b = p.getWorld().getBlockAt(new Location(p.getWorld(), x, y, z));
						if(b.getType() != Material.AIR && !b.equals(c)) {
							list.add(b);
						}
					}
				}
			}
		}
		return list;
	}
}
