package backpack.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryBackpackSlot extends InventoryBasic {
    protected EntityPlayer player;

    public InventoryBackpackSlot(EntityPlayer player) {
        super("text.backpack.backpack_slot", false, 1);
        this.player = player;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void openChest() {
        readFromNBT(player);
    }

    @Override
    public void closeChest() {
        writeToNBT(player);
    }

    public ItemStack getBackpack() {
        return getStackInSlot(0);
    }

    public void readFromNBT(EntityPlayer player) {
        NBTTagCompound playerData = player.getEntityData();
        if(playerData.hasKey("backpack")) {
            ItemStack backpack = ItemStack.loadItemStackFromNBT(playerData.getCompoundTag("backpack"));
            setInventorySlotContents(0, backpack);
        } else {
            setInventorySlotContents(0, null);
        }
    }

    public void writeToNBT(EntityPlayer player) {
        NBTTagCompound playerData = player.getEntityData();
        NBTTagCompound backpack = new NBTTagCompound();
        if(getBackpack() != null) {
            getBackpack().writeToNBT(backpack);
            playerData.setCompoundTag("backpack", backpack);
        } else {
            playerData.removeTag("backpack");
        }
    }
}