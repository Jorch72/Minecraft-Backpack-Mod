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
    public ContainerBackpackSlot(IInventory playerInventory, IInventory backpackInventory) {
        super(playerInventory, backpackInventory, null);

        // init gui parts
        GuiPart top = new GuiPartBackpackSlot(this, backpackInventory);
        GuiPart bottom = new GuiPartPlayerInventory(this, playerInventory, false);
        GuiPart hotbar = new GuiPartPlayerInventory(this, playerInventory, true);

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

        parts.add(top);
        parts.add(bottom);
        parts.add(hotbar);
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
                if(!mergeItemStack(itemStack, parts.get(1).firstSlot, parts.get(2).lastSlot, true)) { // to inventory + hotbar
                    return null;
                }
            } else if(slotPos >= parts.get(1).firstSlot && slotPos < parts.get(1).lastSlot) { // from inventory
                if(itemStack.getItem() instanceof ItemBackpackBase) { // if backpack
                    if(!mergeItemStack(itemStack, 0, 1, false)) { // to backpack slot
                        if(!mergeItemStack(itemStack, parts.get(2).firstSlot, parts.get(2).lastSlot, true)) { // to hotbar
                            return null;
                        }
                    }
                } else { // if not a backpack
                    if(!mergeItemStack(itemStack, parts.get(2).firstSlot, parts.get(2).lastSlot, true)) { // to hotbar
                        return null;
                    }
                }
            } else { // from hotbar
                if(itemStack.getItem() instanceof ItemBackpackBase) { // if backpack
                    if(!mergeItemStack(itemStack, 0, 1, false)) { // to backpack slot
                        if(!mergeItemStack(itemStack, parts.get(1).firstSlot, parts.get(1).lastSlot, false)) { // to inventory
                            return null;
                        }
                    }
                } else { // if not a backpack
                    if(!mergeItemStack(itemStack, parts.get(1).firstSlot, parts.get(1).lastSlot, false)) { // to inventory
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