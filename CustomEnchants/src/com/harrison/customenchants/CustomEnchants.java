// 
// Decompiled by Procyon v0.5.36
// 

package com.harrison.customenchants;

import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.Listener;
import org.bukkit.enchantments.Enchantment;

public class CustomEnchants extends Enchantment implements Listener {
    
	public CustomEnchants(final int id) {
        super(id);
    }
    
    @EventHandler
    public void onPlayerBreakBlock(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final ItemStack mainhand = player.getInventory().getItemInMainHand();
        if (mainhand.containsEnchantment((Enchantment)this)) {
            player.getWorld().createExplosion(event.getBlock().getLocation(), 8.0f, true);
        }
    }
    
    public int getId() {
        return 101;
    }
    
    public boolean canEnchantItem(final ItemStack arg0) {
        return true;
    }
    
    public boolean conflictsWith(final Enchantment arg0) {
        return false;
    }
    
    public EnchantmentTarget getItemTarget() {
        return null;
    }
    
    public int getMaxLevel() {
        return 5;
    }
    
    public String getName() {
        return "Explosive";
    }
    
    public int getStartLevel() {
        return 0;
    }

	@Override
	public boolean isCursed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTreasure() {
		// TODO Auto-generated method stub
		return false;
	}
}
