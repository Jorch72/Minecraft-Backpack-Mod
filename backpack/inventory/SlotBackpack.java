package backpack.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import backpack.util.IBackpack;

public class SlotBackpack extends SlotScrolling {
    public SlotBackpack(IInventory inventory, int slotIndex, int xPos, int yPos) {
        super(inventory, slotIndex, xPos, yPos);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for
     * backpacks.
     */
    @Override
    public boolean isItemValid(ItemStack is) {
        return is != null && is.getItem() instanceof IBackpack ? false : true;
    }
}
