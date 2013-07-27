package backpack.inventory;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import backpack.gui.combined.GuiPart;
import backpack.gui.combined.GuiPartBrewing;
import backpack.gui.combined.GuiPartChest;
import backpack.gui.combined.GuiPartDispenser;
import backpack.gui.combined.GuiPartFurnace;
import backpack.gui.combined.GuiPartHopper;
import backpack.util.IBackpack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerBackpackCombined extends ContainerAdvanced {
    protected ItemStack openedBackpack;
    protected int backpackStartSlot;
    protected int backpackEndSlot;
    public GuiPart top;

    public ContainerBackpackCombined(IInventory playerInventory, IInventory otherInventory, IInventory backpackInventory, ItemStack backpack) {
        super(backpackInventory, otherInventory, backpack);

        if(otherInventory instanceof TileEntityFurnace) {
            top = new GuiPartFurnace(otherInventory, upperInventoryRows);
        } else if(otherInventory instanceof TileEntityDispenser || otherInventory instanceof TileEntityDropper) {
            top = new GuiPartDispenser(otherInventory, upperInventoryRows);
        } else if(otherInventory instanceof TileEntityHopper) {
            top = new GuiPartHopper(otherInventory, upperInventoryRows);
        } else if(otherInventory instanceof TileEntityBrewingStand) {
            top = new GuiPartBrewing(otherInventory, upperInventoryRows);
        } else {
            top = new GuiPartChest(otherInventory, upperInventoryRows);
        }

        int x = LEFTSPACING;
        int y = TOPSPACING;

        // other inventory
        top.addSlots(this);

        backpackStartSlot = inventorySlots.size();

        int rows = lowerInventoryRows > 3 ? 3 : lowerInventoryRows;
        y = top.ySize + INVENTORYSPACING + 1;

        // backpack
        for(int row = 0; row < rows; ++row) {
            for(int col = 0; col < 9; ++col) {
                addSlotToContainer(new SlotBackpack(backpackInventory, col + row * 9, x, y));
                x += SLOT;
            }
            y += SLOT;
            x = LEFTSPACING;
        }

        backpackEndSlot = inventorySlots.size();

        y += HOTBARSPACING;

        // hot bar
        for(int col = 0; col < 9; ++col) {
            addSlotToContainer(new Slot(playerInventory, col, x, y));
            x += SLOT;
        }
    }

    public void addSlot(Slot slot) {
        addSlotToContainer(slot);
    }

    public List<ICrafting> getCrafters() {
        return crafters;
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1iCrafting) {
        super.addCraftingToCrafters(par1iCrafting);
        top.addCraftingToCrafters(this, par1iCrafting);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        top.detectAndSendChanges(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        top.updateProgressBar(par1, par2);
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
            if(itemStack.getItem() instanceof IBackpack) {
                return returnStack;
            }
            returnStack = itemStack.copy();

            if(slotPos < backpackStartSlot) {
                if(!mergeItemStack(itemStack, backpackStartSlot, backpackEndSlot, false)) {
                    return null;
                }
            } else if(slotPos >= backpackStartSlot && slotPos < backpackEndSlot) {
                if(!mergeItemStack(itemStack, 0, backpackStartSlot, false)) {
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