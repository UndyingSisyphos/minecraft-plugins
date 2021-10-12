package reorder.main;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.TreeMap;

public class Utils {

    private static Utils instance = null;

    private String chatPrefix = "[InventoryReorder] ";
    private String chatShortPrefix = "[IR] ";
    private String chatInfo = "[INFO] ";
    private String chatSuccess = "[SUCCESS] ";
    private String chatWarning = "[WARNING] ";
    private String chatError = "[ERROR] ";
    private String chatFatalError = "[FATAL ERROR] ";
    private String chatColoredPrefix = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "InventoryReorder" + ChatColor.DARK_AQUA + "]" + ChatColor.RESET + " ";
    private String chatColoredShortPrefix = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "IR" + ChatColor.DARK_AQUA + "]" + ChatColor.RESET + " ";
    private String chatColoredInfo = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "INFO" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " ";
    private String chatColoredSuccess = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SUCCESS" + ChatColor.DARK_GREEN + "]" + ChatColor.RESET + " ";
    private String chatColoredWarning = ChatColor.GOLD + "[" + ChatColor.YELLOW + "WARNING" + ChatColor.GOLD + "]" + ChatColor.RESET + " ";
    private String chatColoredError = ChatColor.DARK_RED + "[" + ChatColor.RED + "ERROR" + ChatColor.DARK_RED + "]" + ChatColor.RESET + " ";
    private String chatColoredFatalError = ChatColor.DARK_RED + "[" + ChatColor.RED + "FATAL ERROR" + ChatColor.DARK_RED + "]" + ChatColor.RESET + " ";

    private Utils() {}

    public static Utils getInstance() {
        if(instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    public String getChatPrefix() {
        return chatPrefix;
    }

    public String getChatShortPrefix() {
        return chatShortPrefix;
    }

    public String getChatInfo() {
        return chatInfo;
    }

    public String getChatSuccess() {
        return chatSuccess;
    }

    public String getChatWarning() {
        return chatWarning;
    }

    public String getChatError() {
        return chatError;
    }

    public String getChatFatalError() {
        return chatFatalError;
    }

    public String getChatColoredPrefix() {
        return chatColoredPrefix;
    }

    public String getChatColoredShortPrefix() {
        return chatColoredShortPrefix;
    }

    public String getChatColoredInfo() {
        return chatColoredInfo;
    }

    public String getChatColoredSuccess() {
        return chatColoredSuccess;
    }

    public String getChatColoredWarning() {
        return chatColoredWarning;
    }

    public String getChatColoredError() {
        return chatColoredError;
    }

    public String getChatColoredFatalError() {
        return chatColoredFatalError;
    }

    public boolean isInteger(String s) {
        if(s == null) {
            return false;
        }
        try {
            int n = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}