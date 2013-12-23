package backpack.misc;

import net.minecraft.client.model.ModelBiped;
import backpack.model.ModelBackpack;

public class Constants {
    public static final String MOD_ID = "Backpack";
    public static final String MOD_NAME = "Backpack";
    public static final String MOD_VERSION = "1.26.28";

    public static final int PACKET_ID_RENAME = 0;
    public static final int PACKET_ID_OPEN_BACKPACK = 1;
    public static final int PACKET_ID_OPEN_SLOT = 2;
    public static final int PACKET_ID_CLOSE_GUI = 3;
    public static final int PACKET_ID_UPDATE_SCROLLBAR = 4;
    public static final int PACKET_ID_WORN_BACKPACK_DATA = 5;
    public static final int PACKET_ID_GUI_COMMAND = 6;

    public static final int GUI_ID_RENAME_BACKPACK = 0;
    public static final int GUI_ID_BACKPACK = 1;
    public static final int GUI_ID_BACKPACK_WORN = 2;
    public static final int GUI_ID_WORKBENCH_BACKPACK = 3;
    public static final int GUI_ID_WORKBENCH_BACKPACK_WORN = 4;
    public static final int GUI_ID_COMBINED = 5;
    public static final int GUI_ID_BACKPACK_SLOT = 6;

    public static final String CHANNEL = "BackpackChannel";

    public static final String DOMAIN = "backpack";

    public static final String WORN_BACKPACK_OPEN = "wornBackpackOpen";

    public static final String KEY_OPEN = "key.backpack.openBackpack";

    public static String TEXTURES_PATH = "/mods/backpack/textures/";
    public static final String guiCombined = TEXTURES_PATH + "gui/guiCombined.png";
    public static final String guiAlt = TEXTURES_PATH + "gui/guiBackpackAlt.png";

    public static final String modelTexture = TEXTURES_PATH + "model/backpack.png";

    public static final ModelBiped model = new ModelBackpack();
}
