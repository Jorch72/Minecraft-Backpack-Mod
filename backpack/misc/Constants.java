package backpack.misc;

import net.minecraft.util.ResourceLocation;

public class Constants {
    public static final String MOD_ID = "Backpack";
    public static final String MOD_NAME = "Backpack";
    public static final String MOD_VERSION = "1.13.15";
    
    public static final int PACKET_RENAME_ID = 0;
    public static final int PACKET_OPEN_BACKPACK_ID = 1;
    public static final int PACKET_UPDATE_SCROLLBAR_ID = 2;
    
    public static final int GUI_ID_BACKPACK = 1;
    public static final int GUI_ID_BACKPACK_WEARED = 2;
    public static final int GUI_ID_RENAME_BACKPACK = 3;
    public static final int GUI_ID_WORKBENCH_BACKPACK = 4;
    public static final int GUI_ID_WORKBENCH_BACKPACK_WEARED = 5;
    public static final int GUI_ID_COMBINED = 6;

    public static final String CHANNEL = "BackpackChannel";

    public static final String DOMAIN = "backpack";

    public static final String WEARED_BACKPACK_OPEN = "wearedBackpackOpen";

    public static final String KEY_OPEN = "Open Backpack";

    public static final ResourceLocation guiCombined = new ResourceLocation(DOMAIN, "textures/gui/guiCombined.png");
    public static final ResourceLocation guiAlt = new ResourceLocation(DOMAIN, "textures/gui/guiBackpackAlt.png");
}
