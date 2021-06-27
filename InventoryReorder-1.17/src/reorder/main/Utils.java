package reorder.main;

import org.bukkit.Material;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.TreeMap;

public class Utils {

    private static Utils instance = null;

    private String chatPrefix = "[InventoryReorder] ";
    private String chatError = "[ERROR] ";
    private String chatFatalError = "[FATAL ERROR] ";

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

    public String getChatError() {
        return chatError;
    }

    public String getChatFatalError() {
        return chatFatalError;
    }

    /*
    public CustomMap importMap(String name, String path, boolean external, String successMessage, String errorMessage) {


    }*/
}