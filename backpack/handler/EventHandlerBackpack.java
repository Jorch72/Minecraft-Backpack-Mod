package backpack.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent.Specials.Pre;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

import org.lwjgl.opengl.GL11;

import backpack.Backpack;
import backpack.inventory.InventoryBackpackSlot;
import backpack.misc.Constants;

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
    
    @ForgeSubscribe
    public void render(Pre event) {
        EntityPlayer player = event.entityPlayer;

        if(player.getEntityData().hasKey("backpack")) {
            GL11.glPushMatrix();

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            
            Minecraft.getMinecraft().renderEngine.bindTexture(Constants.modelTexture);

            Constants.model.render(player, 0F, 0F, 0F, 0F, 0F, 0.0625F);

            GL11.glPopMatrix();
        }
    }
}