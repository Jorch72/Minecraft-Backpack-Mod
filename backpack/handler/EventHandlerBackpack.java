package backpack.handler;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

import backpack.Backpack;
import backpack.inventory.InventoryBackpackSlot;

public class EventHandlerBackpack {
    @ForgeSubscribe
    public void playerDropOnDeath(PlayerDropsEvent event) {
        EntityPlayer player = event.entityPlayer;
        InventoryBackpackSlot backpackSlotInventory = Backpack.playerTracker.getInventoryBackpackSlot(player);
        ItemStack backpack = backpackSlotInventory.getStackInSlot(0);
        if(backpack != null) {
            event.drops.add(new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, backpack));
            backpackSlotInventory.setInventorySlotContents(0, null);
            backpackSlotInventory.closeChest();
        }
    }
}