package backpack.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryUtil {
    /**
     * This method will read the ItemStacks from the given ItemStack's
     * NBTCompount with the given key and load them into the given inventory.
     * 
     * @param inventory
     *            The inventory to fill with the ItemStacks.
     * @param name
     *            The name of the key in the NBTCompound.
     * @param backpack
     *            The ItemStack with the NBTCompound to read from.
     */
    public static void readInventory(ItemStack[] inventory, String name, ItemStack backpack) {
        readInventory(inventory, name, backpack, true);
    }

    /**
     * This method will read the ItemStacks from the given ItemStack's
     * NBTCompount with the given key and load them into the given inventory.
     * 
     * @param inventory
     *            The inventory to fill with the ItemStacks.
     * @param name
     *            The name of the key in the NBTCompound.
     * @param backpack
     *            The ItemStack with the NBTCompound to read from.
     * @param clearInventory
     *            If the inventory should be cleared before reading the content.
     */
    public static void readInventory(ItemStack[] inventory, String name, ItemStack backpack, boolean clearInventory) {
        if(clearInventory) {
            for(int i = 0; i < inventory.length; i++) {
                inventory[i] = null;
            }
        }
        NBTTagList itemList = NBTUtil.getCompoundTag(backpack, name).getTagList("Items");
        for(int i = 0; i < itemList.tagCount(); i++) {
            NBTTagCompound slotEntry = (NBTTagCompound) itemList.tagAt(i);
            int slot = slotEntry.getByte("Slot") & 0xff;

            if(slot >= 0 && slot < inventory.length) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(slotEntry);
            }
        }
    }

    /**
     * Will save the ItemStacks from the given Inventory in the NBTCompound of
     * the given ItemStack under the given key.
     * 
     * @param inventory
     *            The inventory to read from.
     * @param name
     *            The name of the key in the NBTCompound.
     * @param backpack
     *            The ItemStack to write on.
     */
    public static void writeInventory(ItemStack[] inventory, String name, ItemStack backpack) {
        writeInventory(inventory, name, backpack, 0, inventory.length);
    }

    /**
     * Will save the ItemStacks from the given Inventory in the NBTCompound of
     * the given ItemStack under the given key.
     * 
     * @param inventory
     *            The inventory to read from.
     * @param name
     *            The name of the key in the NBTCompound.
     * @param backpack
     *            The ItemStack to write on.
     * @param from
     *            The start slot from the inventory.
     */
    public static void writeInventory(ItemStack[] inventory, String name, ItemStack backpack, int from) {
        writeInventory(inventory, name, backpack, from, inventory.length);
    }

    /**
     * Will save the ItemStacks from the given Inventory in the NBTCompound of
     * the given ItemStack under the given key.
     * 
     * @param inventory
     *            The inventory to read from.
     * @param name
     *            The name of the key in the NBTCompound.
     * @param backpack
     *            The ItemStack to write on.
     * @param from
     *            The start slot from the inventory.
     * @param to
     *            The end slot from the inventory.
     */
    public static void writeInventory(ItemStack[] inventory, String name, ItemStack backpack, int from, int to) {
        NBTTagList itemList = new NBTTagList();
        for(int i = from; i < to; i++) {
            if(inventory[i] != null) {
                NBTTagCompound slotEntry = new NBTTagCompound();
                slotEntry.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(slotEntry);
                itemList.appendTag(slotEntry);
            }
        }
        // save content of inventory in {name}->Items
        NBTTagCompound craftingInventory = new NBTTagCompound();
        craftingInventory.setTag("Items", itemList);
        NBTUtil.setCompoundTag(backpack, name, craftingInventory);
    }

    public static boolean canStack(ItemStack stack1, ItemStack stack2) {
        return (stack1 == null)
                || (stack2 == null)
                || ((stack1.itemID == stack2.itemID) && ((!stack2.getHasSubtypes()) || (stack2.getItemDamage() == stack1.getItemDamage())) && (ItemStack.areItemStackTagsEqual(stack2, stack1)) && (stack1
                        .isStackable()));
    }
}
