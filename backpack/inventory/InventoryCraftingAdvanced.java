package backpack.inventory;

import java.util.Arrays;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import backpack.util.BackpackUtil;

public class InventoryCraftingAdvanced extends InventoryCrafting implements IInventoryBackpack {
    protected InventoryWorkbenchBackpack backpackInventory = null;
    protected boolean useInventoryMode = false;
    protected int[] mapping;

    public InventoryCraftingAdvanced(Container eventHandler, IInventory backpackInventory) {
        super(eventHandler, 3, 3);
        if(backpackInventory instanceof InventoryWorkbenchBackpack) {
            this.backpackInventory = (InventoryWorkbenchBackpack) backpackInventory;
        }

        mapping = new int[getSizeInventory()];
        Arrays.fill(mapping, -1);
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
    public void setInventorySlotContents(int slotPosition, ItemStack newItemStack) {
        if(useInventoryMode) {
            int correspondingSlot = findCorrespondingSlot(slotPosition);
            // if there is a corresponding slot set the item
            if(correspondingSlot != -1) {
                backpackInventory.setInventorySlotContents(correspondingSlot, newItemStack);
                return;
            }
            /* don't know for what the hell this code was
            if(super.getStackInSlot(slotPosition) == null) {
                for(int i = 0; i < backpackInventory.getSizeInventory(); i++) {
                    if(backpackInventory.getStackInSlot(i) == null) {
                        backpackInventory.setInventorySlotContents(i, newItemStack);
                    }
                }
            }*/
        }
        super.setInventorySlotContents(slotPosition, newItemStack);
        if(backpackInventory != null) {
            backpackInventory.setCraftingSlotContent(slotPosition, getStackInSlot(slotPosition));
        }
    }
    
    @Override
    public ItemStack getStackInSlot(int slotPosition) {
        if(useInventoryMode) {
            int correspondingSlot = findCorrespondingSlot(slotPosition);
            if(correspondingSlot == -1) {
                return null;
            } else {
                return backpackInventory.getStackInSlot(correspondingSlot);
            }
        }
        return super.getStackInSlot(slotPosition);
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

    /**
     * Sets if the craftMatrix should use the backpacks inventory to provide it's content.
     * 
     * @param value
     *            The new value for the mode.
     */
    public void setUseInventoryMode(boolean value) {
        useInventoryMode = value;
        if(value == false) {
            Arrays.fill(mapping, -1);
        }
    }

    /**
     * Will try to find a slot with the same ItemStack as the given slot from
     * the craft matrix.
     * 
     * @param recipeSlotPosition
     *            The index of the slot in the craft matrix.
     * @return The slot number with the same content or -1 if nothing was found.
     */
    protected int findCorrespondingSlot(int recipeSlotPosition) {
        if(mapping[recipeSlotPosition] != -1) {
            return mapping[recipeSlotPosition];
        }
        ItemStack craftingGridStack = super.getStackInSlot(recipeSlotPosition);
        for(int i = 0; i < backpackInventory.getSizeInventory(); i++) {
            ItemStack inventoryStack = backpackInventory.getStackInSlot(i);
            if(BackpackUtil.areStacksEqual(craftingGridStack, inventoryStack)) {
                mapping[recipeSlotPosition] = i;
                return i;
            }
        }
        for(int i = 0; i < backpackInventory.getSizeInventory(); i++) {
            ItemStack inventoryStack = backpackInventory.getStackInSlot(i);
            if(BackpackUtil.areStacksEqualByOD(craftingGridStack, inventoryStack)) {
                mapping[recipeSlotPosition] = i;
                return i;
            }
        }
        return -1;
    }
}