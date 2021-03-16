package prison.customEconomy;

import java.math.BigInteger;

import org.bukkit.entity.Player;

import prison.main.Main;
import prison.main.Utils;

@SuppressWarnings("unused")
public class EconomyUtils {
	
	private static EconomyUtils istance = null;
	private Main main = null;
	private Utils u = null;
	private String positivePrefix = null;
	private String negativePrefix = null;
	
	public EconomyUtils(Main main) {
		this.main = main;
		u = new Utils();
		positivePrefix = "&2[&aBANK&2]&r ";
		negativePrefix = "&4[&cBANK&4]&r ";
	}
	
	public static EconomyUtils getIstance(Main main) {
		if(istance == null) {
			istance = new EconomyUtils(main);
		}
		return istance;
	}
	
	public String getGemsSymbol() {
		return "&a♦";
	}
	
	public String getTokenSymbol() {
		return "&b⛁";
	}
	
	public BigInteger stoBI(String val) {
		return new BigInteger(val);
	}
	
	public BigInteger itoBI(int vali) {
		String vals = Integer.toString(vali);
		return new BigInteger(vals);
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
	
	public void failMessage(Player p) {
		p.sendMessage(u.chat(negativePrefix+"&fTransaction failed!"));
	}
}
