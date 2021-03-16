package prison.customRewards;

import java.util.GregorianCalendar;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import prison.customWarps.WarpExecutor;
import prison.customWarps.WarpUtils;
import prison.main.Main;
import prison.main.Utils;

@SuppressWarnings({"unused", "unlikely-arg-type"})
public class RewardsManager {
	
	private static RewardsManager instance = null;
	private Main main = null;
	private Utils u = null;
	
	private RewardsExecutor rex = null; 
	private Map<String,GregorianCalendar> daily_rewards = null;
	private Map<String,GregorianCalendar> weekly_rewards = null;
	private Map<String,GregorianCalendar> monthly_rewards = null;
	
	public RewardsManager(Main main) {
		this.main = main;
		u = Utils.getInstance();
		rex = new RewardsExecutor(main, this);
	}
	
	public static RewardsManager getInstance(Main main) {
		if(instance == null) {
			instance = new RewardsManager(main);
		}
		return instance;
	}
	
	public long missingDaily(Player p) {
		int res = 0;
		if(daily_rewards.containsKey(p.getUniqueId())) {
			GregorianCalendar next = daily_rewards.get(p.getUniqueId());
			GregorianCalendar actual = new GregorianCalendar();
			next.add(11, 23);
			if(next.compareTo(actual) > 0) {
				res = (int) (next.getTimeInMillis() - actual.getTimeInMillis());
			}
		}
		return res;
	}
	
	public long missingWeekly(Player p) {
		int res = 0;
		if(weekly_rewards.containsKey(p.getUniqueId())) {
			GregorianCalendar next = weekly_rewards.get(p.getUniqueId());
			GregorianCalendar actual = new GregorianCalendar();
			next.add(11, 12);
			next.add(5, 6);
			if(next.compareTo(actual) > 0) {
				res = (int) (next.getTimeInMillis() - actual.getTimeInMillis());
			}
		}
		return res;
	}
	
	public long missingMonthly(Player p) {
		int res = 0;
		if(monthly_rewards.containsKey(p.getUniqueId())) {
			GregorianCalendar next = monthly_rewards.get(p.getUniqueId());
			GregorianCalendar actual = new GregorianCalendar();
			next.add(6, 27);
			if(next.compareTo(actual) > 0) {
				res = (int) (next.getTimeInMillis() - actual.getTimeInMillis());
			}
		}
		return res;
	}
	
	
}
