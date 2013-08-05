package backpack.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBrewingStandIngredient extends Slot {
    public SlotBrewingStandIngredient(IInventory inventory, int slot, int x, int y) {
        super(inventory, slot, x, y);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for
     * the armor slots.
     */
    public boolean isItemValid(ItemStack stack) {
        return stack != null ? stack.getItem().isPotionIngredient(stack) : false;
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as
     * getInventoryStackLimit(), but 1 in the case of armor slots)
     */
    public int getSlotStackLimit() {
        return 64;
    }
}
