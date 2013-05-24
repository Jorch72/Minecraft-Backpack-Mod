package backpack.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class SlotCraftingAdvanced extends SlotCrafting {
    protected IInventory craftMatrix;
    protected IInventory backpackInventory;

    public SlotCraftingAdvanced(EntityPlayer entityPlayer, IInventory craftMatrix, IInventory craftResult, IInventory backpackInventory, int par4, int par5, int par6) {
        super(entityPlayer, craftMatrix, craftResult, par4, par5, par6);
        this.craftMatrix = craftMatrix;
        this.backpackInventory = backpackInventory;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack ist) {
        ItemStack[] currentRecipe = new ItemStack[9];
        for(int i = 0; i < 9; i++) {
            ItemStack itemStack = craftMatrix.getStackInSlot(i);
            if(itemStack == null) {
                currentRecipe[i] = null;
            } else {
                currentRecipe[i] = itemStack.copy();
            }
        }

        super.onPickupFromSlot(player, ist);

        for(int i = 0; i < 9; i++) {
            if(currentRecipe[i] != null) {
                ItemStack currentInSlot = craftMatrix.getStackInSlot(i);

                if(currentInSlot != null) {
                    if(!currentInSlot.isItemEqual(currentRecipe[i])) {
                        if(currentRecipe[i].getItem().hasContainerItem()) {
                            ItemStack containerItem = currentRecipe[i].getItem().getContainerItemStack(currentRecipe[i]);
                            if(containerItem != null && currentInSlot.isItemEqual(containerItem)) {
                                int index = findAlternative(currentRecipe[i]);
                                if(index != -1) {
                                    ItemStack replace = backpackInventory.getStackInSlot(index);
                                    backpackInventory.setInventorySlotContents(index, currentInSlot);
                                    craftMatrix.setInventorySlotContents(i, replace);
                                }
                            }
                        }
                    }
                } else {
                    int index = findAlternative(currentRecipe[i]);
                    if(index != -1) {
                        craftMatrix.setInventorySlotContents(i, backpackInventory.decrStackSize(index, 1));
                    }
                }
            }
        }
    }

    protected int findAlternative(ItemStack original) {
        int originalOreDictId = OreDictionary.getOreID(original);
        for(int i = 0; i < backpackInventory.getSizeInventory(); i++) {
            ItemStack alternative = backpackInventory.getStackInSlot(i);
            if(alternative != null) {
                int alternativeOreDictId = OreDictionary.getOreID(alternative);
                if(originalOreDictId == alternativeOreDictId && original.isItemEqual(alternative)) {
                    return i;
                }
            }
        }
        return -1;
    }
}
