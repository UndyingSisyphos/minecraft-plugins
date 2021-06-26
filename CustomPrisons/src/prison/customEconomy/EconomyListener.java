package prison.customEconomy;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import prison.main.Main;
import prison.main.PlayerData;
import prison.main.Utils;

public class EconomyListener implements Listener, CommandExecutor {
	
	private Main main = null;
	private EconomyManager em = null;
	private Utils u = null;
	
	public EconomyListener(Main main, EconomyManager em) {
		this.main = main;
		main.getServer().getPluginManager().registerEvents((Listener) this, main);
		u = Utils.getInstance();
		this.em = em;
		initCommands();
	}
	
	private void initCommands() {
		main.getCommand("bal").setExecutor(this);
		main.getCommand("withdraw").setExecutor(this);
	}
	
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent e) {
		EquipmentSlot es = e.getHand();
        if (es.equals(EquipmentSlot.HAND)) {
        	Player p = e.getPlayer();
    		Action a = e.getAction();
    		if(a.equals(Action.RIGHT_CLICK_AIR) || a.equals(Action.RIGHT_CLICK_BLOCK)) {
    			ItemStack i = p.getInventory().getItemInMainHand();
    			if(i.getType().equals(Material.PAPER) && i.containsEnchantment(Enchantment.DURABILITY)) {
    				if(i.getEnchantmentLevel(Enchantment.DURABILITY) == 100) {
    					p.getInventory().getItemInMainHand().setAmount(i.getAmount()-1);
    					em.getData(p).redeem(i);
    				}
    			}
    		}
        }
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			switch(cmd.getName().toLowerCase()) {
			case("bal"):
				String message = em.getPlayerBalChat(p, null, command);
				if(message != null) {
					p.sendMessage(u.chat(message));
					return true;
				} else {
					p.sendMessage(u.chat("&c&oToo many arguments!"));
					return false;
				}
			case("pay"):
				if(args.length == 3) {
					int code = em.transaction(p, args[0], args[1], Integer.parseInt(args[2]));
					if(code == 1) {
						p.sendMessage(u.chat("&4&oWrong Object type!/nContact admins to fix the issue!"));
					}
					return true;
				} else {
					p.sendMessage(u.chat("&c&oToo many (or too few) arguments!"));
					return false;
				}
			
			
			
			}
			
			// Withdraw command
			if(cmd.getName().equals("withdraw")) {
				if(args.length == 2) {
					PlayerData data = em.getData(p);
					ItemStack receipt = data.withdraw(args[0], Integer.parseInt(args[1]));
					if(receipt != null) {
						p.getInventory().addItem(receipt);
						EconomyUtils.getIstance(main).successMessage(p);
					} else {
						EconomyUtils.getIstance(main).failMessage(p);
					}
				}
				return true;
			}
		}
		return false;
	}
}
