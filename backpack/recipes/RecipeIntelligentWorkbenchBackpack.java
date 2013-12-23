package backpack.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import backpack.item.ItemWorkbenchBackpack;
import backpack.util.NBTUtil;

public class RecipeIntelligentWorkbenchBackpack implements IRecipe {
    private ItemStack result;

    @Override
    public boolean matches(InventoryCrafting craftingGridInventory, World world) {
        result = null;
        ItemStack backpack = null;
        boolean book = false;

        ItemStack slotStack;
        for(int i = 0; i < craftingGridInventory.getSizeInventory(); i++) {
            slotStack = craftingGridInventory.getStackInSlot(i);

            if(slotStack != null) {
                if(slotStack.getItem() instanceof ItemWorkbenchBackpack) {
                    if(backpack != null) {
                        return false;
                    }
                    backpack = slotStack;
                } else if(slotStack.getItem() == Item.writableBook) {
                    if(book) {
                        return false;
                    }
                    book = true;
                } else {
                    return false;
                }
            }
        }

        if(backpack != null && book) {
            result = backpack.copy();
            NBTUtil.setBoolean(result, "intelligent", true);
        }

        return result != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting craftingGridInventory) {
        return result.copy();
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result;
    }
}