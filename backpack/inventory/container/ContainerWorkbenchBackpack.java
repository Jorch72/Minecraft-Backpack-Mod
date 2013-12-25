package backpack.inventory.container;

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
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import backpack.gui.parts.GuiPart;
import backpack.gui.parts.GuiPartBackpack;
import backpack.gui.parts.GuiPartPlayerInventory;
import backpack.gui.parts.GuiPartWorkbench;
import backpack.inventory.InventoryCraftingAdvanced;
import backpack.inventory.InventoryRecipes;
import backpack.inventory.slot.SlotPhantom;
import backpack.item.ItemBackpackBase;
import backpack.util.NBTUtil;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

@ChestContainer
public class ContainerWorkbenchBackpack extends ContainerAdvanced {
    public InventoryRecipes recipes = null;
    public InventoryCraftingAdvanced craftMatrix = null;
    public IInventory craftResult = new InventoryCraftResult();
    private World worldObj;
    public boolean intelligent = false;
    public boolean saveMode = false;

    public ContainerWorkbenchBackpack(InventoryPlayer playerInventory, IInventory backpackInventory, ItemStack backpackIS) {
        super(playerInventory, backpackInventory, backpackIS);

        worldObj = ((InventoryPlayer) lowerInventory).player.worldObj;
        craftMatrix = new InventoryCraftingAdvanced(this, upperInventory);
        craftMatrix.loadContent();

        if(backpackIS != null && NBTUtil.hasTag(backpackIS, "intelligent")) {
            intelligent = true;
            recipes = new InventoryRecipes(upperInventory);
        }

        // init parts
        GuiPart workbench = new GuiPartWorkbench(this, upperInventory, (InventoryPlayer) lowerInventory);
        GuiPart backpack = new GuiPartBackpack(this, upperInventory, upperInventoryRows, false);
        GuiPart player = new GuiPartPlayerInventory(this, lowerInventory, false);
        GuiPart hotbar = new GuiPartPlayerInventory(this, lowerInventory, true);

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
    public ItemStack slotClick(int slotIndex, int mouseButton, int modifier, EntityPlayer player) {
        Slot slot = slotIndex < 0 ? null : (Slot) inventorySlots.get(slotIndex);
        if(slot instanceof SlotPhantom) {
            if(slot.inventory == recipes) {
                if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
                    if(saveMode) {
                        saveMode = false;
                        recipes.setInventorySlotContents(slotIndex - 10, getSlot(0).getStack());
                        detectAndSendChanges();
                    } else {
                        craftMatrix.loadRecipe(slotIndex - 10);
                    }
                }
            } else {
                slotPhantomClick(slot, mouseButton, modifier, player.inventory.getItemStack());
            }
            return null;
        }
        return super.slotClick(slotIndex, mouseButton, modifier, player);
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory) {
        craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix, worldObj));
        detectAndSendChanges();
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
                return null;
            } else if(slotPos >= parts.get(2).firstSlot && slotPos < parts.get(2).lastSlot) { // from inventory
                if(!mergeItemStackWithBackpack(itemStack)) { // to backpack inventory
                    if(!mergeItemStack(itemStack, parts.get(3).firstSlot, parts.get(3).lastSlot, true)) { // to hotbar
                        return null;
                    }
                }
            } else if(slotPos >= parts.get(3).firstSlot && slotPos < parts.get(3).lastSlot) { // from hotbar
                if(!mergeItemStackWithBackpack(itemStack)) { // to backpack inventory
                    if(!mergeItemStack(itemStack, parts.get(2).firstSlot, parts.get(2).lastSlot, false)) { // to inventory
                        return null;
                    }
                }
            } else if(upperInventoryRows > 0 && slotPos >= parts.get(1).firstSlot && slotPos < parts.get(1).lastSlot) { // from backpack inventory
                if(!mergeItemStack(itemStack, parts.get(3).firstSlot, parts.get(3).lastSlot, true)) { // to hotbar
                    if(!mergeItemStack(itemStack, parts.get(2).firstSlot, parts.get(2).lastSlot, false)) { // to inventory
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

    /**
     * Handles clicking on a phantom slot.
     * 
     * @param slot
     *            The slot that has been clicked.
     * @param mouseButton
     *            The mouse button identifier: 0: left click 1: right click &
     *            left click during drag and drop 2: middle click (scrollwheel)
     * @param modifier
     *            The mouse modifier: 0: normal click 3: drag and drop middle
     *            click 5: drag and drop left or right click
     * @param stackHeld
     *            The stack that the player holds on his mouse.
     */
    protected void slotPhantomClick(Slot slot, int mouseButton, int modifier, ItemStack stackHeld) {
        if(((SlotPhantom) slot).canChangeStack()) {
            if(mouseButton == 2) {
                slot.putStack(null);
            } else {
                ItemStack phantomStack = null;

                if(stackHeld != null) {
                    phantomStack = stackHeld.copy();
                    phantomStack.stackSize = 1;
                }

                slot.putStack(phantomStack);
            }
            slot.onSlotChanged();
        }
    }

    protected boolean mergeItemStackWithBackpack(ItemStack itemStack) {
        if(upperInventoryRows > 0 && !(itemStack.getItem() instanceof ItemBackpackBase)) {
            return mergeItemStack(itemStack, parts.get(1).firstSlot, parts.get(1).lastSlot, false);
        }
        return false;
    }

    @ContainerSectionCallback
    public Map<ContainerSection, List<Slot>> getContainerSections() {
        Map<ContainerSection, List<Slot>> slotRefs = new HashMap<ContainerSection, List<Slot>>();

        slotRefs.put(ContainerSection.CRAFTING_OUT, inventorySlots.subList(0, 1));
        slotRefs.put(ContainerSection.CRAFTING_IN_PERSISTENT, inventorySlots.subList(1, 10));
        slotRefs.put(ContainerSection.INVENTORY, inventorySlots.subList(parts.get(2).firstSlot, parts.get(3).lastSlot));
        slotRefs.put(ContainerSection.INVENTORY_NOT_HOTBAR, inventorySlots.subList(parts.get(2).firstSlot, parts.get(2).lastSlot));
        slotRefs.put(ContainerSection.INVENTORY_HOTBAR, inventorySlots.subList(parts.get(3).firstSlot, parts.get(3).lastSlot));
        if(upperInventoryRows > 0) {
            slotRefs.put(ContainerSection.CHEST, inventorySlots.subList(parts.get(1).firstSlot, parts.get(1).lastSlot));
        }
        return slotRefs;
    }

    @Override
    public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot) {
        return par2Slot.inventory != craftResult && super.func_94530_a(par1ItemStack, par2Slot);
    }

    /**
     * Clears the craft matrix.
     */
    public void clearCraftMatrix() {
        for(int i = 1; i < 10; i++) {
            putStackInSlot(i, null);
        }
    }

    /**
     * Sets the save mode to true so a slot click in the recipe matrix will save
     * the ItemStack from the result slot.
     */
    public void setSaveMode() {
        Slot slot = getSlot(0);
        if(slot.getHasStack()) {
            saveMode = true;
        }
    }
}