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
    ArrayList<Integer> allowedDyes = new ArrayList<Integer>();

    public RecipeRecolorBackpack() {
        allowedDyes.add(Item.dyePowder.itemID);
        allowedDyes.add(Item.leather.itemID);
        allowedDyes.add(Items.tannedLeather.itemID);
    }

    @Override
    public boolean matches(InventoryCrafting craftingGridInventory, World world) {
        ItemStack backpack = null;
        ItemStack dye = null;

        for(int i = 0; i < craftingGridInventory.getSizeInventory(); i++) {
            ItemStack slot = craftingGridInventory.getStackInSlot(i);

            if(slot != null) {
                if(slot.getItem() instanceof ItemBackpack) {
                    if(slot.getItemDamage() == ItemInfo.ENDERBACKPACK || backpack != null) {
                        return false;
                    }
                    backpack = slot;
                } else if(allowedDyes.contains(slot.itemID)) {
                    if(dye != null) {
                        return false;
                    }
                    dye = slot;
                }
            }
        }

        if(backpack != null && dye != null) {
            if(backpack.getItemDamage() > 17 && dye.itemID == Item.leather.itemID) {
                return false;
            } else if(backpack.getItemDamage() < 17 && dye.itemID == Items.tannedLeather.itemID) {
                return false;
            }
        }

        return backpack != null && dye != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting craftingGridInventory) {
        ItemStack backpack = null;
        ItemStack dye = null;

        for(int i = 0; i < craftingGridInventory.getSizeInventory(); i++) {
            ItemStack slot = craftingGridInventory.getStackInSlot(i);

            if(slot != null) {
                if(slot.getItem() instanceof ItemBackpack) {
                    backpack = slot;
                } else if(allowedDyes.contains(slot.itemID)) {
                    dye = slot;
                }
            }
        }

        int damage = dye.getItem() instanceof ItemDye ? dye.getItemDamage() : 16;
        if(backpack.getItemDamage() > 17) {
            damage += 32;
        }

        ItemStack result = backpack.copy();
        result.setItemDamage(damage);

        return result;
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }
}