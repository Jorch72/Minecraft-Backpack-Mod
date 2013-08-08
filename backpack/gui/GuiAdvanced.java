package backpack.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import backpack.misc.Constants;
import backpack.util.NBTUtil;

@SideOnly(Side.CLIENT)
public abstract class GuiAdvanced extends GuiContainer {
    protected static final int TOPSPACING = 16;
    protected static final int BOTTOMSPACING = 7;

    public GuiAdvanced(Container container) {
        super(container);
    }

    protected void drawTopBorder() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.func_110434_K().func_110577_a(Constants.guiCombined);

        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, TOPSPACING);
    }

    protected void drawBottomBorder() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.func_110434_K().func_110577_a(Constants.guiCombined);

        drawTexturedModalRect(guiLeft, guiTop + ySize - BOTTOMSPACING, 0, 160, xSize, BOTTOMSPACING);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

        if(mc.thePlayer != null) {
            EntityPlayer player = mc.thePlayer;

            ItemStack itemStack = player.getCurrentArmor(2);
            if(itemStack != null) {
                if(NBTUtil.hasTag(itemStack, Constants.WEARED_BACKPACK_OPEN)) {
                    NBTUtil.removeTag(itemStack, Constants.WEARED_BACKPACK_OPEN);
                }
            }
        }
    }
}