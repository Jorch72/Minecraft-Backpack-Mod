package backpack.inventory;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import backpack.item.ItemBackpackBase;

public class InventoryCraftingAdvanced extends InventoryCrafting implements IInventoryBackpack {
    protected InventoryWorkbenchBackpack backpackInventory = null;

    public InventoryCraftingAdvanced(Container eventHandler, IInventory backpackInventory) {
        super(eventHandler, 3, 3);
        if(backpackInventory instanceof InventoryWorkbenchBackpack) {
            this.backpackInventory = (InventoryWorkbenchBackpack) backpackInventory;
            for(int i = 0; i < getSizeInventory(); i++) {
                super.setInventorySlotContents(i, this.backpackInventory.getStackInCraftingSlot(i));
            }
        }
    }

    @Override
    public ItemStack decrStackSize(int pos, int amount) {
        ItemStack returnIS = super.decrStackSize(pos, amount);
        if(backpackInventory != null) {
            for(int i = 0; i < getSizeInventory(); i++) {
                backpackInventory.setCraftingSlotContent(i, getStackInSlot(i));
            }
            backpackInventory.onInventoryChanged();
        }
        return returnIS;
    }

    @Override
    public void setInventorySlotContents(int pos, ItemStack ist) {
        super.setInventorySlotContents(pos, ist);
        if(backpackInventory != null) {
            for(int i = 0; i < getSizeInventory(); i++) {
                backpackInventory.setCraftingSlotContent(i, getStackInSlot(i));
            }
            backpackInventory.onInventoryChanged();
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int pos) {
        ItemStack itemstack = getStackInSlot(pos);
        if(itemstack != null && itemstack.getItem() instanceof ItemBackpackBase) {
            setInventorySlotContents(pos, null);
        }
        return itemstack;
    }
}
