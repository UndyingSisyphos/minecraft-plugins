package prison.customGUI;

import java.util.HashMap;
import org.bukkit.entity.Player;

public interface GUICreator {
	public void createGUIs();
	public HashMap<String,GUICreator> getGUIs();
	public CustomGUI getGUI(String name, Player p);
}