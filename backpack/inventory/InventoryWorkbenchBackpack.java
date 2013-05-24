package backpack.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import backpack.util.NBTUtil;

public class InventoryWorkbenchBackpack extends InventoryBackpack {
    protected ItemStack[] craftMatrix;

    public InventoryWorkbenchBackpack(EntityPlayer player, ItemStack is) {
        super(player, is);
    }

    // ***** custom methods which are not in IInventory *****
    /**
     * Returns the ItemStack in the craftingMatrix.
     * 
     * @param pos
     *            The position of the item in the array.
     * @return The ItemStack at the given position.
     */
    public ItemStack getStackInCraftingSlot(int pos) {
        return craftMatrix[pos];
    }

    /**
     * Sets the content of the crafting matrix so it can be saved in the NBT.
     * 
     * @param pos
     *            The position of the ItemStack in the array.
     * @param ist
     *            The itemstack to set.
     */
    public void setCraftingSlotContent(int pos, ItemStack ist) {
        craftMatrix[pos] = ist;
    }

    @Override
    protected void writeToNBT() {
        super.writeToNBT();

        if(craftMatrix == null) {
            craftMatrix = new ItemStack[9];
        }

        NBTTagList itemList = new NBTTagList();
        for(int i = 0; i < craftMatrix.length; i++) {
            if(craftMatrix[i] != null) {
                NBTTagCompound slotEntry = new NBTTagCompound();
                slotEntry.setByte("Slot", (byte) i);
                craftMatrix[i].writeToNBT(slotEntry);
                itemList.appendTag(slotEntry);
            }
        }
        // save content of craftMatrix in Crafting->Items
        NBTTagCompound craftingInventory = new NBTTagCompound();
        craftingInventory.setTag("Items", itemList);
        NBTUtil.setCompoundTag(originalIS, "Crafting", craftingInventory);
    }

    @Override
    protected void readFromNBT() {
        super.readFromNBT();

        if(craftMatrix == null) {
            craftMatrix = new ItemStack[9];
        }

        NBTTagList itemList = NBTUtil.getCompoundTag(originalIS, "Crafting").getTagList("Items");
        for(int i = 0; i < itemList.tagCount(); i++) {
            NBTTagCompound slotEntry = (NBTTagCompound) itemList.tagAt(i);
            int j = slotEntry.getByte("Slot") & 0xff;

            if(j >= 0 && j < craftMatrix.length) {
                craftMatrix[j] = ItemStack.loadItemStackFromNBT(slotEntry);
            }
        }
    }
}
