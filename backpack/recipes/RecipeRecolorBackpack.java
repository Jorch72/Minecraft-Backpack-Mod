package backpack.recipes;

import java.util.ArrayList;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import backpack.item.ItemBackpack;
import backpack.item.Items;
import backpack.util.BackpackUtil;

public class RecipeRecolorBackpack implements IRecipe {
    private ArrayList<Integer> allowedDyes = new ArrayList<Integer>();
    private ArrayList<String> allowedOreDyes = new ArrayList<String>();
    private ItemStack result;

    public RecipeRecolorBackpack() {
        allowedDyes.add(Item.leather.itemID);
        allowedDyes.add(Items.tannedLeather.itemID);
        
        allowedOreDyes.add("dyeBlack");
        allowedOreDyes.add("dyeRed");
        allowedOreDyes.add("dyeGreen");
        allowedOreDyes.add("dyeBrown");
        allowedOreDyes.add("dyeBlue");
        allowedOreDyes.add("dyePurple");
        allowedOreDyes.add("dyeCyan");
        allowedOreDyes.add("dyeLightGray");
        allowedOreDyes.add("dyeGray");
        allowedOreDyes.add("dyePink");
        allowedOreDyes.add("dyeLime");
        allowedOreDyes.add("dyeYellow");
        allowedOreDyes.add("dyeLightBlue");
        allowedOreDyes.add("dyeMagenta");
        allowedOreDyes.add("dyeOrange");
        allowedOreDyes.add("dyeWhite");
    }

    @Override
    public boolean matches(InventoryCrafting craftingGridInventory, World world) {
        result = null;
        ItemStack backpack = null;
        ItemStack dye = null;
        String oreName = null;

        ItemStack slotStack;
        for(int i = 0; i < craftingGridInventory.getSizeInventory(); i++) {
            slotStack = craftingGridInventory.getStackInSlot(i);

            if(slotStack != null) {
                if(slotStack.getItem() instanceof ItemBackpack) {
                    if(BackpackUtil.isEnderBackpack(slotStack) || backpack != null) {
                        return false;
                    }
                    backpack = slotStack;
                } else if(allowedDyes.contains(slotStack.itemID)) {
                    if(dye != null) {
                        return false;
                    }
                    dye = slotStack;
                } else if(allowedOreDyes.contains(OreDictionary.getOreName(OreDictionary.getOreID(slotStack)))) {
                    if(dye != null) {
                        return false;
                    }
                    oreName = OreDictionary.getOreName(OreDictionary.getOreID(slotStack));
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
            if(oreName != null) {
                damage = allowedOreDyes.indexOf(oreName);
            }
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