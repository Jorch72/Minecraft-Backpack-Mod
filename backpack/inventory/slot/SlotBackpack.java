package backpack.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import backpack.item.ItemBackpackBase;
import backpack.misc.ConfigurationBackpack;

public class SlotBackpack extends SlotScrolling {
    public SlotBackpack(IInventory inventory, int slotIndex, int xPos, int yPos) {
        super(inventory, slotIndex, xPos, yPos);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for
     * backpacks and disallowed items.
     */
    @Override
    public boolean isItemValid(ItemStack is) {
        // check for backpack
        if(is != null && is.getItem() instanceof ItemBackpackBase) {
            return false;
        }
        // check for disallowedItems
        String[] disallowedItems = ConfigurationBackpack.DISALLOW_ITEMS.split(",");
        for(String disallowedItem : disallowedItems) {
            Object[] disallowedData = getDisallowedData(disallowedItem);
            // if Integer check id
            if(disallowedData[0] instanceof Integer) {
                if((Integer)disallowedData[0] == is.itemID) {
                    // if disallwedData has 2 values check item damage
                    if(disallowedData.length == 2) {
                        if((Integer)disallowedData[1] == is.getItemDamage()) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            } else {
                // if not an integer it is a string so check for ore dictionary name
                if(OreDictionary.getOreID(is) == OreDictionary.getOreID((String)disallowedData[0])) {
                    return false;
                }
            }
        }
        return super.isItemValid(is);
    }

    private Object[] getDisallowedData(String disallowedItem) {
        String[] disallowedWithMeta = disallowedItem.split(":");
        Object[] result = new Object[disallowedWithMeta.length];
        if(disallowedWithMeta[0].matches("^-?\\d+$")) {
            result[0] = Integer.valueOf(disallowedWithMeta[0]);
            if(result.length == 2 && disallowedWithMeta[1].matches("^-?\\d+$")) {
                result[1] = Integer.valueOf(disallowedWithMeta[1]);
            }
        } else {
            result[0] = disallowedWithMeta[0];
        }
        return result;
    }
}
