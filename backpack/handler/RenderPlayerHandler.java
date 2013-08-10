package backpack.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import backpack.Backpack;
import backpack.misc.Constants;

public class RenderPlayerHandler extends RenderPlayer {
    @Override
    protected void renderSpecials(AbstractClientPlayer player, float par2) {
        super.renderSpecials(player, par2);

        ItemStack backpackStack = Backpack.proxy.backpackSlot.getBackpack();
        if(backpackStack != null) {
            GL11.glPushMatrix();

            Minecraft.getMinecraft().renderEngine.func_110577_a(Constants.modelTexture);

            Constants.model.render(player, 0F, 0F, 0F, 0F, 0F, 0.0625F);

            GL11.glPopMatrix();
        }
    }
}
