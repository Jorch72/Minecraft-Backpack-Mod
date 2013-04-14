package backpack.recipes;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import backpack.Backpack;
import backpack.misc.ConfigurationBackpack;
import backpack.misc.Constants;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class RecipeHelper {
    public static void registerRecipes() {
        ItemStack backpackStack = new ItemStack(Backpack.backpack, 1, 16);
        ItemStack boundLeatherStack = new ItemStack(Backpack.boundLeather);
        ItemStack colorStack = new ItemStack(Item.dyePowder, 1, 0);

        // normal backpack without dye
        GameRegistry.addRecipe(backpackStack, "LLL", "L L", "LLL", 'L', Item.leather);

        // normal big backpack without dye
        backpackStack = new ItemStack(Backpack.backpack, 1, 48);
        GameRegistry.addRecipe(backpackStack, "LLL", "L L", "LLL", 'L', Backpack.tannedLeather);

        // backpacks and big backpacks from black(0) to white(15)
        for(int i = 0; i < 16; i++) {
            // the dye
            colorStack = new ItemStack(Item.dyePowder, 1, i);

            // backpacks
            backpackStack = new ItemStack(Backpack.backpack, 1, i);
            GameRegistry.addRecipe(backpackStack, "LLL", "LDL", "LLL", 'L', Item.leather, 'D', colorStack);
            LanguageRegistry.addName(backpackStack, Constants.BACKPACK_NAMES[i]);

            // big backpacks
            backpackStack = new ItemStack(Backpack.backpack, 1, i + 32);
            GameRegistry.addRecipe(backpackStack, "LLL", "LDL", "LLL", 'L', Backpack.tannedLeather, 'D', colorStack);
            LanguageRegistry.addName(backpackStack, "Big " + Constants.BACKPACK_NAMES[i]);
        }

        // ender Backpack
        if(ConfigurationBackpack.ENDER_RECIPE == 0) {
            backpackStack = new ItemStack(Backpack.backpack, 1, Constants.ENDERBACKPACK);
            GameRegistry.addRecipe(backpackStack, "LLL", "LEL", "LLL", 'L', Item.leather, 'E', Block.enderChest);
        } else {
            backpackStack = new ItemStack(Backpack.backpack, 1, Constants.ENDERBACKPACK);
            GameRegistry.addRecipe(backpackStack, "LLL", "LDL", "LLL", 'L', Item.leather, 'D', Item.eyeOfEnder);
        }
        LanguageRegistry.addName(backpackStack, Constants.BACKPACK_NAMES[16]);

        // bound leather
        GameRegistry.addRecipe(boundLeatherStack, "SSS", "LSL", "SSS", 'S', Item.silk, 'L', Item.leather);
        LanguageRegistry.addName(boundLeatherStack, "Bound Leather");

        // tanned leather
        ItemStack tannedLeatherStack = new ItemStack(Backpack.tannedLeather);
        GameRegistry.addSmelting(Backpack.boundLeather.itemID, tannedLeatherStack, 0.1f);
        LanguageRegistry.addName(tannedLeatherStack, "Tanned Leather");

        // enhance backpack to big backpack
        GameRegistry.addRecipe(new RecipeEnhanceBackpack());

        // recolor backpack
        GameRegistry.addRecipe(new RecipeRecolorBackpack());
    }
}
