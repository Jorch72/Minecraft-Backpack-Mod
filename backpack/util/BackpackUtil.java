package backpack.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import backpack.Backpack;
import backpack.inventory.InventoryBackpack;
import backpack.inventory.InventoryWorkbenchBackpack;
import backpack.item.ItemBackpack;
import backpack.item.ItemInfo;
import backpack.item.ItemWorkbenchBackpack;

public class BackpackUtil {
    /**
     * Returns the IInventory of the current equipped backpack or the ender
     * backpack.
     * 
     * @param player
     *            The player who holds the backpack.
     * @return An IInventory with the content of the backpack.
     */
    public static IInventory getBackpackInv(EntityPlayer player, boolean weared) {
        ItemStack backpack;
        IInventory inventoryBackpack = null;

        if(weared) {
            backpack = Backpack.proxy.backpackSlot.getBackpack();
        } else {
            backpack = player.getCurrentEquippedItem();
        }

        if(backpack != null) {
            if(backpack.getItem() instanceof ItemWorkbenchBackpack) {
                inventoryBackpack = new InventoryWorkbenchBackpack(player, backpack);
            } else if(backpack.getItem() instanceof ItemBackpack) {
                if(backpack.getItemDamage() == ItemInfo.ENDERBACKPACK) {
                    inventoryBackpack = player.getInventoryEnderChest();
                } else {
                    inventoryBackpack = new InventoryBackpack(player, backpack);
                }
            }
        }

        return inventoryBackpack;
    }
}