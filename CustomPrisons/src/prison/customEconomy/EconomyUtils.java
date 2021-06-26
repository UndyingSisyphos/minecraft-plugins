package prison.customEconomy;

import java.math.BigInteger;

import org.bukkit.entity.Player;

import prison.main.Main;
import prison.main.Utils;

@SuppressWarnings("unused")
public class EconomyUtils {
	
	private static EconomyUtils instance = null;
	private Main main = null;
	private Utils u = null;
	private String positivePrefix = "&2[&aBANK&2]&r ";
	private String negativePrefix = "&4[&cBANK&4]&r ";
	private String moneySymbol = "&6$";
	private String gemSymbol = "&a♦";
	private String tokenSymbol = "&b⛁";
	
	public EconomyUtils(Main main) {
		this.main = main;
		u = new Utils();
	}
	
	public static EconomyUtils getIstance(Main main) {
		if(instance == null) {
			instance = new EconomyUtils(main);
		}
		return instance;
	}
	
	public String getMoneySymbol() {
		return moneySymbol;
	}
	
	public String getGemSymbol() {
		return gemSymbol;
	}
	
	public String getTokenSymbol() {
		return tokenSymbol;
	}
	
	public BigInteger stoBI(String val) {
		return new BigInteger(val);
	}
	
	public BigInteger itoBI(int intVal) {
		String stringVal = Integer.toString(intVal);
		return new BigInteger(stringVal);
	}
	
	public String getPP() {
		return positivePrefix;
	}
	
	public String getNP() {
		return negativePrefix;
	}
	
	public void successMessage(Player p) {
		p.sendMessage(u.chat(positivePrefix+"&fTransaction completed!"));
	}
	
	public void cancelMessage(Player p) {
		p.sendMessage(u.chat(negativePrefix+"&fTransaction cancelled!"));
	}
	
	public void failMessage(Player p) {
		p.sendMessage(u.chat(negativePrefix+"&fTransaction failed!"));
	}
}
