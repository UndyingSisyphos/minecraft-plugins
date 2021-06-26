package reorder.main;

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
}