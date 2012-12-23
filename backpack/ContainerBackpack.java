package backpack;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBackpack extends Container {
	private int numRows;
	private ItemStack openedBackpack;

	public ContainerBackpack(IInventory playerInventory, IInventory backpackInventory, ItemStack backpack) {
		numRows = backpackInventory.getSizeInventory() / 9;
		backpackInventory.openChest();
		int offset = (numRows - 4) * 18;

		for (int x = 0; x < 9; ++x) for (int y = 0; y < this.numRows; ++y) {
			this.addSlotToContainer(new SlotBackpack(backpackInventory, x + y * 9, 8 + x * 18, 18 + y * 18));
		}

		for (int x = 0; x < 9; ++x) for (int y = 0; y < 3; ++y) {
			this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 103 + y * 18 + offset));
		}

		for (int y = 0; y < 9; ++y) {
			this.addSlotToContainer(new Slot(playerInventory, y, 8 + y * 18, 161 + offset));
		}
		openedBackpack = backpack;
	}

	/**
	 * True is the current equipped item is the opened item otherwise false.
	 */
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		if(player.getCurrentEquippedItem() == null) {
			return false;
		}
		return player.getCurrentEquippedItem().isItemEqual(openedBackpack);
	}
	
	/**
     * Called when a player shift-clicks on a slot.
     */
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotPos) {
        ItemStack returnStack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotPos);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();
            if ((itemStack.getItem() instanceof ItemBackpack)) {
    	        return returnStack;
            }
            returnStack = itemStack.copy();

            if (slotPos < numRows * 9) {
                if (!this.mergeItemStack(itemStack, numRows * 9, inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemStack, 0, this.numRows * 9, false)) {
                return null;
            }

            if (itemStack.stackSize == 0) {
                slot.putStack((ItemStack)null);
            } else {
                slot.onSlotChanged();
            }
        }

        return returnStack;
    }

}
