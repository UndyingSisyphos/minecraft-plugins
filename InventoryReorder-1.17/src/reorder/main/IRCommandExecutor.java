package reorder.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IRCommandExecutor implements CommandExecutor {

    Main main = null;
    Utils u = null;

    private final String[] commands = {"inventoryreorder"};

    public IRCommandExecutor(Main main) {
        this.main = main;
        u = Utils.getInstance();
        for(String s: commands) {
            main.getCommand(s).setExecutor(this);
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(cmd.getName().equalsIgnoreCase(commands[0])) {
                p.sendMessage(u.getChatPrefix() + "Help and welcome message!\n"+cmd.getAliases().contains(commands[0]));
            }
        }
        return false;
    }
}
