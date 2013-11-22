package backpack.recipes;

import java.util.ArrayList;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import backpack.item.ItemBackpack;
import backpack.item.ItemInfo;
import backpack.item.Items;

public class RecipeRecolorBackpack implements IRecipe {
    private ArrayList<Integer> allowedDyes = new ArrayList<Integer>();
    private ItemStack result;

    public RecipeRecolorBackpack() {
        allowedDyes.add(Item.dyePowder.itemID);
        allowedDyes.add(Item.leather.itemID);
        allowedDyes.add(Items.tannedLeather.itemID);
    }

    @Override
    public boolean matches(InventoryCrafting craftingGridInventory, World world) {
        result = null;
        ItemStack backpack = null;
        ItemStack dye = null;

        ItemStack slotStack;
        for(int i = 0; i < craftingGridInventory.getSizeInventory(); i++) {
            slotStack = craftingGridInventory.getStackInSlot(i);

            if(slotStack != null) {
                if(slotStack.getItem() instanceof ItemBackpack) {
                    if(slotStack.getItemDamage() == ItemInfo.ENDERBACKPACK || backpack != null) {
                        return false;
                    }
                    backpack = slotStack;
                } else if(allowedDyes.contains(slotStack.itemID)) {
                    if(dye != null) {
                        return false;
                    }
                    dye = slotStack;
                } else {
                    return false;
                }
            }
        }

        if(backpack != null && dye != null) {
            if(backpack.getItemDamage() > 17 && dye.itemID == Item.leather.itemID) {
                return false;
            } else if(backpack.getItemDamage() < 17 && dye.itemID == Items.tannedLeather.itemID) {
                return false;
            }

            int damage = dye.getItem() instanceof ItemDye ? dye.getItemDamage() : 16;
            if(backpack.getItemDamage() > 17) {
                damage += 32;
            }

            result = backpack.copy();
            result.setItemDamage(damage);
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