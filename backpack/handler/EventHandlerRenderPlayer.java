package backpack.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent.Specials.Pre;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.opengl.GL11;

import backpack.Backpack;
import backpack.misc.Constants;
import cpw.mods.fml.client.FMLClientHandler;

public class EventHandlerRenderPlayer {
    @ForgeSubscribe
    public void render(Pre event) {
        EntityPlayer player = event.entityPlayer;
        EntityPlayer thisPlayer = FMLClientHandler.instance().getClient().thePlayer;

        if(Backpack.playerHandler.getClientBackpack() != null && player.equals(thisPlayer)) {
            GL11.glPushMatrix();

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            Minecraft.getMinecraft().renderEngine.bindTexture(Constants.modelTexture);

            Constants.model.render(player, 0F, 0F, 0F, 0F, 0F, 0.0625F);

            GL11.glPopMatrix();
        }
    }
}