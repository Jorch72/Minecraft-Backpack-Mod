package backpack.inventory;

import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import backpack.gui.combined.GuiPart;
import backpack.gui.combined.GuiPartBackpack;
import backpack.gui.combined.GuiPartPlayerInventory;
import backpack.gui.combined.GuiPartScrolling;
import backpack.util.IBackpack;

@ChestContainer
public class ContainerBackpack extends ContainerAdvanced {
    public GuiPart top;
    public GuiPart bottom;
    public GuiPart hotbar;

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
            if(itemStack.getItem() instanceof IBackpack) {
                return returnStack;
            }
            returnStack = itemStack.copy();

            if(slotPos < upperInventoryRows * 9) {
                if(!mergeItemStack(itemStack, upperInventoryRows * 9, inventorySlots.size(), true)) {
                    return null;
                }
            } else if(!mergeItemStack(itemStack, 0, upperInventoryRows * 9, false)) {
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