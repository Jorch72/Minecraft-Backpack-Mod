package backpack.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import backpack.Backpack;
import backpack.inventory.InventoryBackpack;
import backpack.inventory.InventoryWorkbenchBackpack;
import backpack.item.ItemBackpack;
import backpack.item.ItemInfo;
import backpack.item.ItemWorkbenchBackpack;
import backpack.misc.ConfigurationBackpack;

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

        if(weared) {
            backpack = Backpack.playerTracker.getBackpack(player);
        } else {
            backpack = player.getCurrentEquippedItem();
        }

        return getBackpackInv(backpack, player);
    }

    public static IInventory getBackpackInv(ItemStack backpack, EntityPlayer player) {
        if(backpack != null) {
            if(backpack.getItem() instanceof ItemWorkbenchBackpack) {
                return new InventoryWorkbenchBackpack(player, backpack);
            } else if(backpack.getItem() instanceof ItemBackpack) {
                if(backpack.getItemDamage() == ItemInfo.ENDERBACKPACK) {
                    return player.getInventoryEnderChest();
                } else {
                    return new InventoryBackpack(player, backpack);
                }
            }
        }
        return null;
    }

    public static void writeBackpackToPlayer(EntityPlayer player, ItemStack backpack) {
        NBTTagCompound playerData = player.getEntityData();
        NBTTagCompound backpackTag = new NBTTagCompound();
        if(backpack != null) {
            backpack.writeToNBT(backpackTag);
            playerData.setCompoundTag("backpack", backpackTag);
        } else {
            playerData.removeTag("backpack");
        }
    }

    /**
     * Returns the size of the inventory based on the ItemStack.
     * 
     * @param is
     *            The ItemStack to check for the size.
     * @return The number of slots the inventory has.
     */
    public static int getInventorySize(ItemStack is) {
        if(is == null) {
            return -1;
        }
        if(is.getItem() instanceof ItemBackpack) {
            return is.getItemDamage() > 17 ? ConfigurationBackpack.BACKPACK_SLOTS_L : ConfigurationBackpack.BACKPACK_SLOTS_S;
        } else {
            return 9 * (is.getItemDamage() == 18 ? 0 : 2);
        }
    }
    
    /**
     * Returns the amount of rows an inventory has based on the fact that it has 9 cols.
     * 
     * @param inventory
     *            The inventory whose rows should be calculated.
     * @return The amount of rows as an Integer.
     */
    public static int getInventoryRows(IInventory inventory) {
        return BackpackUtil.getInventoryRows(inventory, (float)9.);
    }
    
    public static int getInventoryRows(IInventory inventory, float cols) {
        return (int) Math.ceil(inventory.getSizeInventory() / cols);
    }
}