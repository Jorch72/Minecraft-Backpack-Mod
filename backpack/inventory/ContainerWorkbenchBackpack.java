package backpack.inventory;

import invtweaks.api.container.ChestContainer;
import invtweaks.api.container.ContainerSection;
import invtweaks.api.container.ContainerSectionCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import backpack.util.IBackpack;

@ChestContainer
public class ContainerWorkbenchBackpack extends ContainerAdvanced {
    private InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    private IInventory craftResult = new InventoryCraftResult();
    private World worldObj;

    public ContainerWorkbenchBackpack(InventoryPlayer playerInventory, IInventory backpackInventory, ItemStack backpack) {
        super(playerInventory, backpackInventory, backpack);

        TOPSPACING = 75;
        if(upperInventoryRows > 0) {
            INVENTORYSPACING = 14;
        } else {
            INVENTORYSPACING = 9;
        }

        worldObj = playerInventory.player.worldObj;
        backpackInventory.openChest();

        craftMatrix = new InventoryCraftingAdvanced(this, backpackInventory);

        // result slot
        addSlotToContainer(new SlotCraftingAdvanced(playerInventory.player, craftMatrix, craftResult, backpackInventory, 0, 125, 35));

        // crafting grid
        for(int row = 0; row < 3; row++) {
            for(int col = 0; col < 3; col++) {
                addSlotToContainer(new Slot(craftMatrix, col + row * 3, 30 + col * SLOT, 17 + row * SLOT));
            }
        }

        int x = LEFTSPACING;
        int y = TOPSPACING + upperInventoryRows * SLOT + INVENTORYSPACING;

        // inventory
        for(int row = 0; row < 3; row++) {
            for(int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
                x += SLOT;
            }
            y += SLOT;
            x = LEFTSPACING;
        }

        y += HOTBARSPACING;

        // hotbar
        for(int col = 0; col < 9; col++) {
            addSlotToContainer(new Slot(playerInventory, col, x, y));
            x += SLOT;
        }

        x = LEFTSPACING;
        y = TOPSPACING;

        // backpack
        for(int row = 0; row < upperInventoryRows; ++row) {
            for(int col = 0; col < 9; ++col) {
                addSlotToContainer(new SlotBackpack(backpackInventory, col + row * 9, x, y));
                x += SLOT;
            }
            y += SLOT;
            x = LEFTSPACING;
        }

        onCraftMatrixChanged(craftMatrix);
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory) {
        craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix, worldObj));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotPos) {
        ItemStack returnStack = null;
        Slot slot = (Slot) inventorySlots.get(slotPos);

        if(slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();
            returnStack = itemStack.copy();

            if(slotPos == 0) { // from craftingSlot
                if(!mergeItemStackWithBackpack(itemStack)) { // to backpack
                                                             // inventory
                    if(!mergeItemStack(itemStack, 37, 46, true)) { // to hotbar
                        if(!mergeItemStack(itemStack, 10, 37, false)) { // to
                                                                        // inventory
                            return null;
                        }
                    }
                }

                slot.onSlotChange(itemStack, returnStack);
            } else if(slotPos >= 1 && slotPos < 10) { // from crafting matrix
                if(!mergeItemStackWithBackpack(itemStack)) { // to backpack
                                                             // inventory
                    if(!mergeItemStack(itemStack, 37, 46, true)) { // to hotbar
                        if(!mergeItemStack(itemStack, 10, 37, false)) { // to
                                                                        // inventory
                            return null;
                        }
                    }
                }
            } else if(slotPos >= 10 && slotPos < 37) { // from inventory
                if(!mergeItemStackWithBackpack(itemStack)) { // to backpack
                                                             // inventory
                    if(!mergeItemStack(itemStack, 37, 46, true)) { // to hotbar
                        return null;
                    }
                }
            } else if(slotPos >= 37 && slotPos < 46) { // from hotbar
                if(!mergeItemStackWithBackpack(itemStack)) { // to backpack
                                                             // inventory
                    if(!mergeItemStack(itemStack, 10, 37, false)) { // to
                                                                    // inventory
                        return null;
                    }
                }
            } else if(upperInventoryRows > 0 && slotPos >= 46 && slotPos < 64) { // from
                                                                                 // backpack
                                                                                 // inventory
                if(!mergeItemStack(itemStack, 37, 46, true)) { // to hotbar
                    if(!mergeItemStack(itemStack, 10, 37, false)) { // to
                                                                    // inventory
                        return null;
                    }
                }
            } else if(!mergeItemStack(itemStack, 10, 45, false)) {
                return null;
            }

            if(itemStack.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }

            if(itemStack.stackSize == returnStack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemStack);

        }

        return returnStack;
    }

    protected boolean mergeItemStackWithBackpack(ItemStack itemStack) {
        if(upperInventoryRows > 0 && !(itemStack.getItem() instanceof IBackpack)) {
            return mergeItemStack(itemStack, 46, 64, false);
        }
        return false;
    }

    @ContainerSectionCallback
    public Map<ContainerSection, List<Slot>> getContainerSections() {
        Map<ContainerSection, List<Slot>> slotRefs = new HashMap<ContainerSection, List<Slot>>();

        slotRefs.put(ContainerSection.CRAFTING_OUT, inventorySlots.subList(0, 1));
        slotRefs.put(ContainerSection.CRAFTING_IN_PERSISTENT, inventorySlots.subList(1, 10));
        slotRefs.put(ContainerSection.INVENTORY, inventorySlots.subList(10, 46));
        slotRefs.put(ContainerSection.INVENTORY_NOT_HOTBAR, inventorySlots.subList(10, 37));
        slotRefs.put(ContainerSection.INVENTORY_HOTBAR, inventorySlots.subList(37, 46));
        if(upperInventoryRows > 0) {
            slotRefs.put(ContainerSection.CHEST, inventorySlots.subList(46, 64));
        }
        return slotRefs;
    }

    @Override
    public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot) {
        return par2Slot.inventory != craftResult && super.func_94530_a(par1ItemStack, par2Slot);
    }
}
