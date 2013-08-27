package backpack.nei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import backpack.inventory.InventoryWorkbenchBackpack;
import codechicken.core.inventory.InventoryUtils;
import codechicken.nei.FastTransferManger;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.DefaultOverlayHandler;
import codechicken.nei.recipe.IRecipeHandler;

public class OverlayHandlerBackpack extends DefaultOverlayHandler {
    @Override
    public void overlayRecipe(GuiContainer gui, IRecipeHandler recipe, int recipeIndex, boolean shift) {
        List<PositionedStack> ingredients = recipe.getIngredientStacks(recipeIndex);
        List<DistributedIngred> ingredStacks = getPermutationIngredients(ingredients);

        findInventoryQuantities(gui, ingredStacks);

        List<IngredientDistribution> assignedIngredients = assignIngredients(ingredients, ingredStacks);
        if(assignedIngredients == null) {
            return;
        }

        assignIngredSlots(gui, ingredients, assignedIngredients);
        int quantity = calculateRecipeQuantity(assignedIngredients);

        if(quantity != 0) {
            moveIngredients(gui, assignedIngredients, quantity);
        }
    }

    /**
     * Returns a list of items in the recipe with the amount of the item needed
     * in the recipe.
     * 
     * @param ingredients
     *            The items in the recipe as a list of single items.
     * @return List of the items with the amount.
     */
    private List<DistributedIngred> getPermutationIngredients(List<PositionedStack> ingredients) {
        ArrayList<DistributedIngred> ingredStacks = new ArrayList<DistributedIngred>();
        for(PositionedStack posstack : ingredients) {
            for(ItemStack pstack : posstack.items) {
                DistributedIngred istack = findIngred(ingredStacks, pstack);
                if(istack == null) {
                    ingredStacks.add(istack = new DistributedIngred(pstack));
                }
                istack.recipeAmount += pstack.stackSize;
            }
        }
        return ingredStacks;
    }

    /**
     * Fills the items in the ingredStack List with the amount of items
     * available in the inventory.
     * 
     * @param gui
     *            The current open gui.
     * @param ingredStacks
     *            A list of items and the amount their in the recipe.
     */
    private void findInventoryQuantities(GuiContainer gui, List<DistributedIngred> ingredStacks) {
        for(Slot slot : (List<Slot>) gui.inventorySlots.inventorySlots) {
            if(slot.getHasStack()) {
                if(slot.inventory instanceof InventoryPlayer || slot.inventory instanceof InventoryWorkbenchBackpack) {
                    ItemStack pstack = slot.getStack();
                    DistributedIngred istack = findIngred(ingredStacks, pstack);
                    if(istack != null) {
                        istack.invAmount += pstack.stackSize;
                    }
                }
            }
        }
    }

    /**
     * Calculates how many items per slot of the recipe are possible with the
     * content of the inventory.
     * 
     * @param ingredients
     *            A List of the items in the recipe.
     * @param ingredStacks
     *            A List of the items with their amount in the recipe and the
     *            amount in the inventory.
     * @return A List of ingredients and how much is needed for balanced stacks.
     */
    private List<IngredientDistribution> assignIngredients(List<PositionedStack> ingredients, List<DistributedIngred> ingredStacks) {
        ArrayList<IngredientDistribution> assignedIngredients = new ArrayList<IngredientDistribution>();
        for(PositionedStack posstack : ingredients) {
            DistributedIngred biggestIngred = null;
            ItemStack permutation = null;
            int biggestSize = 0;
            for(ItemStack pstack : posstack.items) {
                for(int j = 0; j < ingredStacks.size(); j++) {
                    DistributedIngred istack = ingredStacks.get(j);
                    if(!InventoryUtils.canStack(pstack, istack.stack) || istack.invAmount - istack.distributed < pstack.stackSize) {
                        continue;
                    }

                    int relsize = (istack.invAmount - istack.invAmount / istack.recipeAmount * istack.distributed) / pstack.stackSize;
                    if(relsize > biggestSize) {
                        biggestSize = relsize;
                        biggestIngred = istack;
                        permutation = pstack;
                        break;
                    }
                }
            }

            if(biggestIngred == null) {
                return null;
            }

            biggestIngred.distributed += permutation.stackSize;
            assignedIngredients.add(new IngredientDistribution(biggestIngred, permutation));
        }

        return assignedIngredients;
    }

    /**
     * 
     * @param gui
     * @param ingredients
     * @param assignedIngredients
     * @return
     */
    private Slot[][] assignIngredSlots(GuiContainer gui, List<PositionedStack> ingredients, List<IngredientDistribution> assignedIngredients) {
        Slot[][] recipeSlots = mapIngredSlots(gui, ingredients);

        HashMap<Slot, Integer> distribution = new HashMap<Slot, Integer>();
        for(int i = 0; i < recipeSlots.length; i++) {
            for(Slot slot : recipeSlots[i]) {
                if(!distribution.containsKey(slot)) {
                    distribution.put(slot, -1);
                }
            }
        }

        HashSet<Slot> avaliableSlots = new HashSet<Slot>(distribution.keySet());
        HashSet<Integer> remainingIngreds = new HashSet<Integer>();
        ArrayList<LinkedList<Slot>> assignedSlots = new ArrayList<LinkedList<Slot>>();
        for(int i = 0; i < ingredients.size(); i++) {
            remainingIngreds.add(i);
            assignedSlots.add(new LinkedList<Slot>());
        }

        while(avaliableSlots.size() > 0 && remainingIngreds.size() > 0) {
            for(Iterator<Integer> iterator = remainingIngreds.iterator(); iterator.hasNext();) {
                int i = iterator.next();
                boolean assigned = false;
                DistributedIngred istack = assignedIngredients.get(i).distrib;

                for(Slot slot : recipeSlots[i]) {
                    if(avaliableSlots.contains(slot)) {
                        avaliableSlots.remove(slot);
                        if(slot.getHasStack()) {
                            continue;
                        }

                        istack.numSlots++;
                        assignedSlots.get(i).add(slot);
                        assigned = true;
                        break;
                    }
                }

                if(!assigned || istack.numSlots * istack.stack.getMaxStackSize() >= istack.invAmount) {
                    iterator.remove();
                }
            }
        }

        for(int i = 0; i < ingredients.size(); i++) {
            assignedIngredients.get(i).slots = assignedSlots.get(i).toArray(new Slot[0]);
        }
        return recipeSlots;
    }

    private int calculateRecipeQuantity(List<IngredientDistribution> assignedIngredients) {
        int quantity = Integer.MAX_VALUE;
        for(IngredientDistribution distrib : assignedIngredients) {
            DistributedIngred istack = distrib.distrib;
            if(istack.numSlots == 0) {
                return 0;
            }

            int allSlots = istack.invAmount;
            if(allSlots / istack.numSlots > istack.stack.getMaxStackSize()) {
                allSlots = istack.numSlots * istack.stack.getMaxStackSize();
            }

            quantity = Math.min(quantity, allSlots / istack.distributed);
        }

        return quantity;
    }

    private void moveIngredients(GuiContainer gui, List<IngredientDistribution> assignedIngredients, int quantity) {
        for(IngredientDistribution distrib : assignedIngredients) {
            ItemStack pstack = distrib.permutation;
            int transferCap = quantity * pstack.stackSize;
            int transferred = 0;

            int destSlotIndex = 0;
            Slot dest = distrib.slots[0];
            int slotTransferred = 0;
            int slotTransferCap = pstack.getMaxStackSize();

            for(Slot slot : (List<Slot>) gui.inventorySlots.inventorySlots) {
                if(!slot.getHasStack()) {
                    continue;
                }

                if(!(slot.inventory instanceof InventoryPlayer || slot.inventory instanceof InventoryWorkbenchBackpack)) {
                    continue;
                }

                ItemStack stack = slot.getStack();
                if(!InventoryUtils.canStack(stack, pstack)) {
                    continue;
                }

                FastTransferManger.clickSlot(gui, slot.slotNumber);
                int amount = Math.min(transferCap - transferred, stack.stackSize);
                for(int c = 0; c < amount; c++) {
                    FastTransferManger.clickSlot(gui, dest.slotNumber, 1);
                    transferred++;
                    slotTransferred++;
                    if(slotTransferred >= slotTransferCap) {
                        destSlotIndex++;
                        if(destSlotIndex == distrib.slots.length) {
                            dest = null;
                            break;
                        }
                        dest = distrib.slots[destSlotIndex];
                        slotTransferred = 0;
                    }
                }
                FastTransferManger.clickSlot(gui, slot.slotNumber);
                if(transferred >= transferCap || dest == null) {
                    break;
                }
            }
        }
    }
}
