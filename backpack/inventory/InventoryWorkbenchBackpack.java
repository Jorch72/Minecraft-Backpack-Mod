package backpack.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import backpack.util.InventoryUtil;

public class InventoryWorkbenchBackpack extends InventoryBackpack implements IInventoryBackpack {
    protected ItemStack[] craftMatrix = new ItemStack[9];
    protected ItemStack[] recipes = new ItemStack[9];
    protected ItemStack[][] recipesIngredients = new ItemStack[9][9];

    public InventoryWorkbenchBackpack(EntityPlayer player, ItemStack is) {
        super(player, is);
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
            recipesIngredients[pos][i] = craftMatrix[i] == null ? null : craftMatrix[i].copy();
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
}
