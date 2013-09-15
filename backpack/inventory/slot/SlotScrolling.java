package backpack.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotScrolling extends Slot {
    protected int slotIndex;
    protected boolean isDisabled;

    public SlotScrolling(IInventory inventory, int slotIndex, int x, int y) {
        super(inventory, slotIndex, x, y);
        this.slotIndex = slotIndex;
        isDisabled = false;
    }

    @Override
    public ItemStack getStack() {
        if(isDisabled) {
            return null;
        }
        return inventory.getStackInSlot(slotIndex);
    }

    @Override
    public void putStack(ItemStack par1ItemStack) {
        inventory.setInventorySlotContents(slotIndex, par1ItemStack);
        onSlotChanged();
    }

    @Override
    public ItemStack decrStackSize(int par1) {
        return inventory.decrStackSize(slotIndex, par1);
    }

    @Override
    public boolean isSlotInInventory(IInventory par1iInventory, int par2) {
        return par1iInventory == inventory && par2 == slotIndex;
    }

    @Override
    public int getSlotIndex() {
        return slotIndex;
    }

    public void setSlotIndex(int slotIndex) {
        this.slotIndex = slotIndex;
    }

    public void setDisabled(boolean newValue) {
        isDisabled = newValue;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return !isDisabled;
    }
}