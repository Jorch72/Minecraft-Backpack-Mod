package backpack.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent.Specials.Pre;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.opengl.GL11;

import backpack.misc.Constants;

public class EventHandlerRenderPlayer {
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
