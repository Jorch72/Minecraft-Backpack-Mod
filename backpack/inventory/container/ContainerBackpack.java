package backpack.inventory.container;

import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import backpack.gui.parts.GuiPart;
import backpack.gui.parts.GuiPartBackpack;
import backpack.gui.parts.GuiPartPlayerInventory;
import backpack.gui.parts.GuiPartScrolling;
import backpack.handler.PacketHandlerBackpack;
import backpack.inventory.slot.SlotScrolling;
import backpack.item.ItemBackpackBase;

@ChestContainer
public class ContainerBackpack extends ContainerAdvanced {
    public GuiPart top;
    public GuiPart bottom;

    public ContainerBackpack(IInventory playerInventory, IInventory backpackInventory, ItemStack backpack) {
        super(playerInventory, backpackInventory, backpack);

        // init gui parts
        top = new GuiPartBackpack(this, backpackInventory, upperInventoryRows, true);
        bottom = new GuiPartPlayerInventory(this, playerInventory, false);
        hotbar = new GuiPartPlayerInventory(this, playerInventory, true);

        // init scrollbar
        ((GuiPartScrolling) top).setScrollbarOffset(-6);

        // set spacings
        top.setSpacings(0, 6);
        bottom.setSpacings(8, 6);

        // set offsets
        int offset = 16;
        top.setOffsetY(offset);
        offset += top.ySize;
        bottom.setOffsetY(offset);
        offset += bottom.ySize;
        hotbar.setOffsetY(offset);

        // add slots
        top.addSlots();
        bottom.addSlots();
        hotbar.addSlots();
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

            if(slotPos < top.lastSlot) { // from backpack
                if(!mergeItemStack(itemStack, hotbar.firstSlot, hotbar.lastSlot, true)) { // to hotbar
                    if(!mergeItemStack(itemStack, bottom.firstSlot, bottom.lastSlot, false)) { // to inventory
                        return null;
                    }
                }
            } else if(slotPos >= bottom.firstSlot && slotPos < bottom.lastSlot) { // from inventory
                if(!mergeItemStack(itemStack, top.firstSlot, top.lastSlot, false)) { // to backpack
                    if(!mergeItemStack(itemStack, hotbar.firstSlot, hotbar.lastSlot, true)) { // to hotbar
                        return null;
                    }
                }
            } else { // from hotbar
                if(!mergeItemStack(itemStack, top.firstSlot, top.lastSlot, false)) { // to backpack
                    if(!mergeItemStack(itemStack, bottom.firstSlot, bottom.lastSlot, true)) { // to inventory
                        return null;
                    }
                }
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
        }
    }

    @Override
    public void updateSlots(int part, int offset) {
        int slotNumber = top.firstSlot;
        int inventoryRows = top.inventoryRows;
        int inventoryCols = top.inventoryCols;

        for(int row = 0; row < inventoryRows; ++row) {
            for(int col = 0; col < inventoryCols; ++col) {
                int slotIndex = col + (row + offset) * inventoryCols;

                SlotScrolling slot = (SlotScrolling) inventorySlots.get(slotNumber);

                slot.setSlotIndex(slotIndex);
                slotNumber++;
            }
        }
        detectAndSendChanges();
    }
}