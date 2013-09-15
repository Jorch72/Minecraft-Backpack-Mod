package backpack.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

import backpack.misc.Constants;

public class RenderPlayerHandler extends RenderPlayer {
    @Override
    protected void renderSpecials(EntityPlayer player, float par2) {
        super.renderSpecials(player, par2);

        if(player.getEntityData().hasKey("backpack")) {
            GL11.glPushMatrix();

            Minecraft.getMinecraft().renderEngine.bindTexture(Constants.modelTexture);

            Constants.model.render(player, 0F, 0F, 0F, 0F, 0F, 0.0625F);

            GL11.glPopMatrix();
        }
    }
}
