// 
// Decompiled by Procyon v0.5.36
// 

package com.harrison.customenchants;

import java.lang.reflect.Field;
import java.util.HashMap;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;
import java.util.List;
import org.bukkit.ChatColor;
import java.util.ArrayList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener, CommandExecutor {
    public CustomEnchants ench;
    
    public Main() {
        this.ench = new CustomEnchants(101);
    }
    
    public void onEnable() {
        this.LoadEnchantments();
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)this.ench, (Plugin)this);
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("giveexplosive")) {}
        final Player player = (Player)sender;
        final ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        final ItemMeta meta = item.getItemMeta();
        final ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + this.ench.getName() + " I");
        meta.setDisplayName(ChatColor.RED + "Explosive Pickaxe");
        meta.setLore((List)lore);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment((Enchantment)this.ench, 1);
        player.getInventory().addItem(new ItemStack[] { item });
        player.sendMessage(ChatColor.GREEN + "You have been giving the Explosive Pickaxe.");
        return true;
    }
    
    public void onDisable() {
        try {
            final Field byIdField = Enchantment.class.getDeclaredField("Byid");
            final Field byNameField = Enchantment.class.getDeclaredField("ByName");
            byIdField.setAccessible(true);
            byNameField.setAccessible(true);
            final HashMap<Integer, Enchantment> byId = (HashMap<Integer, Enchantment>)byIdField.get(null);
            final HashMap<Integer, Enchantment> byName = (HashMap<Integer, Enchantment>)byNameField.get(null);
            if (byId.containsKey(this.ench.getId())) {
                byId.remove(this.ench.getId());
            }
            if (byName.containsKey(this.ench.getName())) {
                byName.remove(this.ench.getName());
            }
        }
        catch (Exception ex) {}
    }
    
    private void LoadEnchantments() {
        try {
            try {
                final Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, true);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Enchantment.registerEnchantment((Enchantment)this.ench);
            }
            catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            }
        }
        catch (Exception ex) {}
    }
}
