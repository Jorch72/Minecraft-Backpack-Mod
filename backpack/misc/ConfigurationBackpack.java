package backpack.misc;

import java.io.File;

import net.minecraftforge.common.Configuration;

public class ConfigurationBackpack {
    private Configuration config;

    public static int BACKPACK_ID;
    public static int BOUND_LEATHER_ID;
    public static int TANNED_LEATHER_ID;

    public static int ENDER_RECIPE;
    public static int BACKPACK_SIZE_M;
    public static int BACKPACK_SIZE_L;

    public ConfigurationBackpack(File configFile) {
        config = new Configuration(configFile);
    }

    public void init() {
        // load the content of the configuration file
        config.load();

        // gets the item id from the configuration or creates it if it doesn't
        // exists
        BACKPACK_ID = config.getItem("backpackId", Constants.BACKPACK_ID_DEFAULT).getInt();
        BOUND_LEATHER_ID = config.getItem("boundLeatherId", Constants.BOUND_LEATHER_ID_DEFAULT).getInt();
        TANNED_LEATHER_ID = config.getItem("tannedLeatherId", Constants.TANNED_LEATHER_ID_DEFAULT).getInt();

        config.addCustomCategoryComment(Configuration.CATEGORY_GENERAL, getCommentText());
        ENDER_RECIPE = config.get(Configuration.CATEGORY_GENERAL, "enderRecipe", 0).getInt();
        BACKPACK_SIZE_M = config.get(Configuration.CATEGORY_GENERAL, "backpackSizeM", 3).getInt();
        BACKPACK_SIZE_L = config.get(Configuration.CATEGORY_GENERAL, "backpackSizeL", 6).getInt();

        // save the file so it will be generated if it doesn't exists
        config.save();
    }

    private String getCommentText() {
        return "enderRecipe:\n" + "0 enderChest\n" + "1 eye of the ender\n" + "\n" + "backpackSizeM / backpackSizeL:\n" + "number of rows (9 slots)\n" + "valid: integers 1-6";
    }
}
