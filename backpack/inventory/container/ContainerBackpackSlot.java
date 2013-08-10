package backpack.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import backpack.gui.parts.GuiPart;
import backpack.gui.parts.GuiPartBackpackSlot;
import backpack.gui.parts.GuiPartPlayerInventory;
import backpack.item.ItemBackpackBase;

public class ContainerBackpackSlot extends ContainerAdvanced {
    public GuiPart top;
    public GuiPart bottom;

    public ContainerBackpackSlot(IInventory playerInventory, IInventory backpackInventory) {
        super(playerInventory, backpackInventory, null);

        // init gui parts
        top = new GuiPartBackpackSlot(this, backpackInventory);
        bottom = new GuiPartPlayerInventory(this, playerInventory, false);
        hotbar = new GuiPartPlayerInventory(this, playerInventory, true);

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

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
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
            returnStack = itemStack.copy();

            if(slotPos == 0) { // from backpack slot
                if(!mergeItemStack(itemStack, bottom.firstSlot, hotbar.lastSlot, true)) { // to inventory + hotbar
                    return null;
                }
            } else if(slotPos >= bottom.firstSlot && slotPos < bottom.lastSlot) { // from inventory
                if(itemStack.getItem() instanceof ItemBackpackBase) { // if backpack
                    if(!mergeItemStack(itemStack, 0, 1, false)) { // to backpack slot
                        if(!mergeItemStack(itemStack, hotbar.firstSlot, hotbar.lastSlot, true)) { // to hotbar
                            return null;
                        }
                    }
                } else { // if not a backpack
                    if(!mergeItemStack(itemStack, hotbar.firstSlot, hotbar.lastSlot, true)) { // to hotbar
                        return null;
                    }
                }
            } else { // from hotbar
                if(itemStack.getItem() instanceof ItemBackpackBase) { // if backpack
                    if(!mergeItemStack(itemStack, 0, 1, false)) { // to backpack slot
                        if(!mergeItemStack(itemStack, bottom.firstSlot, bottom.lastSlot, false)) { // to inventory
                            return null;
                        }
                    }
                } else { // if not a backpack
                    if(!mergeItemStack(itemStack, bottom.firstSlot, bottom.lastSlot, false)) { // to inventory
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
}