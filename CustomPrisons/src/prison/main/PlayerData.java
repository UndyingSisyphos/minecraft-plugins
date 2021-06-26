package prison.main;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import prison.customEconomy.EconomyUtils;

@SuppressWarnings("unused")
public class PlayerData {
	
	private Main main = null;
	private Utils u = null;
	private EconomyUtils eu = null;
	
	private String name = null;
	private InetSocketAddress iP = null;
	private UUID UUID = null;
	
	private BigInteger bal = null;
	private BigInteger gems = null;
	private BigInteger tokens = null;
	
	public PlayerData(Main main, Player p) {
		this.main = main;
		u = Utils.getInstance();
		eu = EconomyUtils.getIstance(main);
		name = p.getName();
		UUID = p.getUniqueId();
		bal = new BigInteger("10000");
		gems = new BigInteger("10000");
		tokens = new BigInteger("10000");
	}
	
	public PlayerData(Main main, OfflinePlayer p, BigInteger bal, BigInteger gems, BigInteger tokens) {
		this.main = main;
		u = new Utils();
		eu = EconomyUtils.getIstance(main);
		name = p.getName();
		UUID = p.getUniqueId();
		this.bal = bal;
		this.gems = gems;
		this.tokens = tokens;
	}
	
	public PlayerData(Main main, Player p, BigInteger bal, BigInteger gems, BigInteger tokens) {
		this.main = main;
		u = new Utils();
		eu = EconomyUtils.getIstance(main);
		name = p.getName();
		UUID = p.getUniqueId();
		this.bal = bal;
		this.gems = gems;
		this.tokens = tokens;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(name);
	}
	
	public BigInteger getBalOf(String type) {
		switch(type.toLowerCase()) {
		case("money"):
			return bal;
		case("gems"):
			return gems;
		case("tokens"):
			return tokens;
		default:
			return null;
		}
	}
	
	public void setBalOf(String type, BigInteger value) {
		switch(type.toLowerCase()) {
		case("money"):
			this.bal = value;
		case("gems"):
			this.gems = value;
		case("tokens"):
			this.tokens = value;
		}
	}
	
	
	
	public void pay(String type, int val) {
		if(type.equalsIgnoreCase("money")) {
			bal = bal.add(eu.itoBI(val));
			getPlayer().sendMessage(u.chat(eu.getPP()+"&f"+val+eu.getMoneySymbol()+" &7Have been added to your balance."));
		} else if(type.equalsIgnoreCase("gems")) {
			gems = gems.add(eu.itoBI(val));
			getPlayer().sendMessage(u.chat(eu.getPP()+"&f"+val+eu.getGemSymbol()+" &7Have been added to your balance."));
		} else if(type.equalsIgnoreCase("tokens")) {
			tokens = tokens.add(eu.itoBI(val));
			getPlayer().sendMessage(u.chat(eu.getPP()+"&f"+val+eu.getTokenSymbol()+" &7Have been added to your balance."));
		}
	}
	
	public int take(String type, int val) {
		if(type.equalsIgnoreCase("money")) {
			if(bal.compareTo(eu.itoBI(val)) < 0) {
				getPlayer().sendMessage(u.chat(eu.getNP()+"%cYou don't have enough money to do that!"));
				return -1;
			} else {
				bal = bal.subtract(eu.itoBI(val));
				getPlayer().sendMessage(u.chat(eu.getNP()+"&f"+val+eu.getMoneySymbol()+" &7Have been taken from your balance."));
				return 0;
			}
		} else if(type.equalsIgnoreCase("gems")) {
			if(gems.compareTo(eu.itoBI(val)) < 0) {
				getPlayer().sendMessage(u.chat(eu.getNP()+"%cYou don't have enough gems to do that!"));
				return -1;
			} else {
				gems = gems.subtract(eu.itoBI(val));
				getPlayer().sendMessage(u.chat(eu.getNP()+"&f"+val+eu.getGemSymbol()+" &7Have been taken from your balance."));
				return 0;
			}
		} else if(type.equalsIgnoreCase("tokens")) {
			if(tokens.compareTo(eu.itoBI(val)) < 0) {
				getPlayer().sendMessage(u.chat(eu.getNP()+"%cYou don't have enough tokens to do that!"));
				return -1;
			} else {
				tokens = tokens.subtract(eu.itoBI(val));
				getPlayer().sendMessage(u.chat(eu.getNP()+"&f"+val+eu.getTokenSymbol()+" &7Have been taken from your balance."));
				return 0;
			}
		} else {
			return 1;
		}
	}
	
	public ItemStack withdraw(String type, int val) {
		String curr = null;
		String displayName = null;
		String line0 = "&7Withdrawn by "+name;
		String line1 = "&7Amount: ";
		if(type.equalsIgnoreCase("money")) {
			if(bal.compareTo(eu.itoBI(val)) < 0) {
				getPlayer().sendMessage(u.chat(eu.getNP()+"%cYou don't have enough money to do that!"));
				return null;
			} else {
				take(type, val);
				curr = "&6Money";
				line1 = line1.concat("&f"+val+"&6$");
			}
		} else if(type.equalsIgnoreCase("gems")) {
			if(gems.compareTo(eu.itoBI(val)) < 0) {
				getPlayer().sendMessage(u.chat(eu.getNP()+"%cYou don't have enough gems to do that!"));
				return null;
			} else {
				take(type, val);
				curr = "&aGem";
				line1 = line1.concat("&f"+val+eu.getGemSymbol());
			}
		} else if(type.equalsIgnoreCase("tokens")) {
			if(tokens.compareTo(eu.itoBI(val)) < 0) {
				getPlayer().sendMessage(u.chat(eu.getNP()+"%cYou don't have enough tokens to do that!"));
				return null;
			} else {
				take(type, val);
				curr = "&bToken";
				line1 = line1.concat("&f"+val+eu.getTokenSymbol());
			}
		}
		displayName = u.chat(curr+" &fnote");
		getPlayer().sendMessage(u.chat(eu.getPP()+"&7You received a "+displayName+"&7!"));
		ItemStack receipt = u.createItem(Material.PAPER, 1, u.chat(displayName), line0, line1);
		receipt.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
		ItemMeta meta = receipt.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		receipt.setItemMeta(meta);
		return receipt;
	}
	
	public void redeem(ItemStack i) {
		ItemMeta meta = i.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		String type = null;
		String sub = meta.getDisplayName().substring(2, 5);
		
		if(sub.equals("Mon")) {
			type = "money";
		} else if(sub.equals("Gem")) {
			type = "gems";
		} else if(sub.equals("Tok")) {
			type = "tokens";
		}
		String line1 = lore.get(1);
		int val = 0;
		if(line1.substring(2).startsWith("Amount: ")) {
			val = Integer.parseInt(line1.substring(12, line1.length()-3));
		}
		pay(type, val);
	}

	

	
	//-------------------------------
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InetSocketAddress getIP() {
		return iP;
	}

	public void setIP(InetSocketAddress iP) {
		this.iP = iP;
	}

	public UUID getUUID() {
		return UUID;
	}

	public void setUUID(UUID uUID) {
		UUID = uUID;
	}
	
	public BigInteger getBal() {
		return bal;
	}
	
	public void setBal(BigInteger bal) {
		this.bal = bal;
	}

	public void setGems(BigInteger gems) {
		this.gems = gems;
	}
	
	public BigInteger getGems() {
		return gems;
	}
	
	public BigInteger getTokens() {
		return tokens;
	}
	
	public void setTokens(BigInteger tokens) {
		this.tokens = tokens;
	}
}