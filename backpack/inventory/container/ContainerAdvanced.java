package backpack.inventory.container;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import backpack.Backpack;
import backpack.gui.parts.GuiPart;
import backpack.inventory.IInventoryBackpack;
import backpack.misc.Constants;
import backpack.util.NBTUtil;

public abstract class ContainerAdvanced extends Container {
    protected final ItemStack openedBackpack;
    protected final IInventory lowerInventory;
    protected final IInventory upperInventory;
    public final int upperInventoryRows;
    public final int lowerInventoryRows;
    public GuiPart hotbar;

    public ContainerAdvanced(IInventory lowerInventory, IInventory upperInventory, ItemStack backpack) {
        this.lowerInventory = lowerInventory;
        this.upperInventory = upperInventory;

        lowerInventory.openChest();
        upperInventory.openChest();

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
            itemStack = Backpack.proxy.backpackSlot.getBackpack();
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

        lowerInventory.closeChest();
        upperInventory.closeChest();

        if(!entityplayer.worldObj.isRemote) {
            ItemStack itemStack = Backpack.proxy.backpackSlot.getBackpack();
            if(itemStack != null) {
                if(NBTUtil.hasTag(itemStack, Constants.WEARED_BACKPACK_OPEN)) {
                    NBTUtil.removeTag(itemStack, Constants.WEARED_BACKPACK_OPEN);
                }
            }
        }
    }

    public void addSlot(Slot slot) {
        addSlotToContainer(slot);
    }

    public List<ICrafting> getCrafters() {
        return crafters;
    }

    public void sendScrollbarToServer(GuiPart guiPart, int offset) {
    }

    public void updateSlots(int guiPart, int offset) {
    }
}