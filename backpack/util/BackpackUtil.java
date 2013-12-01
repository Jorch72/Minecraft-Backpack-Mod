package backpack.util;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import backpack.Backpack;
import backpack.inventory.InventoryBackpack;
import backpack.inventory.InventoryWorkbenchBackpack;
import backpack.item.ItemBackpack;
import backpack.item.ItemBackpackBase;
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

    /**
     * Returns the IInventory based on the given ItemStack.
     * 
     * @param backpack
     *            The ItemStack that holds a backpack.
     * @param player
     *            The player who has the backpack.
     * @return An IInventory with the content of the given ItemStack.
     */
    public static IInventory getBackpackInv(ItemStack backpack, EntityPlayer player) {
        if(backpack != null) {
            if(backpack.getItem() instanceof ItemWorkbenchBackpack) {
                return new InventoryWorkbenchBackpack(player, backpack);
            } else if(backpack.getItem() instanceof ItemBackpack) {
                if(isEnderBackpack(backpack)) {
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
            return 9 * (is.getItemDamage() == 18 ? 1 : 2);
        }
    }

    /**
     * Returns the amount of rows an inventory has based on the fact that it has
     * 9 columns.
     * 
     * @param inventory
     *            The inventory whose rows should be calculated.
     * @return The amount of rows as an Integer.
     */
    public static int getInventoryRows(IInventory inventory) {
        return BackpackUtil.getInventoryRows(inventory, (float) 9.);
    }

    /**
     * Returns the amount of rows an inventory has based on the given number of
     * columns.
     * 
     * @param inventory
     *            The inventory whose rows should be calculated.
     * @param cols
     *            The number of columns the inventory has.
     * @return The amount of rows as an Integer.
     */
    public static int getInventoryRows(IInventory inventory, float cols) {
        return (int) Math.ceil(inventory.getSizeInventory() / cols);
    }

    /**
     * Compares the UUID's of two ItemStacks.
     * 
     * @param suspicious
     *            The ItemStack to check.
     * @param original
     *            The original ItemStack to compare to
     * @return Returns true if both have the same UUID, false if one or both
     *         ItemStacks are null, one or both ItemStacks doesn't have the tag
     *         "backpack-UID" or if the UUID's are not equal.
     */
    public static boolean UUIDEquals(ItemStack suspicious, ItemStack original) {
        if(suspicious != null && original != null) {
            if(NBTUtil.hasTag(suspicious, ItemInfo.UID) && NBTUtil.hasTag(original, ItemInfo.UID)) {
                try {
                    UUID UIDsuspicious = UUID.fromString(NBTUtil.getString(suspicious, ItemInfo.UID));
                    UUID UIDoriginal = UUID.fromString(NBTUtil.getString(original, ItemInfo.UID));
                    return UIDsuspicious.equals(UIDoriginal);
                }
                catch (IllegalArgumentException e) {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Checks if an ItemStack is an ender backpack.
     * 
     * @param backpack
     *            The ItemStack to check.
     * @return True if the ItemStack is an ender backpack, false otherwise or if
     *         the stack is null.
     */
    public static boolean isEnderBackpack(ItemStack backpack) {
        if(backpack != null) {
            if(backpack.getItem() instanceof ItemBackpackBase && backpack.getItemDamage() == ItemInfo.ENDERBACKPACK) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if two items are equal based on item id and damage or if they have
     * no subtypes if they have the same item id.
     * 
     * @param firstStack
     *            The first ItemStack to check.
     * @param secondStack
     *            The second ItemStack to compare to.
     * @return True if both have the same item id and damage or true if both
     *         have no subtypes and have the same item id. Otherwise false.
     */
    public static boolean areStacksEqual(ItemStack firstStack, ItemStack secondStack) {
        return areStacksEqual(firstStack, secondStack, false);
    }

    /**
     * Checks if two items are equal based on item id and damage or if they have
     * no subtypes if they have the same item id.
     * 
     * @param firstStack
     *            The first ItemStack to check.
     * @param secondStack
     *            The second ItemStack to compare to.
     * @param useOreDictionary
     *            If true the method also checks if the ItemStacks have the same
     *            OreDictionary ID.
     * @return True if both have the same item id and damage or true if both
     *         have no subtypes and have the same item id. Otherwise false.
     */
    public static boolean areStacksEqual(ItemStack firstStack, ItemStack secondStack, boolean useOreDictionary) {
        if(firstStack == null || secondStack == null) {
            return false;
        }
        if(firstStack.isItemEqual(secondStack)) {
            return true;
        }
        if(!firstStack.getHasSubtypes() && !secondStack.getHasSubtypes()) {
            if(firstStack.itemID == secondStack.itemID) {
                return true;
            }
        }
        if(useOreDictionary && areStacksEqualWithOD(firstStack, secondStack)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if two ItemStacks are equal based on the OreDictionary ID.
     * 
     * @param firstStack
     *            The first ItemStack.
     * @param secondStack
     *            The second ItemStack.
     * @return True if both ItemStacks have the same OreId, false otherwise or
     *         if one or both ItemStacks are null.
     */
    public static boolean areStacksEqualWithOD(ItemStack firstStack, ItemStack secondStack) {
        if(firstStack == null || secondStack == null) {
            return false;
        }
        int oreIdFirst = OreDictionary.getOreID(firstStack);
        int oreIdSecond = OreDictionary.getOreID(secondStack);
        return oreIdFirst == oreIdSecond && oreIdFirst != -1;
    }
}