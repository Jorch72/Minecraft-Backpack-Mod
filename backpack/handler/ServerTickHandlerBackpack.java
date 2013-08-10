package backpack.handler;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import backpack.item.ItemBackpackBase;
import backpack.misc.ConfigurationBackpack;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class ServerTickHandlerBackpack implements ITickHandler {

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        EntityPlayerMP player = (EntityPlayerMP) tickData[0];
        ItemStack[] inventory = player.inventory.mainInventory;
        int counter = 0;
        for(int i = 0; i < inventory.length; i++) {
            if(inventory[i] != null && inventory[i].getItem() instanceof ItemBackpackBase) {
                counter++;
                if(counter > ConfigurationBackpack.MAX_BACKPACK_AMOUNT) {
                    player.dropPlayerItem(inventory[i].copy());
                    inventory[i] = null;
                }
            }
        }
        counter -= ConfigurationBackpack.MAX_BACKPACK_AMOUNT;
        if(counter > 0) {
            player.addChatMessage("[Backpacks] You are not allowed to have more than " + ConfigurationBackpack.MAX_BACKPACK_AMOUNT + " backpacks in your inventory.");
            player.addChatMessage("[Backpacks] " + counter + " backpacks were removed from your inventory. Look on the ground.");
        }
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.PLAYER);
    }

    @Override
    public String getLabel() {
        return "Backpack server tick handler";
    }
}