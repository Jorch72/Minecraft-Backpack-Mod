package backpack.inventory.slot;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.oredict.OreDictionary;
import backpack.inventory.InventoryWorkbenchBackpack;
import backpack.inventory.container.ContainerWorkbenchBackpack;
import backpack.util.BackpackUtil;
import cpw.mods.fml.common.registry.GameRegistry;

public class SlotCraftingAdvanced extends SlotCrafting {
    protected IInventory craftMatrix;
    protected InventoryWorkbenchBackpack backpackInventory;
    protected Container container;

    public SlotCraftingAdvanced(EntityPlayer player, ContainerWorkbenchBackpack container, InventoryWorkbenchBackpack backpackInventory, int slotIndex, int xPosition, int yPosition) {
        super(player, container.craftMatrix, container.craftResult, slotIndex, xPosition, yPosition);
        this.craftMatrix = container.craftMatrix;
        this.backpackInventory = backpackInventory;
        this.container = container;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack ist) {
        ArrayList<ItemStack> currentRecipe = getRecipeIngredients();

        backpackInventory.setCraftingHandlerMode(true);
        GameRegistry.onItemCrafted(player, ist, backpackInventory);
        backpackInventory.setCraftingHandlerMode(false);
        onCrafting(ist);

        for(int i = 0; i < currentRecipe.size(); i++) {
            ItemStack ingredient = currentRecipe.get(i);

            // search for same item in backpack inventory and reduce ingredient
            // amount
            for(int j = 0; j < backpackInventory.getSizeInventory(); j++) {
                ItemStack itemstack = backpackInventory.getStackInSlot(j);

                if(itemstack != null) {
                    if(BackpackUtil.areStacksEqual(ingredient, itemstack)) {
                        ingredient.stackSize -= backpackInventory.decrStackSize(j, ingredient.stackSize).stackSize;
                        if(ingredient.getItem().hasContainerItem()) {
                            ItemStack containerItem = ingredient.getItem().getContainerItemStack(ingredient);

                            if(containerItem.isItemStackDamageable() && containerItem.getItemDamage() > containerItem.getMaxDamage()) {
                                MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, containerItem));
                                containerItem = null;
                            }

                            if(containerItem != null) {
                                if(backpackInventory.getStackInSlot(j) == null) {
                                    backpackInventory.setInventorySlotContents(j, containerItem);
                                } else {
                                    if(!mergeItemStack(containerItem, backpackInventory)) {
                                        if(!player.inventory.addItemStackToInventory(containerItem)) {
                                            player.dropPlayerItem(containerItem);
                                        }
                                    }
                                }
                            }
                        }
                        if(ingredient.stackSize == 0) {
                            break;
                        }
                    }
                }
            }

            // if ingredient amount is not zero search for alternative item (ore
            // dictionary)
            int slot = 0;
            while(ingredient.stackSize != 0) {
                slot = findAlternative(ingredient, slot);
                if(slot < 0) {
                    break;
                }
                ingredient.stackSize -= backpackInventory.decrStackSize(slot, ingredient.stackSize).stackSize;
            }
        }
        container.onCraftMatrixChanged(craftMatrix);
    }

    /**
     * Uses the oreDictionary to search for an alternative.
     * 
     * @param original
     *            The original ItemStack.
     * @return The index of the slot which has the same item as in the original
     *         ItemStack based on the OreDictionary.
     */
    protected int findAlternative(ItemStack original) {
        return findAlternative(original, 0);
    }

    /**
     * Uses the oreDictionary to search for an alternative.
     * 
     * @param original
     *            The original ItemStack.
     * @param startingSlot
     *            The slot to start with the search.
     * @return The index of the slot which has the same item as in the original
     *         ItemStack based on the OreDictionary.
     */
    protected int findAlternative(ItemStack original, int startingSlot) {
        int originalOreDictId = OreDictionary.getOreID(original);
        for(int i = startingSlot; i < backpackInventory.getSizeInventory(); i++) {
            ItemStack alternative = backpackInventory.getStackInSlot(i);
            if(alternative != null) {
                if(originalOreDictId == OreDictionary.getOreID(alternative)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
        ArrayList<ItemStack> currentRecipe = getRecipeIngredients();

        Iterator<ItemStack> recipeIterator = currentRecipe.iterator();
        while(recipeIterator.hasNext()) {
            ItemStack recipeStack = recipeIterator.next();
            for(int i = 0; i < backpackInventory.getSizeInventory(); i++) {
                ItemStack backpackStack = backpackInventory.getStackInSlot(i);
                if(BackpackUtil.areStacksEqual(recipeStack, backpackStack, true)) {
                    recipeStack.stackSize -= backpackStack.stackSize;
                    if(recipeStack.stackSize <= 0) {
                        recipeIterator.remove();
                        break;
                    }
                }
            }
        }

        return currentRecipe.isEmpty();
    }

    /**
     * Returns an ArrayList with the ingredients of the recipe. Each ItemStack
     * holds the amount of this item needed in the recipe.
     * 
     * @return An ArrayList with all the ingredients of the current recipe and
     *         the needed amount.
     */
    protected ArrayList<ItemStack> getRecipeIngredients() {
        ArrayList<ItemStack> currentRecipe = new ArrayList<ItemStack>();
        boolean add;

        for(int i = 0; i < craftMatrix.getSizeInventory(); i++) {
            add = true;
            ItemStack recipeStack = craftMatrix.getStackInSlot(i);
            if(recipeStack != null) {
                for(ItemStack stack : currentRecipe) {
                    if(stack.isItemEqual(recipeStack)) {
                        stack.stackSize++;
                        add = false;
                        break;
                    }
                }
                if(add) {
                    ItemStack ingredient = recipeStack.copy();
                    ingredient.stackSize = 1;
                    currentRecipe.add(ingredient);
                }
            }
        }
        return currentRecipe;
    }

    /**
     * Slightly modified version of Container.mergeItemStack(...)
     * 
     * @param sourceStack
     *            The stack that should be merged into the inventory.
     * @param inventory
     *            The inventory where the stack should go.
     * @return True if the stack was put into the inventory completely, false
     *         otherwise.
     */
    protected boolean mergeItemStack(ItemStack sourceStack, IInventory inventory) {
        boolean result = false;
        int currentSlotIndex = 0;

        ItemStack slotStack;

        if(sourceStack.isStackable()) {
            while(sourceStack.stackSize > 0 && currentSlotIndex < inventory.getSizeInventory()) {
                slotStack = inventory.getStackInSlot(currentSlotIndex);

                if(slotStack != null && slotStack.itemID == sourceStack.itemID && (!sourceStack.getHasSubtypes() || sourceStack.getItemDamage() == slotStack.getItemDamage())
                        && ItemStack.areItemStackTagsEqual(sourceStack, slotStack)) {
                    int l = slotStack.stackSize + sourceStack.stackSize;

                    if(l <= sourceStack.getMaxStackSize()) {
                        sourceStack.stackSize = 0;
                        slotStack.stackSize = l;
                        inventory.onInventoryChanged();
                        result = true;
                    } else if(slotStack.stackSize < sourceStack.getMaxStackSize()) {
                        sourceStack.stackSize -= sourceStack.getMaxStackSize() - slotStack.stackSize;
                        slotStack.stackSize = sourceStack.getMaxStackSize();
                        inventory.onInventoryChanged();
                        result = true;
                    }
                }

                currentSlotIndex++;
            }
        }

        if(sourceStack.stackSize > 0) {
            for(int i = 0; i < inventory.getSizeInventory(); i++) {
                slotStack = inventory.getStackInSlot(i);

                if(slotStack == null) {
                    inventory.setInventorySlotContents(i, sourceStack.copy());
                    sourceStack.stackSize = 0;
                    result = true;
                    break;
                }
            }
        }

        return result;
    }
}