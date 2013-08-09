package backpack.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.LanguageRegistry;

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
    
    public static void addNames() {
        ItemStack itemStack;
        for(int i = 0; i < 16; i++) {
            itemStack = new ItemStack(backpack, 1, i);
            LanguageRegistry.addName(itemStack, ItemInfo.NAME_BACKPACK[i]);
            itemStack = new ItemStack(backpack, 1, i + 32);
            LanguageRegistry.addName(backpack, "Big " + ItemInfo.NAME_BACKPACK[i]);
        }
        itemStack = new ItemStack(backpack, 1, ItemInfo.ENDERBACKPACK);
        LanguageRegistry.addName(itemStack, ItemInfo.NAME_BACKPACK[16]);
        
        itemStack = new ItemStack(workbenchBackpack, 1, 18);
        LanguageRegistry.addName(itemStack, ItemInfo.NAME_BACKPACK_WORKBENCH);
        itemStack = new ItemStack(workbenchBackpack, 1, 50);
        LanguageRegistry.addName(itemStack, "Big " + ItemInfo.NAME_BACKPACK_WORKBENCH);
        
        LanguageRegistry.addName(boundLeather, ItemInfo.NAME_BOUND_LEATHER);
        LanguageRegistry.addName(tannedLeather, ItemInfo.NAME_TANNED_LEATHER);
    }
}