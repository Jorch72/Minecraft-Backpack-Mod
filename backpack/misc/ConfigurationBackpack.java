package backpack.misc;

import java.io.File;

import net.minecraftforge.common.Configuration;
import backpack.item.ItemInfo;

public class ConfigurationBackpack {
    public static int ENDER_RECIPE;
    public static int BACKPACK_SIZE_M;
    public static int BACKPACK_SIZE_L;
    public static int MAX_BACKPACK_AMOUNT;
    public static boolean OPEN_ONLY_WEARED_BACKPACK;
    public static boolean AIRSHIP_MOD_COMPATIBILITY;
    public static boolean DISABLE_BACKPACKS;
    public static boolean DISABLE_BIG_BACKPACKS;
    public static boolean DISABLE_ENDER_BACKPACKS;
    public static boolean DISABLE_WORKBENCH_BACKPACKS;

    public static void init(File configFile) {
        Configuration config = new Configuration(configFile);
        // load the content of the configuration file
        config.load();

        // gets the item id from the configuration or creates it if it doesn't exists
        ItemInfo.ID_BACKPACK = config.getItem(ItemInfo.CONFIG_KEY_BACKPACK, ItemInfo.DEFAULT_ID_BACKPACK).getInt();
        ItemInfo.ID_BACKPACK_WORKBENCH = config.getItem(ItemInfo.CONFIG_KEY_BACKPACK_WORKBENCH, ItemInfo.DEFAULT_ID_BACKPACK_WORKBENCH).getInt();
        ItemInfo.ID_BOUND_LEATHER = config.getItem(ItemInfo.CONFIG_KEY_BOUND_LEATHER, ItemInfo.DEFAULT_ID_BOUND_LEATHER).getInt();
        ItemInfo.ID_TANNED_LEATHER = config.getItem(ItemInfo.CONFIG_KEY_TANNED_LEATHER, ItemInfo.DEFAULT_ID_TANNED_LEATHER).getInt();

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
        DISABLE_BACKPACKS = config.get(Configuration.CATEGORY_GENERAL, "disableBackpacks", false, getDisableBackpacksComment()).getBoolean(false);
        DISABLE_BIG_BACKPACKS = config.get(Configuration.CATEGORY_GENERAL, "disableBigBackpacks", false, getDisableBigBackpacksComment()).getBoolean(false);
        DISABLE_ENDER_BACKPACKS = config.get(Configuration.CATEGORY_GENERAL, "disableEnderBackpack", false, getDisableEnderBackpacksComment()).getBoolean(false);
        DISABLE_WORKBENCH_BACKPACKS = config.get(Configuration.CATEGORY_GENERAL, "disableWorkbenchBackpack", false, getDisableWorkbenchBackpacksComment()).getBoolean(false);

        // save the file so it will be generated if it doesn't exists
        config.save();
    }

    private static String getEnderRecipeComment() {
        return "##############\n" + "Recipe to craft ender backpack\n" + "0 ender chest\n" + "1 eye of the ender\n" + "##############";
    }

    private static String getBackpackSizeComment() {
        return "##############\n" + "Number of rows (9 slots) a backpack has\n" + "valid: integers 1-6\n" + "##############";
    }

    private static String getMaxBackpackAmountComment() {
        return "##############\n" + "Number of backpacks a player can have in his inventory\n" + "valid: integers 0-36\n" + "0 = unlimited\n" + "##############";
    }

    private static String getOpenOnlyWearedBackpacksComment() {
        return "##############\n" + "If true you can only open a backpack that you wear in your chest slot\n" + "##############";
    }

    private static String getAirshipModCompatibilityComment() {
        return "##############\n" + "If true normal backpack requires a chest in the middle\n" + "##############";
    }

    private static String getDisableBackpacksComment() {
        return "##############\n" + "If true small backpacks are not craftable\n" + "##############";
    }

    private static String getDisableBigBackpacksComment() {
        return "##############\n" + "If true big backpacks are not craftable\n" + "##############";
    }

    private static String getDisableEnderBackpacksComment() {
        return "##############\n" + "If true ender backpacks are not craftable\n" + "##############";
    }

    private static String getDisableWorkbenchBackpacksComment() {
        return "##############\n" + "If true workbench backpacks are not craftable\n" + "##############";
    }
}
