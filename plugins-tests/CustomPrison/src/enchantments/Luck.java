package enchantments;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class Luck extends Enchantment implements Listener {
	
	Random rand;
	
	public Luck(int id) {
		super(id);
		rand = new Random();
	}

	@Override
	public boolean canEnchantItem(ItemStack arg0) {
		if(arg0.getType().equals(Material.DIAMOND_PICKAXE)) {
			return true;
		}
		return false;
	}
	
	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent event) {
		Player p = event.getPlayer();
		ItemStack item = new ItemStack(p.getInventory().getItemInMainHand());
		if(item.containsEnchantment(this)) {
			int sponge = 0;
			switch(item.getEnchantmentLevel(this)) {
			case(1):
				sponge = rand.nextInt(100);
			case(2):
				sponge = rand.nextInt(50);
			case(3):
				sponge = rand.nextInt(10);
			default:
				sponge = 0;
			}
			if(sponge == 5) {
				p.getInventory().addItem(new ItemStack(Material.SPONGE, 1));
			}
		}
	}
	
	@Override
	public int getId() {
		return 101;
	}

	@Override
	public boolean conflictsWith(Enchantment arg0) {
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return null;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public String getName() {
		return ChatColor.translateAlternateColorCodes('&', "&6&lLucky Mining&r");
	}

	@Override
	public int getStartLevel() {
		return 0;
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
