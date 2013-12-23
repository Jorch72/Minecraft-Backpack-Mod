package backpack.inventory;

import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import backpack.util.BackpackUtil;
import backpack.util.InventoryUtil;

public class InventoryWorkbenchBackpack extends InventoryBackpack implements IInventoryBackpack {
    protected ItemStack[] craftMatrix = new ItemStack[9];
    protected ItemStack[] recipes = new ItemStack[9];
    protected ItemStack[][] recipesIngredients = new ItemStack[9][9];
    protected boolean craftingHandlerMode = false;
    protected int[] mapping;

    public InventoryWorkbenchBackpack(EntityPlayer player, ItemStack is) {
        super(player, is);
        mapping = new int[craftMatrix.length];
        Arrays.fill(mapping, -1);
    }

    @Override
    public int getSizeInventory() {
        if(craftingHandlerMode) {
            return craftMatrix.length;
        }
        return super.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slotPosition) {
        if(craftingHandlerMode) {
            int correspondingSlot = findCorrespondingSlot(slotPosition);
            if(correspondingSlot == -1) {
                return null;
            } else {
                return super.getStackInSlot(correspondingSlot);
            }
        }
        return super.getStackInSlot(slotPosition);
    }

    @Override
    public void setInventorySlotContents(int slotPosition, ItemStack newItemStack) {
        if(craftingHandlerMode) {
            int correspondingSlot = findCorrespondingSlot(slotPosition);
            if(correspondingSlot != -1) {
                super.setInventorySlotContents(correspondingSlot, newItemStack);
                return;
            }
            if(getStackInCraftingSlot(slotPosition) == null) {
                for(int i = 0; i < super.getSizeInventory(); i++) {
                    if(super.getStackInSlot(i) == null) {
                        super.setInventorySlotContents(i, newItemStack);
                    }
                }
            }
        }
        super.setInventorySlotContents(slotPosition, newItemStack);
    }

    // ***** custom methods which are not in IInventory *****
    /**
     * Returns the ItemStack in the craftingMatrix.
     * 
     * @param pos
     *            The position of the item in the array.
     * @return The ItemStack at the given position.
     */
    public ItemStack getStackInCraftingSlot(int pos) {
        return craftMatrix[pos];
    }

    /**
     * Sets the content of the crafting matrix so it can be saved in the NBT.
     * 
     * @param pos
     *            The position of the ItemStack in the array.
     * @param ist
     *            The ItemStack to set.
     */
    public void setCraftingSlotContent(int pos, ItemStack ist) {
        craftMatrix[pos] = ist;
        onInventoryChanged();
    }

    /**
     * Returns the ItemStack in the recipe matrix.
     * 
     * @param pos
     *            The position of the item in the array.
     * @return The ItemStack at the given position.
     */
    public ItemStack getStackInRecipeSlot(int pos) {
        return recipes[pos];
    }

    /**
     * Sets the content of the recipe matrix so it can be saved in the NBT.
     * 
     * @param pos
     *            The position of the ItemStack in the array.
     * @param ist
     *            The ItemStack to set.
     */
    public void setRecipeSlotContent(int pos, ItemStack ist) {
        recipes[pos] = ist;
        for(int i = 0; i < craftMatrix.length; i++) {
            if(craftMatrix[i] != null) {
                recipesIngredients[pos][i] = craftMatrix[i].copy();
            }
        }
        onInventoryChanged();
    }

    public boolean hasRecipe(int recipe) {
        return recipes[recipe] != null;
    }

    public void loadRecipe(int recipe) {
        InventoryUtil.readInventory(craftMatrix, "Recipe-" + recipe, originalIS);
        onInventoryChanged();
    }

    @Override
    protected void writeToNBT() {
        super.writeToNBT();

        if(craftMatrix == null) {
            craftMatrix = new ItemStack[9];
        }
        if(recipes == null) {
            recipes = new ItemStack[9];
        }
        if(recipesIngredients == null) {
            recipesIngredients = new ItemStack[9][9];
        }

        InventoryUtil.writeInventory(craftMatrix, "Crafting", originalIS);
        InventoryUtil.writeInventory(recipes, "Recipes", originalIS);
        for(int i = 0; i < recipesIngredients.length; i++) {
            InventoryUtil.writeInventory(recipesIngredients[i], "Recipe-" + i, originalIS);
        }
    }

    @Override
    protected void readFromNBT() {
        super.readFromNBT();

        if(craftMatrix == null) {
            craftMatrix = new ItemStack[9];
        }
        if(recipes == null) {
            recipes = new ItemStack[9];
        }
        if(recipesIngredients == null) {
            recipesIngredients = new ItemStack[9][9];
        }

        InventoryUtil.readInventory(craftMatrix, "Crafting", originalIS);
        InventoryUtil.readInventory(recipes, "Recipes", originalIS);
        for(int i = 0; i < recipesIngredients.length; i++) {
            InventoryUtil.readInventory(recipesIngredients[i], "Recipe-" + i, originalIS);
        }
    }

    /**
     * Sets if the inventory is in crafting handler mode so it will react
     * different.
     * 
     * @param value
     *            The new value for the mode.
     */
    public void setCraftingHandlerMode(boolean value) {
        craftingHandlerMode = value;
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
        ItemStack craftingGridStack = getStackInCraftingSlot(recipeSlotPosition);
        for(int i = 0; i < super.getSizeInventory(); i++) {
            ItemStack inventoryStack = super.getStackInSlot(i);
            if(BackpackUtil.areStacksEqual(craftingGridStack, inventoryStack)) {
                mapping[recipeSlotPosition] = i;
                return i;
            }
        }
        for(int i = 0; i < super.getSizeInventory(); i++) {
            ItemStack inventoryStack = super.getStackInSlot(i);
            if(BackpackUtil.areStacksEqualWithOD(craftingGridStack, inventoryStack)) {
                mapping[recipeSlotPosition] = i;
                return i;
            }
        }
        return -1;
    }
}
