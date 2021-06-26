package prison.customEconomy;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import prison.main.FilesManager;
import prison.main.Main;
import prison.main.PlayerData;
import prison.main.Utils;

@SuppressWarnings("unused")
public class EconomyManager implements Listener {
	
	private static EconomyManager instance = null;
	private Main main = null;
	private Utils u = null;
	private EconomyUtils eu = null;
	private EconomyListener el = null;
	
	private Map<UUID,PlayerData> map = null;
	
	public EconomyManager(Main main) {
		this.main = main;
		main.getServer().getPluginManager().registerEvents((Listener) this, main);
		u = Utils.getInstance();
		eu = EconomyUtils.getIstance(main);
		el = new EconomyListener(main, this);
		map = FilesManager.getInstance(main).readPlayersData();
	}
	
	public static EconomyManager getInstance(Main main) {
		if(instance == null) {
			instance = new EconomyManager(main);
		}
		return instance;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if(!map.containsKey(e.getPlayer().getUniqueId())) {
			addPlayer(e.getPlayer());
		}
	}
	
	public void addPlayer(Player p) {
		map.put(p.getUniqueId(), new PlayerData(main, p));
	}
	
	public PlayerData getData(Player p) {
		return map.get(p.getUniqueId());
	}
	
	public PlayerData getData(String name) {
		for(Entry<UUID,PlayerData> e: map.entrySet()) {
			if(e.getKey().equals(Bukkit.getPlayer(name).getUniqueId())) {
				return e.getValue();
			}
		}
		return null;
	}
	
	public Map<UUID,PlayerData> getAllData() {
		return map;
	}
	
	public String getPlayerBalChat(Player p, String[] args) {
		switch(args.length) {
		case(0):
			return getPlayerBalChat(p, true, "");
		case(1):
			if(args[0] == "money" || args[0] == "gems" || args[0] == "tokens") {
				return getPlayerBalChat(p, true, args[0]);
			} else {
				return getPlayerBalChat(args[0], p.getName().equals(args[0]), "");
			}
		case(2):
			return getPlayerBalChat(args[1], p.getName().equals(args[1]), args[0]);
		default:
			return null;
		}
		
	}
	
	public String getPlayerBalChat(Object player, Boolean self, String type) {
		
		PlayerData data = null;
		String res = "";
		
		if(player instanceof String) {
			data = getData((String) player);
		} else if(player instanceof Player) {
			data = getData((Player) player);
		} else {
			return "&4&oWrong Object type!/nContact admins to fix the issue!";
		}
		
		if(data == null) {
			return "&c&oPlayer not found!";
		}
		
		if(self) {
			res += "Your current ";
		} else {
			res += data.getName() + "'s current ";
		}
		
		switch(type.toLowerCase()) {
		case("money"):
			res += "&6money&7 balance is &f"+data.getBal()+eu.getMoneySymbol();
		case("gems"):
			res += "&agems&7 balance is &f"+data.getGems()+eu.getGemSymbol();
		case("tokens"):
			res += "&btokens&7 balance is &f"+data.getTokens()+eu.getTokenSymbol();
		default:
			res += "total balance is:";
			res += "/n &f• "+"&6Money&7: &f"+data.getBal()+eu.getMoneySymbol();
			res += "/n &f• "+"&aGems&7: &f"+data.getGems()+eu.getGemSymbol();
			res += "/n &f• "+"&bTokens&7: &f"+data.getTokens()+eu.getTokenSymbol();
		}
		
		return res;
	}
	
	public int transaction(Object fromPlayer, Object toPlayer, String type, int amount) {
		
		PlayerData sender = null;
		PlayerData receiver = null;
		
		if(fromPlayer instanceof String) {
			sender = getData((String) fromPlayer);
		} else if(fromPlayer instanceof Player) {
			sender = getData((Player) fromPlayer);
		} else {
			return 1;
		}
		
		if(toPlayer instanceof String) {
			receiver = getData((String) toPlayer);
		} else if(toPlayer instanceof Player) {
			receiver = getData((Player) toPlayer);
		} else {
			return 1;
		}
		
		if(receiver == null) {
			sender.getPlayer().sendMessage(u.chat("&cPlayer not found!"));
			eu.cancelMessage(sender.getPlayer());
			return 0;
		}
		
		if(amount < 0) {
			sender.getPlayer().sendMessage(u.chat("&c&oCannot tranfer an amount lower than 0!"));
		} else if(amount == 0) {
			sender.getPlayer().sendMessage(u.chat(eu.getPP()+"&7Useless transaction... Completed!"));
		} else {
			int errorCode = sender.take(type, amount);
			switch(errorCode) {
			case(-1):
				eu.failMessage(sender.getPlayer());
			case(1):
				sender.getPlayer().sendMessage(u.chat("&4&oWrong Object type!/nContact admins to fix the issue!"));
			case(0):
				receiver.take(type, amount);
				eu.successMessage(sender.getPlayer());
				eu.successMessage(receiver.getPlayer());
			}
		}
		return 0;
	}
	
}
