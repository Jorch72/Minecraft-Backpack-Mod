package backpack.util;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
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
    public static IInventory getBackpackInv(EntityPlayer player, boolean worn) {
        ItemStack backpack;

        if(worn) {
            backpack = Backpack.playerHandler.getBackpack(player);
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
     * Returns the amount of rows an inventory has based on the fact that it has
     * 9 cols.
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

    public static boolean UUIDEquals(ItemStack suspicious, ItemStack original) {
        if(suspicious != null && original != null) {
            if(NBTUtil.hasTag(suspicious, "UID") && NBTUtil.hasTag(original, "UID")) {
                UUID UIDsuspicious = UUID.fromString(NBTUtil.getString(suspicious, "UID"));
                UUID UIDoriginal = UUID.fromString(NBTUtil.getString(original, "UID"));
                return UIDsuspicious.equals(UIDoriginal);
            }
        }
        return false;
    }
}