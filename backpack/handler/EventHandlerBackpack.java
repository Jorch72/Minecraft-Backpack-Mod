package backpack.handler;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.world.WorldEvent;
import backpack.Backpack;

public class EventHandlerBackpack {
    @ForgeSubscribe
    public void playerDropOnDeath(PlayerDropsEvent event) {
        EntityPlayer player = event.entityPlayer;

        ItemStack backpack = Backpack.playerHandler.getBackpack(player);
        if(backpack != null) {
            event.drops.add(new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, backpack));
            Backpack.playerHandler.setBackpack(player, null);
        }
    }

    @ForgeSubscribe
    public void onWorldUnload(WorldEvent.Unload event) {
        Backpack.playerHandler.saveAllPlayerData();
    }
}