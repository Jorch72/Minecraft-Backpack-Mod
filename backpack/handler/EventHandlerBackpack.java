package backpack.handler;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import backpack.Backpack;

public class EventHandlerBackpack {
    @ForgeSubscribe
    public void playerDropOnDeath(PlayerDropsEvent event) {
        ItemStack backpack = Backpack.proxy.getBackpack();
        if(backpack != null) {
            EntityPlayer player = event.entityPlayer;
            event.drops.add(new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, backpack));
        }
    }
}
