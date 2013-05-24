package backpack.inventory;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class InventoryCraftingAdvanced extends InventoryCrafting {
    protected InventoryWorkbenchBackpack backpackInventory;

    public InventoryCraftingAdvanced(Container eventHandler, InventoryWorkbenchBackpack backpackInventory) {
        super(eventHandler, 3, 3);
        this.backpackInventory = backpackInventory;
        for(int i = 0; i < getSizeInventory(); i++) {
            super.setInventorySlotContents(i, backpackInventory.getStackInCraftingSlot(i));
        }
    }

    @Override
    public ItemStack decrStackSize(int pos, int amount) {
        ItemStack returnIS = super.decrStackSize(pos, amount);
        for(int i = 0; i < getSizeInventory(); i++) {
            backpackInventory.setCraftingSlotContent(i, getStackInSlot(i));
        }
        backpackInventory.onInventoryChanged();
        return returnIS;
    }

    @Override
    public void setInventorySlotContents(int pos, ItemStack ist) {
        super.setInventorySlotContents(pos, ist);
        for(int i = 0; i < getSizeInventory(); i++) {
            backpackInventory.setCraftingSlotContent(i, getStackInSlot(i));
        }
        backpackInventory.onInventoryChanged();
    }
}
