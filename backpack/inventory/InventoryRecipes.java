package backpack.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

public class InventoryRecipes extends InventoryBasic {
    protected InventoryWorkbenchBackpack backpackInventory = null;

    public InventoryRecipes(IInventory backpackInventory) {
        super("Recipes", false, 9);
        if(backpackInventory instanceof InventoryWorkbenchBackpack) {
            this.backpackInventory = (InventoryWorkbenchBackpack) backpackInventory;
            for(int i = 0; i < getSizeInventory(); i++) {
                super.setInventorySlotContents(i, this.backpackInventory.getStackInRecipeSlot(i));
            }
        }
    }

    @Override
    public void setInventorySlotContents(int pos, ItemStack ist) {
        super.setInventorySlotContents(pos, ist);
        if(backpackInventory != null) {
            backpackInventory.setRecipeSlotContent(pos, getStackInSlot(pos));
        }
    }
}