package backpack.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotScrolling extends Slot {
    protected int slotIndex;

    public SlotScrolling(IInventory inventory, int slotIndex, int x, int y) {
        super(inventory, slotIndex, x, y);
        this.slotIndex = slotIndex;
    }

    @Override
    public ItemStack getStack() {
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
}