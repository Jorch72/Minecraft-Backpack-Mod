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
import backpack.item.ItemBackpackBase;

@ChestContainer
public class ContainerBackpack extends ContainerAdvanced {

    public ContainerBackpack(IInventory playerInventory, IInventory backpackInventory, ItemStack backpack) {
        super(playerInventory, backpackInventory, backpack);

        // init gui parts
        GuiPart top = new GuiPartBackpack(this, upperInventory, upperInventoryRows, true);
        GuiPart bottom = new GuiPartPlayerInventory(this, lowerInventory, false);
        GuiPart hotbar = new GuiPartPlayerInventory(this, lowerInventory, true);

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

        parts.add(top);
        parts.add(bottom);
        parts.add(hotbar);
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

            if(slotPos < parts.get(0).lastSlot) { // from backpack
                if(!mergeItemStack(itemStack, parts.get(2).firstSlot, parts.get(2).lastSlot, true)) { // to hotbar
                    if(!mergeItemStack(itemStack, parts.get(1).firstSlot, parts.get(1).lastSlot, false)) { // to inventory
                        return null;
                    }
                }
            } else if(slotPos >= parts.get(1).firstSlot && slotPos < parts.get(1).lastSlot) { // from inventory
                if(!mergeItemStack(itemStack, parts.get(0).firstSlot, parts.get(0).lastSlot, false)) { // to backpack
                    if(!mergeItemStack(itemStack, parts.get(2).firstSlot, parts.get(2).lastSlot, true)) { // to hotbar
                        return null;
                    }
                }
            } else { // from hotbar
                if(!mergeItemStack(itemStack, parts.get(0).firstSlot, parts.get(0).lastSlot, false)) { // to backpack
                    if(!mergeItemStack(itemStack, parts.get(1).firstSlot, parts.get(1).lastSlot, true)) { // to inventory
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