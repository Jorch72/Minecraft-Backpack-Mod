package backpack.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import backpack.inventory.InventoryBackpack;
import backpack.inventory.InventoryWorkbenchBackpack;
import backpack.item.ItemBackpack;
import backpack.item.ItemWorkbenchBackpack;
import backpack.misc.Constants;

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
            backpack = player.getCurrentArmor(2);
        } else {
            backpack = player.getCurrentEquippedItem();
        }

        if(backpack != null) {
            if(backpack.getItem() instanceof ItemWorkbenchBackpack) {
                inventoryBackpack = new InventoryWorkbenchBackpack(player, backpack);
            } else if(backpack.getItem() instanceof ItemBackpack) {
                if(backpack.getItemDamage() == Constants.ENDERBACKPACK) {
                    inventoryBackpack = player.getInventoryEnderChest();
                } else {
                    inventoryBackpack = new InventoryBackpack(player, backpack);
                }
            }
        }

        return inventoryBackpack;
    }
}
