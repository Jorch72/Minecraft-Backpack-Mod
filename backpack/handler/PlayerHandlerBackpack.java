package backpack.handler;

import net.minecraft.entity.player.EntityPlayer;
import backpack.Backpack;
import backpack.inventory.InventoryBackpackSlot;
import cpw.mods.fml.common.IPlayerTracker;

public class PlayerHandlerBackpack implements IPlayerTracker {

    @Override
    public void onPlayerLogin(EntityPlayer player) {
        Backpack.proxy.backpackSlot = new InventoryBackpackSlot(player);
        Backpack.proxy.backpackSlot.openChest();
    }

    @Override
    public void onPlayerLogout(EntityPlayer player) {
    }

    @Override
    public void onPlayerChangedDimension(EntityPlayer player) {
    }

    @Override
    public void onPlayerRespawn(EntityPlayer player) {
        boolean keepInv = player.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory");

        if(!keepInv) {
            player.getEntityData().removeTag("backpack");
        } else {
            Backpack.proxy.backpackSlot.writeToNBT(player);
        }

        Backpack.proxy.backpackSlot = new InventoryBackpackSlot(player);
        Backpack.proxy.backpackSlot.openChest();
    }
}