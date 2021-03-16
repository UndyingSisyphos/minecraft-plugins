package prison.customRewards;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import prison.customGUI.CustomGUI;
import prison.customGUI.GUICreator;
import prison.customGUI.GUIUtils;
import prison.main.Main;
import prison.main.Utils;

public class RewardsGUI implements GUICreator {
	
	private Main main = null;
	private Utils u = null;
	private GUIUtils gu = null;
	
	private CustomGUI rewards = null;
	
	public RewardsGUI(Main main) {
		this.main = main;
		u = Utils.getInstance();
		gu = GUIUtils.getInstance(main);
		createGUIs();
	}
	
	@Override
	public void createGUIs() {
		rewards = gu.fillGUI(new CustomGUI(main, u.chat("&0Rewards"), 3));
	}

	@Override
	public HashMap<String, GUICreator> getGUIs() {
		HashMap<String,GUICreator> fullList = new HashMap<String,GUICreator>();
		fullList.put("rewards", this);
		return fullList;
	}

	@Override
	public CustomGUI getGUI(String name, Player p) {
		switch(name) {
		case("rewards"):
			return getRewards();
		default:
			return null;
		}
	}

	public CustomGUI getRewards() {
		if(!rewards.isInitialized()) {
			rewards.setItem(u.createItem(Material.GOLD_INGOT, 1, u.chat("&6Daily Reward")), 11);
			rewards.setItem(u.createItem(Material.DIAMOND, 1, u.chat("&bWeekly Reward")), 13);
			rewards.setItem(u.createItem(Material.EMERALD, 1, u.chat("&aMonthly Reward")), 15);
			rewards.setInitialized(true);
		}
		return rewards;
	}

	public void setRewards(CustomGUI rewards) {
		this.rewards = rewards;
	}

}
