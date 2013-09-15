package backpack.inventory.container;

import invtweaks.api.ContainerGUI.ContainerSectionCallback;
import invtweaks.api.ContainerSection;

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
import backpack.gui.parts.GuiPart;
import backpack.gui.parts.GuiPartBackpack;
import backpack.gui.parts.GuiPartPlayerInventory;
import backpack.gui.parts.GuiPartWorkbench;
import backpack.inventory.InventoryCraftingAdvanced;
import backpack.item.ItemBackpackBase;

public class ContainerWorkbenchBackpack extends ContainerAdvanced {
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult = new InventoryCraftResult();
    private World worldObj;

    public ContainerWorkbenchBackpack(InventoryPlayer playerInventory, IInventory backpackInventory, ItemStack backpackIS) {
        super(playerInventory, backpackInventory, backpackIS);

        worldObj = playerInventory.player.worldObj;
        craftMatrix = new InventoryCraftingAdvanced(this, backpackInventory);

        // init parts
        GuiPart workbench = new GuiPartWorkbench(this, backpackInventory, playerInventory);
        GuiPart backpack = new GuiPartBackpack(this, backpackInventory, upperInventoryRows, false);
        GuiPart player = new GuiPartPlayerInventory(this, playerInventory, false);
        GuiPart hotbar = new GuiPartPlayerInventory(this, playerInventory, true);

        // set spacings
        backpack.setSpacings(6, 0);
        player.setSpacings(13, 6);

        // set offsets
        int offset = 16;
        workbench.setOffsetY(offset);
        offset += workbench.ySize;
        backpack.setOffsetY(offset);
        offset += backpack.ySize;
        player.setOffsetY(offset);
        offset += player.ySize;
        hotbar.setOffsetY(offset);

        // add slots
        workbench.addSlots();
        player.addSlots();
        hotbar.addSlots();
        backpack.addSlots();

        parts.add(workbench);
        parts.add(backpack);
        parts.add(player);
        parts.add(hotbar);

        onCraftMatrixChanged(craftMatrix);
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory) {
        craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix, worldObj));
    }

    @Override
    public void onCraftGuiClosed(EntityPlayer entityplayer) {
        if(!worldObj.isRemote) {
            for(int i = 0; i < 9; ++i) {
                ItemStack itemstack = craftMatrix.getStackInSlotOnClosing(i);

                if(itemstack != null && itemstack.getItem() instanceof ItemBackpackBase) {
                    entityplayer.dropPlayerItem(itemstack);
                }
            }
        }

        super.onCraftGuiClosed(entityplayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotPos) {
        ItemStack returnStack = null;
        Slot slot = (Slot) inventorySlots.get(slotPos);

        if(slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();
            returnStack = itemStack.copy();

            if(slotPos == 0) { // from craftingSlot
                if(!mergeItemStackWithBackpack(itemStack)) { // to backpack inventory
                    if(!mergeItemStack(itemStack, 37, 46, true)) { // to hotbar
                        if(!mergeItemStack(itemStack, 10, 37, false)) { // to inventory
                            return null;
                        }
                    }
                }

                slot.onSlotChange(itemStack, returnStack);
            } else if(slotPos >= 1 && slotPos < 10) { // from crafting matrix
                if(!mergeItemStackWithBackpack(itemStack)) { // to backpack inventory
                    if(!mergeItemStack(itemStack, 37, 46, true)) { // to hotbar
                        if(!mergeItemStack(itemStack, 10, 37, false)) { // to inventory
                            return null;
                        }
                    }
                }
            } else if(slotPos >= 10 && slotPos < 37) { // from inventory
                if(!mergeItemStackWithBackpack(itemStack)) { // to backpack inventory
                    if(!mergeItemStack(itemStack, 37, 46, true)) { // to hotbar
                        return null;
                    }
                }
            } else if(slotPos >= 37 && slotPos < 46) { // from hotbar
                if(!mergeItemStackWithBackpack(itemStack)) { // to backpack inventory
                    if(!mergeItemStack(itemStack, 10, 37, false)) { // to inventory
                        return null;
                    }
                }
            } else if(upperInventoryRows > 0 && slotPos >= 46 && slotPos < 64) { // from backpack inventory
                if(!mergeItemStack(itemStack, 37, 46, true)) { // to hotbar
                    if(!mergeItemStack(itemStack, 10, 37, false)) { // to inventory
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
        if(upperInventoryRows > 0 && !(itemStack.getItem() instanceof ItemBackpackBase)) {
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