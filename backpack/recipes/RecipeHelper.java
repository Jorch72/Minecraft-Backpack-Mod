package backpack.recipes;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import backpack.item.ItemInfo;
import backpack.item.Items;
import backpack.misc.ConfigurationBackpack;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeHelper {
    public static void registerRecipes() {
        ItemStack backpackStack = new ItemStack(Items.backpack, 1, 16);
        ItemStack boundLeatherStack = new ItemStack(Items.boundLeather);

        if(!ConfigurationBackpack.DISABLE_BACKPACKS) {
            // normal backpack without dye
            if(ConfigurationBackpack.AIRSHIP_MOD_COMPATIBILITY) {
                GameRegistry.addRecipe(backpackStack, "LLL", "LCL", "LLL", 'L', Item.leather, 'C', Block.chest);
            } else {
                GameRegistry.addRecipe(backpackStack, "LLL", "L L", "LLL", 'L', Item.leather);
            }
        }

        if(!ConfigurationBackpack.DISABLE_BIG_BACKPACKS) {
            // normal big backpack without dye
            backpackStack = new ItemStack(Items.backpack, 1, 48);
            GameRegistry.addRecipe(backpackStack, "LLL", "L L", "LLL", 'L', Items.tannedLeather);
        }
        
        String[] dyes = { "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta",
                "dyeOrange", "dyeWhite" };

        // backpacks and big backpacks from black(0) to white(15)
        for(int i = 0; i < 16; i++) {
            if(!ConfigurationBackpack.DISABLE_BACKPACKS) {
                // backpacks
                backpackStack = new ItemStack(Items.backpack, 1, i);
                GameRegistry.addRecipe(new ShapedOreRecipe(backpackStack, "LLL", "LDL", "LLL", 'L', Item.leather, 'D', dyes[i]));
            }

            if(!ConfigurationBackpack.DISABLE_BIG_BACKPACKS) {
                // big backpacks
                backpackStack = new ItemStack(Items.backpack, 1, i + 32);
                GameRegistry.addRecipe(new ShapedOreRecipe(backpackStack, "LLL", "LDL", "LLL", 'L', Items.tannedLeather, 'D', dyes[i]));
            }
        }

        if(!ConfigurationBackpack.DISABLE_ENDER_BACKPACKS) {
            // ender Backpack
            if(ConfigurationBackpack.ENDER_RECIPE == 0) {
                backpackStack = new ItemStack(Items.backpack, 1, ItemInfo.ENDERBACKPACK);
                GameRegistry.addRecipe(backpackStack, "LLL", "LEL", "LLL", 'L', Item.leather, 'E', Block.enderChest);
            } else {
                backpackStack = new ItemStack(Items.backpack, 1, ItemInfo.ENDERBACKPACK);
                GameRegistry.addRecipe(backpackStack, "LLL", "LDL", "LLL", 'L', Item.leather, 'D', Item.eyeOfEnder);
            }
        }

        if(!ConfigurationBackpack.DISABLE_WORKBENCH_BACKPACKS) {
            // workbench Backpacks
            backpackStack = new ItemStack(Items.workbenchBackpack, 1, 18);
            GameRegistry.addRecipe(backpackStack, "LLL", "LWL", "LLL", 'L', Item.leather, 'W', Block.workbench);
    
            backpackStack = new ItemStack(Items.workbenchBackpack, 1, 50);
            GameRegistry.addRecipe(backpackStack, "LLL", "LWL", "LLL", 'L', Items.tannedLeather, 'W', Block.workbench);
        }

        // bound leather
        GameRegistry.addRecipe(boundLeatherStack, "SSS", "LSL", "SSS", 'S', Item.silk, 'L', Item.leather);

        // tanned leather
        ItemStack tannedLeatherStack = new ItemStack(Items.tannedLeather);
        GameRegistry.addSmelting(Items.boundLeather.itemID, tannedLeatherStack, 0.1f);

        // enhance backpack to big backpack
        GameRegistry.addRecipe(new RecipeEnhanceBackpack());

        // recolor backpack
        GameRegistry.addRecipe(new RecipeRecolorBackpack());
    }
}