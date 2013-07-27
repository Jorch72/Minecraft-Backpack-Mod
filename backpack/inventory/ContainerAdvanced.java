package backpack.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import backpack.misc.Constants;
import backpack.util.NBTUtil;

public class ContainerAdvanced extends Container {
    public int TOPSPACING = 18;
    public int LEFTSPACING = 8;
    public int BOTTOMSPACING = 7;
    public int SLOT = 18;
    public int INVENTORYSPACING = 14;
    public int HOTBARSPACING = 4;

    protected final ItemStack openedBackpack;
    public final int upperInventoryRows;
    public final int lowerInventoryRows;

    public ContainerAdvanced(IInventory lowerInventory, IInventory upperInventory, ItemStack backpack) {
        lowerInventoryRows = (int) Math.ceil(lowerInventory.getSizeInventory() / 9.);
        upperInventoryRows = (int) Math.ceil(upperInventory.getSizeInventory() / 9.);

        if(lowerInventory instanceof IInventoryBackpack || lowerInventory instanceof InventoryEnderChest) {
            openedBackpack = backpack;
        } else if(upperInventory instanceof IInventoryBackpack || upperInventory instanceof InventoryEnderChest) {
            openedBackpack = backpack;
        } else {
            openedBackpack = null;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        ItemStack itemStack = null;
        if(openedBackpack != null && NBTUtil.getBoolean(openedBackpack, Constants.WEARED_BACKPACK_OPEN)) {
            itemStack = entityplayer.getCurrentArmor(2);
        } else if(entityplayer.getCurrentEquippedItem() != null) {
            itemStack = entityplayer.getCurrentEquippedItem();
        }
        if(itemStack != null && openedBackpack != null && itemStack.getDisplayName() == openedBackpack.getDisplayName()) {
            return true;
        }
        return false;
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer) {
        super.onContainerClosed(entityplayer);

        if(!entityplayer.worldObj.isRemote) {
            ItemStack itemStack = entityplayer.getCurrentArmor(2);
            if(itemStack != null) {
                if(NBTUtil.hasTag(itemStack, Constants.WEARED_BACKPACK_OPEN)) {
                    NBTUtil.removeTag(itemStack, Constants.WEARED_BACKPACK_OPEN);
                }
            }
        }
    }
}