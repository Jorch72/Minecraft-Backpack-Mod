package backpack.inventory;

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
import backpack.gui.combined.GuiPart.TEXTPOSITION;
import backpack.gui.combined.GuiPartBackpack;
import backpack.gui.combined.GuiPartBrewing;
import backpack.gui.combined.GuiPartFlexible;
import backpack.gui.combined.GuiPartFurnace;
import backpack.gui.combined.GuiPartPlayerInventory;
import backpack.gui.combined.GuiPartScrolling;
import backpack.util.IBackpack;
import backpack.util.PacketHandlerBackpack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerBackpackCombined extends ContainerAdvanced {
    protected int backpackStartSlot;
    protected int backpackEndSlot;
    public GuiPart top;
    public GuiPart bottom;

    public ContainerBackpackCombined(IInventory playerInventory, IInventory otherInventory, IInventory backpackInventory, ItemStack backpack) {
        super(backpackInventory, otherInventory, backpack);

        // init parts
        if(otherInventory instanceof TileEntityFurnace) {
            top = new GuiPartFurnace(this, otherInventory, upperInventoryRows);
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

        bottom = new GuiPartBackpack(this, backpackInventory, lowerInventoryRows, upperInventoryRows <= 3);
        ((GuiPartScrolling) bottom).setScrollbarOffset(2);

        hotbar = new GuiPartPlayerInventory(this, playerInventory, true);

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
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1iCrafting) {
        super.addCraftingToCrafters(par1iCrafting);
        top.addCraftingToCrafters(par1iCrafting);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        top.detectAndSendChanges();
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
    
    @Override
    public void sendScrollbarToServer(GuiPart guiPart, int offset) {
        if(guiPart == top) {
            PacketHandlerBackpack.sendScrollbarPositionToServer(0, offset);
        } else if(guiPart == bottom) {
            PacketHandlerBackpack.sendScrollbarPositionToServer(1, offset);
        }
    }
    
    @Override
    public void updateSlots(int part, int offset) {
        int slotNumber, inventoryRows, inventoryCols;
        if(part == 0) {
            slotNumber = top.firstSlot;
            inventoryRows = top.inventoryRows;
            inventoryCols = top.inventoryCols;
        } else {
            slotNumber = bottom.firstSlot;
            inventoryRows = bottom.inventoryRows;
            inventoryCols = bottom.inventoryCols;
        }

        for(int row = 0; row < inventoryRows; ++row) {
            for(int col = 0; col < inventoryCols; ++col) {
                int slotIndex = col + (row + offset) * inventoryCols;

                SlotScrolling slot = (SlotScrolling)inventorySlots.get(slotNumber);
                
                slot.setSlotIndex(slotIndex);
                slotNumber++;
            }
        }
        detectAndSendChanges();
    }
}