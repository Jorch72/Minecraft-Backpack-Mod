package backpack.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import backpack.gui.parts.GuiPart;
import backpack.gui.parts.GuiPart.TEXTPOSITION;
import backpack.gui.parts.GuiPartBackpack;
import backpack.gui.parts.GuiPartBrewing;
import backpack.gui.parts.GuiPartFlexible;
import backpack.gui.parts.GuiPartFurnace;
import backpack.gui.parts.GuiPartPlayerInventory;
import backpack.gui.parts.GuiPartScrolling;
import backpack.item.ItemBackpackBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerBackpackCombined extends ContainerAdvanced {
    protected int backpackStartSlot;
    protected int backpackEndSlot;

    public ContainerBackpackCombined(IInventory playerInventory, IInventory otherInventory, IInventory backpackInventory, ItemStack backpack) {
        super(backpackInventory, otherInventory, backpack);

        GuiPart top;
        // init parts
        if(otherInventory instanceof TileEntityFurnace) {
            top = new GuiPartFurnace(this, ((InventoryPlayer) playerInventory).player, otherInventory, upperInventoryRows);
            top.setTextPosition(TEXTPOSITION.MIDDLE);
        } else if(otherInventory instanceof TileEntityDispenser || otherInventory instanceof TileEntityDropper) {
            top = new GuiPartFlexible(this, otherInventory, 3, 3);
            top.setTextPosition(TEXTPOSITION.MIDDLE);
        } else if(otherInventory instanceof TileEntityHopper) {
            top = new GuiPartFlexible(this, otherInventory, upperInventoryRows);
        } else if(otherInventory instanceof TileEntityBrewingStand) {
            top = new GuiPartBrewing(this, otherInventory, upperInventoryRows);
            top.setTextPosition(TEXTPOSITION.MIDDLE);
        } else {
            top = new GuiPartFlexible(this, otherInventory, upperInventoryRows, 9, lowerInventoryRows <= 3);
            ((GuiPartScrolling) top).setScrollbarOffset(-6);
        }

        GuiPart bottom = new GuiPartBackpack(this, backpackInventory, lowerInventoryRows, upperInventoryRows <= 3);
        ((GuiPartScrolling) bottom).setScrollbarOffset(2);

        GuiPart hotbar = new GuiPartPlayerInventory(this, playerInventory, true);

        // set spacings
        top.setSpacings(0, 6);
        bottom.setSpacings(7, 6);

        // set offsets
        int offset = 16;
        top.setOffsetY(offset);
        offset += top.ySize;
        bottom.setOffsetY(offset);
        offset += bottom.ySize;
        hotbar.setOffsetY(offset);

        // add slots
        top.addSlots();
        backpackStartSlot = inventorySlots.size();
        bottom.addSlots();
        backpackEndSlot = inventorySlots.size();
        hotbar.addSlots();

        parts.add(top);
        parts.add(bottom);
        parts.add(hotbar);
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1iCrafting) {
        super.addCraftingToCrafters(par1iCrafting);
        parts.get(0).addCraftingToCrafters(par1iCrafting);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        parts.get(0).detectAndSendChanges();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        parts.get(0).updateProgressBar(par1, par2);
    }

    /**
     * Called when a player shift-clicks on a slot.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotPos) {
        ItemStack returnStack = null;
        Slot slot = (Slot) inventorySlots.get(slotPos);

        if(slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();
            if(itemStack.getItem() instanceof ItemBackpackBase) {
                return returnStack;
            }
            returnStack = itemStack.copy();

            if(slotPos < backpackStartSlot) { // from other inventory
                if(!mergeItemStack(itemStack, backpackStartSlot, backpackEndSlot, false)) { // to backpack
                    return null;
                }
            } else if(slotPos >= backpackStartSlot && slotPos < backpackEndSlot) { // from backpack
                if(!mergeItemStack(itemStack, 0, backpackStartSlot, false)) { // to other inventory
                    return null;
                }
            } else if(!mergeItemStack(itemStack, 0, backpackEndSlot, false)) {
                return null;
            }

            if(itemStack.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }
        }

        return returnStack;
    }
}