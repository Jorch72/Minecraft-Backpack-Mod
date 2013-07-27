package backpack.inventory;

import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import backpack.util.IBackpack;

@ChestContainer
public class ContainerBackpack extends ContainerAdvanced {
    public ContainerBackpack(IInventory playerInventory, IInventory backpackInventory, ItemStack backpack) {
        super(playerInventory, backpackInventory, backpack);
        backpackInventory.openChest();

        int y = TOPSPACING;
        int x = LEFTSPACING;

        // backpack
        for(int row = 0; row < upperInventoryRows; ++row) {
            for(int col = 0; col < 9; ++col) {
                addSlotToContainer(new SlotBackpack(backpackInventory, col + row * 9, x, y));
                x += SLOT;
            }
            y += SLOT;
            x = LEFTSPACING;
        }

        y += INVENTORYSPACING;

        // inventory
        for(int row = 0; row < 3; ++row) {
            for(int col = 0; col < 9; ++col) {
                addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
                x += SLOT;
            }
            y += SLOT;
            x = LEFTSPACING;
        }

        y += HOTBARSPACING;

        // hot bar
        for(int col = 0; col < 9; ++col) {
            addSlotToContainer(new Slot(playerInventory, col, x, y));
            x += SLOT;
        }
    }

    /**
     * Called when a player shift-clicks on a slot.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotPos) {
        ItemStack returnStack = null;
        Slot slot = (Slot) inventorySlots.get(slotPos);

        if(slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();
            if(itemStack.getItem() instanceof IBackpack) {
                return returnStack;
            }
            returnStack = itemStack.copy();

            if(slotPos < upperInventoryRows * 9) {
                if(!mergeItemStack(itemStack, upperInventoryRows * 9, inventorySlots.size(), true)) {
                    return null;
                }
            } else if(!mergeItemStack(itemStack, 0, upperInventoryRows * 9, false)) {
                return null;
            }

            if(itemStack.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }
        }

        return returnStack;
    }
}