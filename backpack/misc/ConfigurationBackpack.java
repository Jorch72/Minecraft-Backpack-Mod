package backpack.misc;

import java.io.File;

import net.minecraftforge.common.Configuration;

public class ConfigurationBackpack {
    private Configuration config;

    public static int BACKPACK_ID;
    public static int WORKBENCHBACKPACK_ID;
    public static int BOUND_LEATHER_ID;
    public static int TANNED_LEATHER_ID;

    public static int ENDER_RECIPE;
    public static int BACKPACK_SIZE_M;
    public static int BACKPACK_SIZE_L;
    public static int MAX_BACKPACK_AMOUNT;
    public static boolean OPEN_ONLY_WEARED_BACKPACK;
    public static boolean AIRSHIP_MOD_COMPATIBILITY;

    public ConfigurationBackpack(File configFile) {
        config = new Configuration(configFile);
    }

    public void init() {
        // load the content of the configuration file
        config.load();

        // gets the item id from the configuration or creates it if it doesn't exists
        BACKPACK_ID = config.getItem("backpackId", Constants.BACKPACK_ID_DEFAULT).getInt();
        WORKBENCHBACKPACK_ID = config.getItem("workbenchBackpackId", Constants.WORKBENCHBACKPACK_ID_DEFAULT).getInt();
        BOUND_LEATHER_ID = config.getItem("boundLeatherId", Constants.BOUND_LEATHER_ID_DEFAULT).getInt();
        TANNED_LEATHER_ID = config.getItem("tannedLeatherId", Constants.TANNED_LEATHER_ID_DEFAULT).getInt();

        ENDER_RECIPE = config.get(Configuration.CATEGORY_GENERAL, "enderRecipe", 0, getEnderRecipeComment()).getInt();
        if(ENDER_RECIPE < 0 || ENDER_RECIPE > 1) {
            ENDER_RECIPE = 0;
        }
        BACKPACK_SIZE_L = config.get(Configuration.CATEGORY_GENERAL, "backpackSizeL", 6, getBackpackSizeComment()).getInt();
        if(BACKPACK_SIZE_L < 1 || BACKPACK_SIZE_L > 6) {
            BACKPACK_SIZE_L = 6;
        }
        BACKPACK_SIZE_M = config.get(Configuration.CATEGORY_GENERAL, "backpackSizeM", 3).getInt();
        if(BACKPACK_SIZE_M < 1 || BACKPACK_SIZE_M > 6) {
            BACKPACK_SIZE_M = 6;
        }
        MAX_BACKPACK_AMOUNT = config.get(Configuration.CATEGORY_GENERAL, "maxBackpackAmount", 0, getMaxBackpackAmountComment()).getInt();
        if(MAX_BACKPACK_AMOUNT < 0 || MAX_BACKPACK_AMOUNT > 36) {
            MAX_BACKPACK_AMOUNT = 0;
        }
        OPEN_ONLY_WEARED_BACKPACK = config.get(Configuration.CATEGORY_GENERAL, "openOnlyWearedBackpacks", false, getOpenOnlyWearedBackpacksComment()).getBoolean(false);
        AIRSHIP_MOD_COMPATIBILITY = config.get(Configuration.CATEGORY_GENERAL, "airshipModCompatibility", false, getAirshipModCompatibilityComment()).getBoolean(false);

        // save the file so it will be generated if it doesn't exists
        config.save();
    }
    
    private String getEnderRecipeComment() {
        return "##############\n" + "Recipe to craft ender backpack\n" + "0 ender chest\n" + "1 eye of the ender\n" + "##############";
    }
    
    private String getBackpackSizeComment() {
        return "##############\n" + "Number of rows (9 slots) a backpack has\n" + "valid: integers 1-6\n" + "##############";
    }

    private String getMaxBackpackAmountComment() {
        return "##############\n" + "Number of backpacks a player can have in his inventory\n" + "valid: integers 0-36\n" + "0 = unlimited\n" + "##############";
    }
    
    private String getOpenOnlyWearedBackpacksComment() {
        return "##############\n" + "If true you can only open a backpack that you wear in your chest slot\n" + "##############";
    }
    
    private String getAirshipModCompatibilityComment() {
        return "##############\n" + "If true normal backpack requires a chest in the middle\n" + "##############";
    }
}
