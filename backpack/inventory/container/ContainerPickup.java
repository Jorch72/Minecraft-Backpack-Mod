package backpack.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import backpack.inventory.slot.SlotBackpack;

public class ContainerPickup extends ContainerAdvanced {
    public ContainerPickup(IInventory inventory) {
        super(null, inventory, null);
        for(int i = 0; i < inventory.getSizeInventory(); i++) {
            addSlotToContainer(new SlotBackpack(inventory, i, 0, 0));
        }
    }

    public void pickupItem(ItemStack itemStack) {
        mergeItemStack(itemStack, 0, this.inventorySlots.size(), false);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }
}