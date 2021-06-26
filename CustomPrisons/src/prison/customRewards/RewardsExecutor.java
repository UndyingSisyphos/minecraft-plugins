package prison.customRewards;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import prison.customGUI.GUIManager;
import prison.main.Main;
import prison.main.Utils;

@SuppressWarnings("unused")
public class RewardsExecutor implements CommandExecutor {
	
	private Main main = null;
	private Utils u = null;
	private GUIManager gm = null;
	private RewardsManager rm = null;
	
	public RewardsExecutor(Main main, RewardsManager rm) {
		this.main = main;
		u = Utils.getInstance();
		gm = GUIManager.getInstance(main);
		this.rm = rm;
		initCommands();
	}
	
	private void initCommands() {
		main.getCommand("claim").setExecutor(this);
		main.getCommand("daily").setExecutor(this);
		main.getCommand("weekly").setExecutor(this);
		main.getCommand("monthly").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		return false;
	}

}
