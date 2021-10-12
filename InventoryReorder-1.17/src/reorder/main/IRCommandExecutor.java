package reorder.main;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class IRCommandExecutor implements CommandExecutor {

    Main main = null;
    Utils u = null;
    TablesManager tm = null;

    boolean guiStyle = false;
    boolean isEditing = false;
    String editingTable = "";

    private final HashMap<String,Integer> commands = new HashMap<String,Integer>() {{
        put("inventoryreorder", 0);
        /*put("help", 0);
        put("style", 1);
        put("menu", 2);
        put("table", 3);
        put("swap", 4);
        put("move", 5);
        put("invert", 6);
        put("save", 7);*/
    }};

    public IRCommandExecutor(Main main) {
        this.main = main;
        u = Utils.getInstance();
        tm = TablesManager.getInstance(main);
        for(String s: commands.keySet()) {
            this.main.getCommand(s).setExecutor(this);
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            String c = cmd.getName();
            switch(commands.get(c)) {
                case(0):
                    return cmdHelp(sender);
                case(1):
                    return cmdStyle(sender, args);
                case(2):
                    return cmdMenu(sender, args);
                case(3):
                    return cmdTable(sender, args);
                case(4):
                    return cmdSwap(sender, args);
                case(5):
                    // MOVE
                    break;
                case(6):
                    // INVERT
                    break;
                case(7):
                    // SAVE
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    private void sendMessage(CommandSender sender, String message) {
        if(sender instanceof Player) {
            ((Player) sender).sendMessage(message);
        } else {
            System.out.println(message);
        }
    }

    private String notEnoughArgs() {
        return u.getChatColoredShortPrefix() + u.getChatColoredError() + "Not enough arguments";
    }

    private String unknownArg() {
        return u.getChatColoredShortPrefix() + u.getChatColoredError() + "Unknown argument";
    }

    private String notMaterial(String name) {
        return u.getChatColoredShortPrefix() +  u.getChatColoredError() + "Material '"+ ChatColor.ITALIC + ChatColor.AQUA + name + ChatColor.RESET + "' doesn't exist";
    }

    private boolean cmdHelp(CommandSender sender) {
        sendMessage(sender, u.getChatColoredPrefix() + "Help and welcome message!");
        sendMessage(sender, u.getChatColoredShortPrefix() + u.getChatColoredInfo() + "Help and welcome message!");
        sendMessage(sender, u.getChatColoredShortPrefix() + u.getChatColoredSuccess() + "Help and welcome message!");
        sendMessage(sender, u.getChatColoredShortPrefix() + u.getChatColoredWarning() + "Help and welcome message!");
        sendMessage(sender, u.getChatColoredShortPrefix() + u.getChatColoredError() + "Help and welcome message!");
        sendMessage(sender, u.getChatColoredShortPrefix() + u.getChatColoredFatalError() + "Help and welcome message!");
        return true;
    }

    private boolean cmdMenu(CommandSender sender, String args[]) {
        return true;
    }

    private boolean cmdStyle(CommandSender sender, String args[]) {
        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase("gui")) {
                if(guiStyle) {
                    sendMessage(sender, u.getChatColoredShortPrefix() + u.getChatColoredInfo() + "Plugin style is already set to Gui");
                } else {
                    guiStyle = true;
                    sendMessage(sender, u.getChatColoredShortPrefix() + u.getChatColoredSuccess() + "Plugin style is now set to Gui");
                }
                return true;
            } else if(args[0].equalsIgnoreCase("text")) {
                if (!guiStyle) {
                    sendMessage(sender, u.getChatColoredShortPrefix() + u.getChatColoredInfo() + "Plugin style is already set to Text");
                } else {
                    guiStyle = false;
                    sendMessage(sender, u.getChatColoredShortPrefix() + u.getChatColoredSuccess() + "Plugin style is now set to Text");
                }
                return true;
            } else {
                unknownArg();
            }
        } else {
            notEnoughArgs();
        }
        return false;
    }

    private boolean cmdTable(CommandSender sender, String args[]) {
        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase("active")) {
                sendMessage(sender, u.getChatColoredPrefix() + "The currently active table is '" + main.getActiveTableName() + "'");
                return true;
            } else if(args[0].equalsIgnoreCase("use")) {
                if(args.length >= 2) {
                    int res = tm.setActiveTable(args[1]);
                    switch(res) {
                        case(-1):
                            sendMessage(sender, u.getChatColoredShortPrefix() + u.getChatColoredWarning() + "The specified table doesn't exist, settings remain unchanged, the active table is '" + main.getActiveTableName() + "'.");
                            break;
                        case(0):
                            sendMessage(sender, u.getChatColoredShortPrefix() + u.getChatColoredInfo() + "The specified table is already the active one, setting remain unchanged.");
                            break;
                        case(1):
                            sendMessage(sender, u.getChatColoredShortPrefix() + u.getChatColoredSuccess() + "Successfully activated the table '" + main.getActiveTableName() + "'");
                            break;
                        default:
                            break;
                    }
                    return true;
                } else {
                    notEnoughArgs();
                }
            } else if(args[0].equalsIgnoreCase("edit")) {
                if(isEditing) {
                    // ALREADY EDITING
                    return true;
                } else {
                    if (args.length >= 2) {
                        if (tm.getTable(args[1]) != null) {
                            isEditing = true;
                            editingTable = args[1].toLowerCase();
                        } else {
                            // TABLE DOESN'T EXIST
                        }
                        return true;
                    } else {
                        sendMessage(sender, notEnoughArgs());
                    }
                }
            } else {
                sendMessage(sender, unknownArg());
            }
        } else {
            sendMessage(sender, notEnoughArgs());
        }
        return false;
    }

    private boolean cmdSwap(CommandSender sender, String args[]) {
        if(!isEditing) {
            sendMessage(sender, u.getChatColoredWarning() + "You are not editing any table at the moment. To do so use the command '" + ChatColor.ITALIC + ChatColor.AQUA + "/table edit <table-name>"  + ChatColor.RESET + "'");
            return true;
        }
        if(args.length >= 2) {
            if(u.isInteger(args[0])) {
                if(u.isInteger(args[1])) {
                    tm.getTable(editingTable).swap(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
                } else {
                    Material m1 = Material.matchMaterial(args[1]);
                    if(m1 != null) {
                        tm.getTable(editingTable).swap(m1, Integer.parseInt(args[0]));
                    } else {
                        sendMessage(sender, notMaterial(args[1]));
                    }
                }
            } else {
                Material m0 = Material.matchMaterial(args[0]);
                if(m0 != null) {
                    if(u.isInteger(args[1])) {
                        tm.getTable(editingTable).swap(m0, Integer.parseInt(args[1]));
                    } else {
                        Material m1 = Material.matchMaterial(args[1]);
                        if(m1 != null) {
                            tm.getTable(editingTable).swap(m0, m1);
                        } else {
                            sendMessage(sender, notMaterial(args[1]));
                        }
                    }
                } else {
                    sendMessage(sender, notMaterial(args[0]));
                }
            }
            return true;
        } else {
            sendMessage(sender, notEnoughArgs());
        }
        return false;
    }

}
