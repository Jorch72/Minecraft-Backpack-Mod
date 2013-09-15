package backpack.handler;

import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import backpack.inventory.InventoryBackpackSlot;
import cpw.mods.fml.common.IPlayerTracker;

public class PlayerHandlerBackpack implements IPlayerTracker {

    public ConcurrentHashMap<String, InventoryBackpackSlot> playerInventories = new ConcurrentHashMap<String, InventoryBackpackSlot>();

    @Override
    public void onPlayerLogin(EntityPlayer player) {
        InventoryBackpackSlot backpackSlot = new InventoryBackpackSlot(player);
        backpackSlot.openChest();

        playerInventories.put(player.username, backpackSlot);

        PacketHandlerBackpack.sendWearedBackpackDataToClient(player);
    }

    @Override
    public void onPlayerLogout(EntityPlayer player) {
        if(player != null) {
            InventoryBackpackSlot backpackSlot = getInventoryBackpackSlot(player);
            if(backpackSlot != null) {
                backpackSlot.closeChest();
                playerInventories.remove(player.username);
            }
        }
    }

    @Override
    public void onPlayerChangedDimension(EntityPlayer player) {
        if(player != null) {
            InventoryBackpackSlot backpackSlot = getInventoryBackpackSlot(player);
            if(backpackSlot != null) {
                backpackSlot.closeChest();
            }

            PacketHandlerBackpack.sendWearedBackpackDataToClient(player);
        }
    }

    @Override
    public void onPlayerRespawn(EntityPlayer player) {
        boolean keepInv = player.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory");

        if(!keepInv) {
            playerInventories.remove(player.username);
        }

        PacketHandlerBackpack.sendWearedBackpackDataToClient(player);
    }

    public InventoryBackpackSlot getInventoryBackpackSlot(EntityPlayer player) {
        InventoryBackpackSlot backpackSlot = playerInventories.get(player.username);
        if(backpackSlot == null) {
            backpackSlot = new InventoryBackpackSlot(player);
            playerInventories.put(player.username, backpackSlot);
        }
        return backpackSlot;
    }

    public ItemStack getBackpack(EntityPlayer player) {
        InventoryBackpackSlot backpackSlot = playerInventories.get(player.username);
        if(backpackSlot != null) {
            return backpackSlot.getStackInSlot(0);
        }
        return null;
    }
}