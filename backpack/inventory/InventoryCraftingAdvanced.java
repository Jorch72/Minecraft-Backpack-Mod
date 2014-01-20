package backpack.inventory;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class InventoryCraftingAdvanced extends InventoryCrafting implements IInventoryBackpack {
    protected InventoryWorkbenchBackpack backpackInventory = null;

    public InventoryCraftingAdvanced(Container eventHandler, IInventory backpackInventory) {
        super(eventHandler, 3, 3);
        if(backpackInventory instanceof InventoryWorkbenchBackpack) {
            this.backpackInventory = (InventoryWorkbenchBackpack) backpackInventory;
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
            backpackInventory.setCraftingSlotContent(pos, getStackInSlot(pos));
        }
    }

    /**
     * Loads the content of the backpackInventory into the current inventory.
     */
    public void loadContent() {
        if(backpackInventory != null) {
            for(int i = 0; i < getSizeInventory(); i++) {
                super.setInventorySlotContents(i, backpackInventory.getStackInCraftingSlot(i));
            }
        }
    }

    /**
     * Loads the recipe for the given recipe slot.
     * 
     * @param recipe
     *            The number of the recipe slot.
     */
    public void loadRecipe(int recipe) {
        if(backpackInventory != null && backpackInventory.hasRecipe(recipe)) {
            backpackInventory.loadRecipe(recipe);
            loadContent();
        }
    }
}