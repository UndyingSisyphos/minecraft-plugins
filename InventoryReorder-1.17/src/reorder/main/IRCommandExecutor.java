package reorder.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IRCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            p.sendMessage("cmd = " + cmd.toString() + "\ncommand = " + command + "\nargs[] = " + args.toString());
        }
        return false;
    }
}
