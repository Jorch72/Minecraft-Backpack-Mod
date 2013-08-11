package backpack.item;

import net.minecraft.item.Item;

public class Items {
    public static Item backpack;
    public static Item boundLeather;
    public static Item tannedLeather;
    public static Item workbenchBackpack;

    public static void initItems() {
        backpack = new ItemBackpack(ItemInfo.ID_BACKPACK);
        workbenchBackpack = new ItemWorkbenchBackpack(ItemInfo.ID_BACKPACK_WORKBENCH);
        boundLeather = new ItemLeather(ItemInfo.ID_BOUND_LEATHER);
        tannedLeather = new ItemLeather(ItemInfo.ID_TANNED_LEATHER);
    }
}